package lotto.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LottoResultTest {

    // 테스트 편의를 위해 LottoResult 객체를 미리 선언합니다.
    private LottoResult lottoResult;

    private final int PURCHASE_AMOUNT = 8000; // 8장 구매 가정

    // 메서드 실행 전에 객체생성
    @BeforeEach
    void setUp() {
        lottoResult = new LottoResult(PURCHASE_AMOUNT);
    }

    //생성자 테스트
    @DisplayName("생성 시 구매 금액이 정확히 저장되고 카운트가 0으로 초기화되어야 한다.")
    @Test
    void constructor_InitializationTest() {
        // given: setUp에서 8000원으로 생성됨

        // when & then
        // private 필드의 Getter가 없으므로 간접적으로 검증
        assertThat(lottoResult.getCountOfFifth()).isEqualTo(0);

        // 수익률이 초기값 0.0인지 (또는 계산 전 상태인지) 확인
        // calculateFinalProfitRate()를 호출해도 카운트가 0이므로 수익률은 0.0
        assertThat(lottoResult.calculateFinalProfitRate()).isZero();
    }

    //incrementCount(Rank rank) 테스트
    @DisplayName("각 등수에 해당하는 카운트가 정확히 증가해야 한다.")
    @Test
    void incrementCount_shouldIncreaseCorrectRank() {
        // given
        // Rank enum이 올바르게 정의되었다고 가정 (Rank.FIRST, Rank.SECOND, Rank.FIFTH)

        // when
        lottoResult.incrementCount(Rank.FIFTH);
        lottoResult.incrementCount(Rank.FIFTH);
        lottoResult.incrementCount(Rank.SECOND);
        lottoResult.incrementCount(Rank.FIRST);

        // then
        assertThat(lottoResult.getCountOfFifth()).isEqualTo(2); // 5등 2회

        // (필요한 경우) 다른 등수의 Getter가 있다면 추가 검증
        // assertThat(lottoResult.getCountOfSecond()).isEqualTo(1);
        // assertThat(lottoResult.getCountOfFirst()).isEqualTo(1);
    }

    @DisplayName("미당첨(MISS) 등수는 카운트가 증가하지 않아야 한다.")
    @Test
    void incrementCount_shouldIgnoreMissRank() {
        // given
        lottoResult.incrementCount(Rank.FIFTH); // 5등 1회 증가

        // when
        lottoResult.incrementCount(Rank.MISS);
        lottoResult.incrementCount(Rank.MISS);

        // then
        assertThat(lottoResult.getCountOfFifth()).isEqualTo(1);
        // (다른 카운트 변수들 역시 0을 유지해야 함을 확인)
    }

    //calculateFinalProfitRate() 테스트
    @DisplayName("수익률을 정확하게 계산해야 한다. (1등 당첨)")
    @Test
    void calculateFinalProfitRate_WithFirstRank() {
        // given: 구매 금액 8000원
        lottoResult.incrementCount(Rank.FIRST); // 1등 1회 당첨

        // 총 상금: 2,000,000,000
        // 수익률: (2,000,000,000 / 8000) * 100 = 25,000,000.0%

        // when
        double profitRate = lottoResult.calculateFinalProfitRate();

        // then
        assertThat(profitRate).isEqualTo(25_000_000.0);
    }

    @DisplayName("수익률을 정확하게 계산해야 한다. (5등 8회 당첨)")
    @Test
    void calculateFinalProfitRate_WithFifthRank() {
        // given: 구매 금액 8000원
        for (int i = 0; i < 8; i++) {
            lottoResult.incrementCount(Rank.FIFTH); // 5등 8회 당첨 (5000원 * 8회 = 40,000원)
        }

        // 총 상금: 40,000원
        // 수익률: (40,000 / 8000) * 100 = 500.0%

        // when
        double profitRate = lottoResult.calculateFinalProfitRate();

        // then
        assertThat(profitRate).isEqualTo(500.0);
    }

    @DisplayName("수익률을 정확하게 계산해야 한다. (소수점 포함 및 복합 당첨)")
    @Test
    void calculateFinalProfitRate_Complex() {
        // given: 구매 금액 8000원
        lottoResult.incrementCount(Rank.FIFTH); // 5등 1회 (5,000)
        lottoResult.incrementCount(Rank.SECOND); // 2등 1회 (30,000,000)

        // 총 상금: 30,005,000원
        // 수익률: (30,005,000 / 8000) * 100 = 375,062.5%

        // when
        double profitRate = lottoResult.calculateFinalProfitRate();

        // then
        assertThat(profitRate).isEqualTo(375062.5);
    }

    @DisplayName("구매 금액이 0일 경우 수익률은 0.0을 반환해야 한다.")
    @Test
    void calculateFinalProfitRate_ZeroPurchase() {
        // given
        LottoResult zeroPurchaseResult = new LottoResult(0);
        zeroPurchaseResult.incrementCount(Rank.FIRST);

        // when
        double profitRate = zeroPurchaseResult.calculateFinalProfitRate();

        // then
        assertThat(profitRate).isEqualTo(0.0);
    }
}
package lotto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LottoResultTest {

    private final int PURCHASE_AMOUNT = 8000; // 8장 구매 가정 (1000원 * 8)

    // LottoResult는 Service에서 Map을 받아 생성된다고 가정하고 테스트합니다.

    @DisplayName("getTotalPrize()가 총 상금 금액을 정확히 계산해야 한다.")
    @Test
    void getTotalPrize_CalculationTest() {
        // given: 5등 8회, 2등 1회 당첨 시나리오
        Map<Rank, Integer> results = new HashMap<>();
        results.put(Rank.FIFTH, 8); // 5,000원 * 8 = 40,000원
        results.put(Rank.SECOND, 1); // 30,000,000원 * 1 = 30,000,000원

        // 총 상금: 30,040,000원
        LottoResult lottoResult = new LottoResult(results, PURCHASE_AMOUNT);

        // when
        double totalPrize = lottoResult.getTotalPrize();

        // then
        assertThat(totalPrize).isEqualTo(30_040_000.0);
    }

    @DisplayName("calculateProfitRate()가 수익률을 정확히 계산해야 한다. (복합 당첨)")
    @Test
    void calculateProfitRate_Complex() {
        // given: 총 상금 30,040,000원, 구매 금액 8000원
        Map<Rank, Integer> results = new HashMap<>();
        results.put(Rank.FIFTH, 8);
        results.put(Rank.SECOND, 1);
        LottoResult lottoResult = new LottoResult(results, PURCHASE_AMOUNT);

        // 수익률: (30,040,000 / 8000) * 100 = 375,500.0%

        // when
        double profitRate = lottoResult.calculateProfitRate();

        // then
        // 소수점 정확도를 위해 isCloseTo를 사용합니다.
        assertThat(profitRate).isCloseTo(375500.0, org.assertj.core.data.Offset.offset(0.001));
    }

    @DisplayName("당첨이 전혀 없을 경우 수익률은 0.0을 반환해야 한다.")
    @Test
    void calculateProfitRate_NoWins() {
        // given: 당첨 횟수가 없는 빈 Map
        Map<Rank, Integer> results = new HashMap<>();
        LottoResult lottoResult = new LottoResult(results, PURCHASE_AMOUNT);

        // when
        double profitRate = lottoResult.calculateProfitRate();

        // then
        assertThat(profitRate).isEqualTo(0.0);
    }

    @DisplayName("구매 금액이 0일 경우 수익률은 0.0을 반환해야 한다.")
    @Test
    void calculateProfitRate_ZeroPurchase() {
        // given: 1등 당첨이 있지만 구매 금액이 0원
        Map<Rank, Integer> results = new HashMap<>();
        results.put(Rank.FIRST, 1);
        LottoResult zeroPurchaseResult = new LottoResult(results, 0);

        // when
        double profitRate = zeroPurchaseResult.calculateProfitRate();

        // then
        assertThat(profitRate).isEqualTo(0.0);
    }
}
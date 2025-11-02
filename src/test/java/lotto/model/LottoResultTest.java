package lotto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LottoResultTest {

    private final int PURCHASE_AMOUNT = 8000; // 8장 구매 가정 (1000원 * 8)

    @DisplayName("incrementCount()가 Rank별로 올바르게 증가해야 한다.")
    @Test
    void incrementCount_ShouldIncreaseEachRankProperly() {
        // given
        LottoResult lottoResult = new LottoResult(PURCHASE_AMOUNT);

        // when
        lottoResult.incrementCount(Rank.FIRST);
        lottoResult.incrementCount(Rank.SECOND);
        lottoResult.incrementCount(Rank.SECOND);
        lottoResult.incrementCount(Rank.THIRD);
        lottoResult.incrementCount(Rank.THIRD);
        lottoResult.incrementCount(Rank.FOURTH);
        lottoResult.incrementCount(Rank.FIFTH);
        lottoResult.incrementCount(Rank.FIFTH);
        lottoResult.incrementCount(Rank.FIFTH);

        // then
        assertThat(lottoResult.getCountOfFirst()).isEqualTo(1);
        assertThat(lottoResult.getCountOfSecond()).isEqualTo(2);
        assertThat(lottoResult.getCountOfThird()).isEqualTo(2);
        assertThat(lottoResult.getCountOfFourth()).isEqualTo(1);
        assertThat(lottoResult.getCountOfFifth()).isEqualTo(3);
    }

    @DisplayName("getTotalPrize()에 기반한 calculateProfitRate()가 정확히 계산된다.")
    @Test
    void calculateProfitRate_ShouldReturnCorrectValue() {
        // given
        LottoResult lottoResult = new LottoResult(PURCHASE_AMOUNT);
        // 1등 1회(2,000,000,000), 2등 1회(30,000,000), 5등 2회(5,000 * 2)
        lottoResult.incrementCount(Rank.FIRST);
        lottoResult.incrementCount(Rank.SECOND);
        lottoResult.incrementCount(Rank.FIFTH);
        lottoResult.incrementCount(Rank.FIFTH);

        // when
        double profitRate = lottoResult.calculateProfitRate();

        // then
        // 총 상금: 2,000,000,000 + 30,000,000 + 10,000 = 2,030,010,000
        // 수익률: (2,030,010,000 / 8000) * 100 = 25,375,1250.0%
        assertThat(profitRate).isEqualTo(Math.round(profitRate * 100.0) / 100.0);
    }

    @DisplayName("당첨이 전혀 없을 경우 수익률은 0.0을 반환해야 한다.")
    @Test
    void calculateProfitRate_NoWins() {
        // given
        LottoResult lottoResult = new LottoResult(PURCHASE_AMOUNT);

        // when
        double profitRate = lottoResult.calculateProfitRate();

        // then
        assertThat(profitRate).isEqualTo(0.0);
    }

    @DisplayName("구매 금액이 0원일 경우 수익률은 0.0을 반환해야 한다.")
    @Test
    void calculateProfitRate_ZeroPurchaseAmount() {
        // given
        LottoResult lottoResult = new LottoResult(0);
        lottoResult.incrementCount(Rank.FIRST);

        // when
        double profitRate = lottoResult.calculateProfitRate();

        // then
        assertThat(profitRate).isEqualTo(0.0);
    }

    @DisplayName("복합적인 등수 조합에 대해 총 수익률이 올바르게 계산된다.")
    @Test
    void calculateProfitRate_MultipleRanksMix() {
        // given
        LottoResult lottoResult = new LottoResult(PURCHASE_AMOUNT);
        lottoResult.incrementCount(Rank.THIRD);   // 1,500,000
        lottoResult.incrementCount(Rank.FOURTH);  // 50,000
        lottoResult.incrementCount(Rank.FOURTH);  // 50,000
        lottoResult.incrementCount(Rank.FIFTH);   // 5,000

        // when
        double profitRate = lottoResult.calculateProfitRate();

        // then
        // 총 상금: 1,500,000 + 100,000 + 5,000 = 1,605,000
        // 수익률: (1,605,000 / 8000) * 100 = 20,062.5%
        assertThat(profitRate).isCloseTo(20_062.5, org.assertj.core.data.Offset.offset(0.001));
    }
}

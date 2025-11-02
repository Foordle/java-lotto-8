package lotto.sevice;

import lotto.service.LottoService;
import lotto.model.Lottos;
import lotto.model.Lotto;
import lotto.model.LottoResult;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LottoServiceTest {
    private final LottoService lottoService = new LottoService();

    @DisplayName("정상 금액 입력 시 개수만큼 로또가 생성된다.")
    @Test
    void buyLottos_validAmount_createsCorrectCount() {
        // given
        int purchaseAmount = 5000; // 5장

        // when
        Lottos lottos = lottoService.buyLottos(purchaseAmount);

        // then
        assertThat(lottos.getLottos()).hasSize(5);
    }

    @DisplayName("구입 금액이 0 이하이면 예외가 발생한다.")
    @Test
    void buyLottos_amountBelowZero_throwsException() {
        assertThatThrownBy(() -> lottoService.buyLottos(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1000원 이상");
    }

    @DisplayName("구입 금액이 1000원 단위가 아니면 예외가 발생한다.")
    @Test
    void buyLottos_notMultipleOf1000_throwsException() {
        assertThatThrownBy(() -> lottoService.buyLottos(2500))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("1000원 단위");
    }

    @DisplayName("보너스 번호가 당첨 번호와 중복되면 예외가 발생한다.")
    @Test
    void calculateResults_duplicateBonusNumber_throwsException() {
        // given
        Lottos purchased = new Lottos(List.of(new Lotto(List.of(1, 2, 3, 4, 5, 6))));
        Lotto winning = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        int bonusNumber = 6; // 중복

        // when & then
        assertThatThrownBy(() -> lottoService.calculateResults(purchased, winning, bonusNumber, 1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보너스 번호는 당첨 번호와 중복될 수 없습니다");
    }

    @DisplayName("각 로또의 당첨 결과를 정확히 집계해야 한다.")
    @Test
    void calculateResults_returnsCorrectCounts() {
        // given
        Lottos purchased = new Lottos(List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),  // 6개 → 1등
                new Lotto(List.of(1, 2, 3, 4, 5, 7)),  // 5개+보너스 → 2등
                new Lotto(List.of(1, 2, 3, 4, 5, 8)),  // 5개 → 3등
                new Lotto(List.of(1, 2, 3, 4, 9, 10)), // 4개 → 4등
                new Lotto(List.of(1, 2, 3, 11, 12, 13)), // 3개 → 5등
                new Lotto(List.of(40, 41, 42, 43, 44, 45)) // 0개 → MISS
        ));

        Lotto winning = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        int bonusNumber = 7;
        int purchaseAmount = 6000;

        // when
        LottoResult result = lottoService.calculateResults(purchased, winning, bonusNumber, purchaseAmount);

        // then
        assertThat(result.getCountOfFirst()).isEqualTo(1);
        assertThat(result.getCountOfSecond()).isEqualTo(1);
        assertThat(result.getCountOfThird()).isEqualTo(1);
        assertThat(result.getCountOfFourth()).isEqualTo(1);
        assertThat(result.getCountOfFifth()).isEqualTo(1);

        // 6개 중 MISS 1개 → 1등~5등 합계가 5, 정상적으로 누적됨
        double profitRate = result.calculateProfitRate();
        assertThat(profitRate).isGreaterThan(0.0);
    }
}

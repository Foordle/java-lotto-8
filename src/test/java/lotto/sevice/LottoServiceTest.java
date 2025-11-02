package lotto.sevice;

import lotto.service.LottoService;
import lotto.model.Lottos;
import lotto.model.Lotto;
import lotto.model.LottoResult;
import lotto.model.Rank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class LottoServiceTest {
    private final LottoService lottoService = new LottoService();

    @DisplayName("정상 금액 입력 시 개수만큼 로또가 생성된다.")
    @Test
    void buyLottos_validAmount_createsCorrectCount() {
        // given
        int purchaseAmount = 5000; // 5장 구매

        // when
        Lottos lottos = lottoService.buyLottos(purchaseAmount);

        // then
        assertThat(lottos.getLottos()).hasSize(5);
    }

    @DisplayName("구입 금액이 0원 이하이면 예외가 발생한다.")
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
        int bonus = 6;

        // when & then
        assertThatThrownBy(() -> lottoService.calculateResults(purchased, winning, bonus, 1000))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보너스 번호는 당첨 번호와 중복될 수 없습니다");
    }

    @DisplayName("당첨 로또와 일치하는 개수에 따라 Rank가 올바르게 계산된다.")
    @Test
    void calculateResults_returnsCorrectRanks() {
        // given
        Lottos purchased = new Lottos(List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)), // 6개 일치 → 1등
                new Lotto(List.of(1, 2, 3, 4, 5, 7)), // 5개 + 보너스 → 2등
                new Lotto(List.of(1, 2, 3, 4, 5, 8)), // 5개 → 3등
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
        Map<Rank, Integer> counts = result.getWinningCounts();

        assertThat(counts.get(Rank.FIRST)).isEqualTo(1);
        assertThat(counts.get(Rank.SECOND)).isEqualTo(1);
        assertThat(counts.get(Rank.THIRD)).isEqualTo(1);
        assertThat(counts.get(Rank.FOURTH)).isEqualTo(1);
        assertThat(counts.get(Rank.FIFTH)).isEqualTo(1);
        assertThat(counts.get(Rank.MISS)).isEqualTo(1);
    }
}

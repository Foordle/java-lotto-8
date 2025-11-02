package lotto.sevice;

import lotto.service.LottoService;
import lotto.model.Lottos;
import lotto.model.Lotto;
import lotto.model.LottoResult;
import lotto.service.LottoGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LottoServiceTest {
    private LottoService lottoService;
    private final int LOTTO_PRICE = 1000;
    private final List<Integer> VALID_NUMBERS = List.of(1, 2, 3, 4, 5, 6);

    @BeforeEach
    void setUp() {
        // 실제 LottoGenerator 객체를 생성하여 Service에 주입합니다.
        this.lottoService = new LottoService(new LottoGenerator());
    }

    @DisplayName("정상 금액 입력 시 개수만큼 로또가 생성된다.")
    @Test
    void buyLottos_validAmount_createsCorrectCount() {
        // given
        int purchaseAmount = 5000; // 5장

        // when
        Lottos lottos = lottoService.buyLottos(purchaseAmount);

        // then
        // 생성된 로또의 '내용'은 무시하고, '개수'만 검증합니다.
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
        Lottos purchased = new Lottos(List.of(new Lotto(VALID_NUMBERS)));
        Lotto winning = new Lotto(VALID_NUMBERS);
        int bonusNumber = 6; // 중복

        // when & then
        assertThatThrownBy(() -> lottoService.calculateResults(purchased, winning, bonusNumber, LOTTO_PRICE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보너스 번호는 당첨 번호와 중복될 수 없습니다");
    }

    @DisplayName("각 로또의 당첨 결과를 정확히 집계해야 한다.")
    @Test
    void calculateResults_returnsCorrectCounts() {
        // given
        // 이 테스트는 난수 생성과 무관하므로, 테스트 코드가 직접 Lotto 객체를 만들어 사용합니다.
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

        // 수익률 검증
        double expectedPrize = 2000000000.0 + 30000000.0 + 1500000.0 + 50000.0 + 5000.0;
        double expectedRate = (expectedPrize / purchaseAmount) * 100.0;

        assertThat(result.calculateProfitRate()).isEqualTo(expectedRate);
    }
}

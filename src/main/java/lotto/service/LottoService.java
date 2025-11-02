package lotto.service;
import lotto.model.*;
import java.util.List;

public class LottoService {
    private static final int LOTTO_PRICE = 1000;
    private final LottoGenerator lottoGenerator; // final로 선언하고 생성자 주입을 준비합니다.

    // 생성자를 통해 의존성(LottoGenerator)을 주입받습니다. (Mocking/Stubbing 용이)
    public LottoService(LottoGenerator lottoGenerator) {
        this.lottoGenerator = lottoGenerator;
    }


    public Lottos buyLottos(int amount) {
        validatePurchaseAmount(amount);
        int count = amount / LOTTO_PRICE;
        return generateLottos(count);
    }

    private void validatePurchaseAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1000원 이상이어야 합니다.");
        }
        if (amount % LOTTO_PRICE != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1000원 단위로 입력해야 합니다.");
        }
    }

    private Lottos generateLottos(int count) {
        List<Lotto> purchasedLottos = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            purchasedLottos.add(lottoGenerator.generateLotto());
        }
        return new Lottos(purchasedLottos);
    }


    public LottoResult calculateResults(Lottos purchasedLottos, Lotto winningLotto, int bonusNumber, int purchaseAmount) {
        validateBonusNumber(winningLotto, bonusNumber);
        LottoResult result = new LottoResult(purchaseAmount);

        for (Lotto lotto : purchasedLottos.getLottos()) {
            Rank rank = determineRank(lotto, winningLotto, bonusNumber);

            result.incrementCount(rank);
        }

        return result;
    }

    private void validateBonusNumber(Lotto winningLotto, int bonusNumber) {
        if (winningLotto.contains(bonusNumber)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }

    private Rank determineRank(Lotto lotto, Lotto winningLotto, int bonusNumber) {
        int matchCount = lotto.countMatch(winningLotto);
        boolean isBonusMatch = lotto.contains(bonusNumber);
        return Rank.measurementRank(matchCount, isBonusMatch);
    }

}
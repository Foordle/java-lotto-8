package lotto.service;

import lotto.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LottoService {
    private static final int LOTTO_PRICE = 1000;
    private final LottoGenerator lottoGenerator = new LottoGenerator();

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

    /**
     * 당첨 번호와 보너스 번호를 사용하여 최종 결과를 계산합니다.
     */
    public LottoResult calculateResults(Lottos purchasedLottos, Lotto winningLotto, int bonusNumber, int purchaseAmount) {
        validateBonusNumber(winningLotto, bonusNumber);

        Map<Rank, Integer> results = initializeResults();

        for (Lotto lotto : purchasedLottos.getLottos()) {
            Rank rank = determineRank(lotto, winningLotto, bonusNumber);
            incrementRankCount(results, rank);
        }

        return new LottoResult(results, purchaseAmount);
    }

    private void validateBonusNumber(Lotto winningLotto, int bonusNumber) {
        if (winningLotto.contains(bonusNumber)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
        // 범위 검증은 InputView에서 문자열 파싱 시 처리되지만, Service에서 최종 확인하는 것이 안전합니다.
    }

    private Map<Rank, Integer> initializeResults() {
        Map<Rank, Integer> results = new HashMap<>();
        for (Rank rank : Rank.values()) {
            results.put(rank, 0);
        }
        return results;
    }

    private Rank determineRank(Lotto lotto, Lotto winningLotto, int bonusNumber) {
        int matchCount = lotto.countMatch(winningLotto);
        boolean isBonusMatch = lotto.contains(bonusNumber);
        return Rank.measurementRank(matchCount, isBonusMatch);
    }

    private void incrementRankCount(Map<Rank, Integer> results, Rank rank) {
        if (rank != Rank.MISS) {
            results.put(rank, results.get(rank) + 1);
        }
    }
}

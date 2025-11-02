package lotto.model;

import java.util.Map;
import java.util.Optional;

public class LottoResult {
    private final Map<Rank, Integer> result;
    private final int purchaseAmount; // 수익률 계산을 위해 필요

    public LottoResult(Map<Rank, Integer> result, int purchaseAmount) {
        this.result = result;
        this.purchaseAmount = purchaseAmount;
    }

    /**
     * View가 당첨 통계를 가져가기 위한 Getter 메서드입니다.
     */
    public Map<Rank, Integer> getWinningCounts() {
        return result;
    }

    /**
     * 총 상금 계산 로직을 수행합니다. (수익률 계산에 사용)
     */
    public double getTotalPrize() {
        return result.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrize() * entry.getValue())
                .sum();
    }

    /**
     * 수익률을 계산하는 최종 비즈니스 로직입니다.
     */
    public double calculateProfitRate() {
        if (purchaseAmount == 0) {
            return 0.0;
        }
        // 총 상금을 구매 금액으로 나누어 수익률을 계산합니다.
        return (getTotalPrize() / purchaseAmount) * 100.0;
    }
}
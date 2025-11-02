package lotto.model;
import java.util.Map;

public class LottoResult {
    private final Map<Rank, Integer> result;
    private final int purchaseAmount; // 수익률 계산을 위해 필요

    public LottoResult(Map<Rank, Integer> result, int purchaseAmount) {
        this.result = result;
        this.purchaseAmount = purchaseAmount;
    }

    public Map<Rank, Integer> getWinningCounts() {
        return result;
    }

    public double getTotalPrize() {
        return result.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrize() * entry.getValue())
                .sum();
    }

    public double calculateProfitRate() {
        if (purchaseAmount == 0) {
            return 0.0;
        }
        // 총 상금을 구매 금액으로 나누어 수익률을 계산합니다.
        return (getTotalPrize() / purchaseAmount) * 100.0;
    }
}
package lotto.model;
import java.util.Map;

public class LottoResult {

    private final int purchaseAmount; // 수익률 계산

    private int countOfFirst = 0;
    private int countOfSecond = 0;
    private int countOfThird = 0;
    private int countOfFourth = 0;
    private int countOfFifth = 0;

    public LottoResult(int purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public void incrementCount(Rank rank) {
        if (rank == Rank.FIRST) {
            countOfFirst++;
            return;
        }
        if (rank == Rank.SECOND) {
            countOfSecond++;
            return;
        }
        if (rank == Rank.THIRD) {
            countOfThird++;
            return;
        }
        if (rank == Rank.FOURTH) {
            countOfFourth++;
            return;
        }
        if (rank == Rank.FIFTH) {
            countOfFifth++;
            return;
        }
    }

    private double getTotalPrize() {
        return (double) countOfFirst * Rank.FIRST.getPrize() +
                (double) countOfSecond * Rank.SECOND.getPrize() +
                (double) countOfThird * Rank.THIRD.getPrize() +
                (double) countOfFourth * Rank.FOURTH.getPrize() +
                (double) countOfFifth * Rank.FIFTH.getPrize();
    }

    public double calculateProfitRate() {
        if (purchaseAmount == 0) {
            return 0.0;
        }
        double rawRate = (getTotalPrize() / purchaseAmount) * 100.0;
        return Math.round(rawRate * 100.0) / 100.0; // 소수둘째자리까지
    }

    // OutputView를 위한 Getter 메서드
    public int getCountOfFirst() { return countOfFirst; }
    public int getCountOfSecond() { return countOfSecond; }
    public int getCountOfThird() { return countOfThird; }
    public int getCountOfFourth() { return countOfFourth; }
    public int getCountOfFifth() { return countOfFifth; }
}
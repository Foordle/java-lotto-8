package lotto.model;

public class LottoResult {
    // 1등부터 5등까지 각 등수의 당첨 횟수를 저장하는 변수들
    private int countOfFirst = 0;
    private int countOfSecond = 0;
    private int countOfThird = 0;
    private int countOfFourth = 0;
    private int countOfFifth = 0;

    private final int purchaseAmount;
    private final double profitRate;

    // 생성자에서 총 상금 계산 및 수익률 계산을 수행합니다.
    public LottoResult(int purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
        // profitRate는 모든 등수 카운트가 설정된 후 최종적으로 계산됩니다.
        this.profitRate = 0.0; // 초기값 설정 후, Service에서 최종적으로 계산됨
    }

    /**
     * Service 계층에서 당첨 결과를 반영할 때 사용되는 메서드
     */
    public void incrementCount(Rank rank) {
        if (rank == Rank.FIRST) {
            countOfFirst++;
            return;
        }
        if (rank == Rank.SECOND) {
            countOfSecond++;
            return;
        }

        if (rank == Rank.FIFTH) {
            countOfFifth++;
        }
    }


    public double calculateFinalProfitRate() {
        long totalPrize = (long) countOfFirst * Rank.FIRST.getPrize() +
                (long) countOfSecond * Rank.SECOND.getPrize() +
                // ... (나머지 등수 상금 합산)
                (long) countOfFifth * Rank.FIFTH.getPrize();

        if (purchaseAmount == 0) {
            return 0.0;
        }
        return (double) totalPrize / purchaseAmount * 100.0;
    }

    public int getCountOfFifth() {
        return countOfFifth;
    }


}
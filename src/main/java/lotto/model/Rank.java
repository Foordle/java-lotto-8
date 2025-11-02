package lotto.model;

import java.util.Arrays;

public enum Rank {
    FIRST(6, false, 2_000_000_000),
    SECOND(5, true, 30_000_000),
    THIRD(5, false, 1_500_000),
    FOURTH(4, false, 50_000),
    FIFTH(3, false, 5_000),
    MISS(0, false, 0);

    private final int matchCount;
    private final boolean bonusMatch;
    private final int prize;

    Rank(int matchCount, boolean bonusMatch, int prize) {
        this.matchCount = matchCount;
        this.bonusMatch = bonusMatch;
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }

    // 매칭된 개수와 보너스 번호 일치 여부로 Rank 결정
    public static Rank measurementRank(int matchCount, boolean bonusMatched) {
        if (matchCount < 3) {
            return MISS;
        }
        return Arrays.stream(values())
                .filter(rank -> rank.matchCount == matchCount)
                .filter(rank -> rank.bonusMatch == bonusMatched)
                .filter(rank -> rank != MISS)
                .findFirst()
                .orElse(MISS);
    }
    public String getRankString() {
        String prizeFormatted = String.format("%,d", prize); // 금액 포맷팅

        if (this == SECOND) {
            return String.format("%d개 일치, 보너스 볼 일치 (%s원)", matchCount, prizeFormatted);
        }
        if (this == MISS) {
            return "미당첨"; // 디버깅을 위해 일단...
        }
        return String.format("%d개 일치 (%s원)", matchCount, prizeFormatted);
    }
}

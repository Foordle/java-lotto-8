package lotto.model;

import java.util.Arrays;

public enum Rank {
    // 순서는 출력을 위해 낮은 등수부터 높은 등수 순으로 지정
    // (match, bonusMatch, prize, rankOrder)
    FIFTH(3, false, 5_000, 5),
    FOURTH(4, false, 50_000, 4),
    THIRD(5, false, 1_500_000, 3),
    SECOND(5, true, 30_000_000, 2),
    FIRST(6, false, 2_000_000_000, 1),
    MISS(0, false, 0, 6); // 미당첨은 항상 마지막 순서

    private final int matchCount;
    private final boolean bonusMatch;
    private final int prize;
    private final int rankOrder;

    Rank(int matchCount, boolean bonusMatch, int prize, int rankOrder) {
        this.matchCount = matchCount;
        this.bonusMatch = bonusMatch;
        this.prize = prize;
        this.rankOrder = rankOrder;
    }

    // 일치 횟수와 보너스 일치 여부로 해당 등수를 찾아 반환합니다.
    public static Rank valueOf(int matchCount, boolean bonusMatch) {
        if (matchCount < 3) {
            return MISS;
        }

        return Arrays.stream(values())
                .filter(rank -> rank.matchCount == matchCount)
                .filter(rank -> rank.bonusMatch == bonusMatch)
                .filter(rank -> rank != MISS)
                .findFirst()
                .orElse(MISS);
    }

    public int getPrize() {
        return prize;
    }


    public int getRankOrder() {
        return rankOrder;
    }


    public String getDescription() {
        if (this == SECOND) {
            return String.format("%d개 일치, 보너스 볼 일치 (%s원)", matchCount, String.format("%,d", prize));
        }
        if (this == MISS) {
            return "미당첨";
        }
        return String.format("%d개 일치 (%s원)", matchCount, String.format("%,d", prize));
    }
}
package lotto.view;

import lotto.model.Lottos;
import lotto.model.LottoResult;
import lotto.model.Rank;

public class OutputView {

    // 로또 발행 출력은 그대로 유지
    public static void printPurchasedLottos(Lottos lottos) {
        System.out.printf("\n%d개를 구매했습니다.\n", lottos.getNumberOfLottos());

        for (String lottoString : lottos.getLottosDisplayStrings()) {
            System.out.println(lottoString);
        }
    }

    // --- 당첨 결과 출력 로직 (핵심 수정) ---

    public static void printWinningResult(LottoResult result) {
        System.out.println("\n당첨 통계");
        System.out.println("---");

        // 5등부터 1등까지 순서대로 출력하는 메서드를 호출
        printRankResults(result);

        // 수익률 계산 및 출력
        double profitRate = result.calculateProfitRate();
        double roundedRate = Math.round(profitRate * 10.0) / 10.0;

        System.out.printf("총 수익률은 %.1f%%입니다.\n", roundedRate);
    }

    /**
     * 고정된 포맷 템플릿에 LottoResult의 Getter 값을 순서대로 매핑하여 출력합니다.
     * 이 방법은 View가 포맷팅 책임을 가장 단순하게 수행하는 방식입니다.
     */
    private static void printRankResults(LottoResult result) {
        System.out.printf("%s - %d개\n", Rank.FIFTH.getRankString(), result.getCountOfFifth());
        System.out.printf("%s - %d개\n", Rank.FOURTH.getRankString(), result.getCountOfFourth());
        System.out.printf("%s - %d개\n", Rank.THIRD.getRankString(), result.getCountOfThird());
        System.out.printf("%s - %d개\n", Rank.SECOND.getRankString(), result.getCountOfSecond());
        System.out.printf("%s - %d개\n", Rank.FIRST.getRankString(), result.getCountOfFirst());
    }

    public static void printError(String message) {
        System.out.println(message);
    }
}
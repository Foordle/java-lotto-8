package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    private static final String ERROR_MESSAGE = "[ERROR]";

    @Test
    void 기능_테스트() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    run("8000", "1,2,3,4,5,6", "7");
                    assertThat(output()).contains(
                            "8개를 구매했습니다.",
                            "[8, 21, 23, 41, 42, 43]",
                            "[3, 5, 11, 16, 32, 38]",
                            "[7, 11, 16, 35, 36, 44]",
                            "[1, 8, 11, 31, 41, 42]",
                            "[13, 14, 16, 38, 42, 45]",
                            "[7, 11, 30, 40, 42, 43]",
                            "[2, 13, 22, 32, 38, 45]",
                            "[1, 3, 5, 14, 22, 45]",
                            "3개 일치 (5,000원) - 1개",
                            "4개 일치 (50,000원) - 0개",
                            "5개 일치 (1,500,000원) - 0개",
                            "5개 일치, 보너스 볼 일치 (30,000,000원) - 0개",
                            "6개 일치 (2,000,000,000원) - 0개",
                            "총 수익률은 62.5%입니다."
                    );
                },
                List.of(8, 21, 23, 41, 42, 43),
                List.of(3, 5, 11, 16, 32, 38),
                List.of(7, 11, 16, 35, 36, 44),
                List.of(1, 8, 11, 31, 41, 42),
                List.of(13, 14, 16, 38, 42, 45),
                List.of(7, 11, 30, 40, 42, 43),
                List.of(2, 13, 22, 32, 38, 45),
                List.of(1, 3, 5, 14, 22, 45)
        );
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("1000j");
            assertThat(output()).contains(ERROR_MESSAGE);
        });
    }


    @DisplayName("구매 금액 입력 시 예외 처리 및 재입력을 확인한다.")
    @Test
    void 예외_테스트_구매금액() {
        assertSimpleTest(() -> {
            // runException: 잘못된 입력 -> 에러 메시지 출력 -> 정상 입력
            runException("1000j", "1500", "3000", "1,2,3,4,5,6", "7");

            // 출력에 에러 메시지가 2번 포함되고, 최종적으로 3000원 기준으로 "3개를 구매했습니다"가 출력되어야 함.
            assertThat(output()).contains(
                    ERROR_MESSAGE, // 1000j (비숫자)
                    ERROR_MESSAGE, // 1500 (1000원 단위 아님)
                    "3개를 구매했습니다."
            );
        });
    }

    @DisplayName("당첨 번호 입력 시 예외 처리 및 재입력을 확인한다.")
    @Test
    void 예외_테스트_당첨번호() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // 입력:
                    // 1. 구매 금액 (정상)
                    // 2. 당첨 번호 (범위 초과) -> 에러 -> (중복) -> 에러 -> (정상)
                    runException("3000", "1,2,3,4,5,46", "1,2,3,4,5,5", "1,2,3,4,5,6", "7");

                    assertThat(output()).contains(
                            "3개를 구매했습니다.",
                            ERROR_MESSAGE, // 1,2,3,4,5,46 (45 초과)
                            ERROR_MESSAGE, // 1,2,3,4,5,5 (중복)
                            "보너스 번호를 입력해 주세요." // 정상 진행 후 다음 단계로 이동
                    );
                },
                List.of(10, 20, 30, 40, 41, 42),
                List.of(11, 21, 31, 41, 42, 43),
                List.of(12, 22, 32, 42, 43, 44)
        );
    }

    @DisplayName("보너스 번호 입력 시 예외 처리 (당첨번호 중복)를 확인한다.")
    @Test
    void 예외_테스트_보너스번호_중복() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    // 입력:
                    // 1. 구매 금액 (정상)
                    // 2. 당첨 번호 (정상)
                    // 3. 보너스 번호 (당첨 번호와 중복) -> 에러 -> (정상)
                    runException("3000", "1,2,3,4,5,6", "6", "7");

                    assertThat(output()).contains(
                            "3개를 구매했습니다.",
                            ERROR_MESSAGE, // 보너스 6 (당첨 번호와 중복)
                            "총 수익률은 0.0%입니다." // 최종적으로 정상 종료됨
                    );
                },
                List.of(10, 20, 30, 40, 41, 42),
                List.of(11, 21, 31, 41, 42, 43),
                List.of(12, 22, 32, 42, 43, 44)
        );
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}

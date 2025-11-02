package lotto.controller;

import lotto.model.Lotto;
import lotto.model.Lottos;
import lotto.model.LottoResult;
import lotto.service.LottoService;
import lotto.view.InputView;
import lotto.view.OutputView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public class LottoController {
    private final LottoService lottoService;

    public LottoController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    public void run() {
        // 1. 로또 구매 및 발행
        Lottos purchasedLottos = purchaseLotto();
        if (purchasedLottos == null) {
            return; // 예외 발생 후 프로그램 종료 (실제 환경에 따라 처리)
        }

        // 2. 당첨 번호 입력 및 검증
        Lotto winningLotto = readWinningLotto();
        int bonusNumber = readBonusNumber(winningLotto);

        // 3. 결과 계산 및 출력
        int purchaseAmount = purchasedLottos.getNumberOfLottos() * 1000;

        LottoResult result = lottoService.calculateResults(
                purchasedLottos, winningLotto, bonusNumber, purchaseAmount
        );
        OutputView.printWinningResult(result);
    }

    // 1. 로또 구매 및 발행 (예외 처리 루프) 
    private Lottos purchaseLotto() {
        try {
            int amount = readPurchaseAmount();
            Lottos lottos = lottoService.buyLottos(amount);
            OutputView.printPurchasedLottos(lottos);
            return lottos;
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
            // 재귀 호출을 통해 예외 발생 지점부터 입력을 다시 받습니다.
            return purchaseLotto();
        }
    }

    private int readPurchaseAmount() {
        String input = InputView.readPurchaseAmount();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 숫자여야 합니다.");
        }
    }

    // 2. 당첨 번호 입력 (예외 처리 루프) 

    private Lotto readWinningLotto() {
        try {
            String input = InputView.readWinningNumbers();
            List<Integer> numbers = parseNumbers(input);
            // Lotto 객체 생성 시 유효성 검증 (개수, 범위, 중복)이 발생할 수 있습니다.
            return new Lotto(numbers);
        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
            return readWinningLotto(); // 재입력
        }
    }

    private int readBonusNumber(Lotto winningLotto) {
        try {
            String input = InputView.readBonusNumber();
            int bonus = parseSingleNumber(input);

            validateBonusNumberAgainstWinningLotto(winningLotto, bonus);
            return bonus;

        } catch (IllegalArgumentException e) {
            OutputView.printError(e.getMessage());
            return readBonusNumber(winningLotto); // 재입력
        }
    }

    // 3. 헬퍼 및 파싱 메서드 (Controller의 기술적 변환 책임) 

    private void validateBonusNumberAgainstWinningLotto(Lotto winningLotto, int bonusNumber) {
        // Model의 contains() 메서드를 이용해 보너스 번호 중복을 검증합니다.
        if (winningLotto.contains(bonusNumber)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }

    private List<Integer> parseNumbers(String input) {
        try {
            // 문자열을 쉼표로 분리하고, 공백 제거 후 숫자로 변환합니다.
            return Arrays.stream(input.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 숫자여야 합니다.");
        }
    }

    private int parseSingleNumber(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 숫자여야 합니다.");
        }
    }
}
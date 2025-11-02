package lotto;

import lotto.controller.LottoController;
import lotto.service.LottoService;
import lotto.service.LottoGenerator;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        // 1. 객체 생성 (Model과 View는 다른 객체를 통해 간접적으로 사용됨)
        LottoGenerator generator = new LottoGenerator();
        LottoService lottoService = new LottoService(generator);
        LottoController controller = new LottoController(lottoService);

        // 2. 게임 실행
        controller.run();
    }
}

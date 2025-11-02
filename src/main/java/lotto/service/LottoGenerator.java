package lotto.service;
import camp.nextstep.edu.missionutils.Randoms;
import lotto.model.Lotto;

import java.util.List;

public class LottoGenerator {
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;
    private static final int LOTTO_SIZE = 6;

    public Lotto generateLotto() {
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(
                MIN_NUMBER, MAX_NUMBER, LOTTO_SIZE
        );
        return new Lotto(numbers);
    }
}

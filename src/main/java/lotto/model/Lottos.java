package lotto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lottos {
    private final List<Lotto> lottos;

    public Lottos(List<Lotto> lottos) {
        this.lottos = Collections.unmodifiableList(new ArrayList<>(lottos)); // 불변 리스트로 저장: 외부에서 변경X
    }

    public int getNumberOfLottos() {
        return lottos.size();
    }

    public List<Lotto> getLottos(){
        return lottos;
    }

    public String getNumberOfLottosString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getNumberOfLottos()).append("개를 구매했습니다.\n");
        for (Lotto lotto : lottos) {
            sb.append(lotto.getLottoToString()).append("\n");
        }
        return sb.toString().trim();
    }
}
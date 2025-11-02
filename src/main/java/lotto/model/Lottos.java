package lotto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Lottos {
    private final List<Lotto> lottos;

    public Lottos(List<Lotto> lottos) {
        // 방어적 복사 후 불변 리스트로 저장
        this.lottos = Collections.unmodifiableList(new ArrayList<>(lottos));
    }

    public int getNumberOfLottos() {
        return lottos.size();
    }


    public List<Lotto> getLottos(){
        return lottos;
    }

    public List<String> getLottosDisplayStrings(){
        return lottos.stream()
                .map(Lotto::toString) // Lotto의 toString() 사용
                .collect(Collectors.toList());
    }
}
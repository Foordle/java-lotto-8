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

    /**
     * Service 계층에서 순회하여 당첨 결과를 계산하는 데 사용됩니다.
     */
    public List<Lotto> getLottos(){
        return lottos;
    }

    /**
     * View가 출력에 필요한 로또 번호 목록을 문자열 리스트로 가져가게 합니다.
     * (출력 포맷팅 로직은 OutputView에서 수행)
     */
    public List<String> getLottosDisplayStrings(){
        return lottos.stream()
                .map(Lotto::toString) // Lotto의 toString() 사용
                .collect(Collectors.toList());
    }
}
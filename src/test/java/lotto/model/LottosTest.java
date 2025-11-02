package lotto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class LottosTest {

    private List<Lotto> createTestLottoList(int count) {
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            lottos.add(new Lotto(List.of(1 + i, 2 + i, 3 + i, 4 + i, 5 + i, 6 + i)));
        }
        return lottos;
    }

    @DisplayName("생성 시 원본 리스트 변경으로부터 내부 상태가 보호되어야 한다.")
    @Test
    void constructor_shouldBeImmutable() {
        // given
        List<Lotto> mutableLottoList = createTestLottoList(1);
        Lottos lottos = new Lottos(mutableLottoList); // 생성자에서 방어적 복사

        // when
        // 외부의 원본 리스트에 객체를 추가
        mutableLottoList.add(new Lotto(List.of(10, 11, 12, 13, 14, 15)));

        // then
        // Lottos 객체의 크기는 여전히 1개여야 한다.
        assertThat(lottos.getNumberOfLottos()).isEqualTo(1);
    }

    @DisplayName("getNumberOfLottos()가 로또 개수를 정확히 반환해야 한다.")
    @Test
    void getNumberOfLottos_shouldReturnCorrectSize() {
        // given
        Lottos lottos3 = new Lottos(createTestLottoList(3));

        // when & then
        assertThat(lottos3.getNumberOfLottos()).isEqualTo(3);
    }

    @DisplayName("getLottosDisplayStrings()가 포맷팅된 문자열 리스트를 반환해야 한다.")
    @Test
    void getLottosDisplayStrings_shouldReturnFormattedList() {
        // given
        // Lotto.toString()은 [1, 2, 3, 4, 5, 6] 형태로 반환한다고 가정
        Lotto lotto1 = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto lotto2 = new Lotto(List.of(10, 11, 12, 13, 14, 15));

        List<Lotto> lottoList = List.of(lotto1, lotto2);
        Lottos lottos = new Lottos(lottoList);

        // expected
        List<String> expected = List.of("[1, 2, 3, 4, 5, 6]", "[10, 11, 12, 13, 14, 15]");

        // when
        List<String> actual = lottos.getLottosDisplayStrings(); // 수정된 메서드 이름 사용

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("getLottos()가 반환하는 리스트를 수정하려 하면 예외가 발생해야 한다.")
    @Test
    void getLottos_shouldReturnUnmodifiableList() {
        // given
        Lottos lottos = new Lottos(createTestLottoList(1));
        List<Lotto> returnedList = lottos.getLottos();

        // when & then
        // 반환된 리스트에 요소를 추가하려 하면 예외가 발생해야 한다.
        assertThatThrownBy(() -> returnedList.add(new Lotto(List.of(1, 2, 3, 4, 5, 7))))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
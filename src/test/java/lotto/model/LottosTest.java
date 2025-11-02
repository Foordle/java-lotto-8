package lotto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class LottosTest {

    // 테스트에 사용할 Lotto 객체 목록 생성
    private List<Lotto> createTestLottoList(int count) {
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // 편의상 유효한 번호를 사용하여 Lotto 객체 생성
            lottos.add(new Lotto(List.of(1 + i, 2 + i, 3 + i, 4 + i, 5 + i, 6 + i)));
        }
        return lottos;
    }

    // --- 1. 생성자 테스트 ---

    @DisplayName("Lottos 객체 생성 후 외부에서 원본 리스트를 변경해도 내부 상태는 유지되어야 한다.")
    @Test
    void constructor_shouldBeImmutable() {
        // given
        List<Integer> initialNumbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        List<Lotto> mutableLottoList = new ArrayList<>();
        mutableLottoList.add(new Lotto(initialNumbers));

        Lottos lottos = new Lottos(mutableLottoList);

        // when
        // 1. 외부의 원본 리스트에 객체를 추가
        mutableLottoList.add(new Lotto(List.of(10, 11, 12, 13, 14, 15)));

        // then
        // 2. Lottos 객체의 크기는 여전히 1개여야 한다 (불변 리스트로 복사되어야 함)
        assertThat(lottos.getNumberOfLottos()).isEqualTo(1);
    }

    @DisplayName("Lottos 객체가 반환하는 리스트를 수정하려 하면 예외가 발생해야 한다.")
    @Test
    void getLottos_shouldReturnUnmodifiableList() {
        // given
        List<Lotto> lottosList = createTestLottoList(2);
        Lottos lottos = new Lottos(lottosList);

        // when
        List<Lotto> returnedList = lottos.getLottos();

        // then
        // 반환된 리스트에 요소를 추가하려 하면 UnmodifiableList에 의해 예외가 발생해야 한다.
        assertThatThrownBy(() -> returnedList.add(new Lotto(List.of(1, 2, 3, 4, 5, 7))))
                .isInstanceOf(UnsupportedOperationException.class);
    }


    // --- 2. getNumberOfLottos() 테스트 ---

    @DisplayName("로또 개수를 정확히 반환해야 한다.")
    @Test
    void getNumberOfLottos_shouldReturnCorrectSize() {
        // given
        Lottos lottos3 = new Lottos(createTestLottoList(3));
        Lottos lottos5 = new Lottos(createTestLottoList(5));

        // when & then
        assertThat(lottos3.getNumberOfLottos()).isEqualTo(3);
        assertThat(lottos5.getNumberOfLottos()).isEqualTo(5);
    }


    // --- 3. getNumberOfLottosString() 테스트 ---

    @DisplayName("로또 구매 결과를 출력 형식에 맞게 문자열로 반환해야 한다.")
    @Test
    void getNumberOfLottosString_shouldReturnFormattedString() {
        // given
        Lotto lotto1 = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto lotto2 = new Lotto(List.of(10, 11, 12, 13, 14, 15));

        List<Lotto> lottoList = List.of(lotto1, lotto2);
        Lottos lottos = new Lottos(lottoList);

        // Lotto.toString()이 반환하는 값과 일치해야 합니다.
        String expected = "2개를 구매했습니다.\n[1, 2, 3, 4, 5, 6]\n[10, 11, 12, 13, 14, 15]";

        // when
        String actual = lottos.getNumberOfLottosString();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    // --- 4. getLottos(int index) 테스트 ---

    @DisplayName("getLottos는 전체 로또 목록을 반환한다.")
    @Test
    void getLottos_withIndex_shouldReturnFullList() {
        // given
        List<Lotto> originalList = createTestLottoList(3);
        Lottos lottos = new Lottos(originalList);

        // when
        List<Lotto> returnedList = lottos.getLottos();

        // then
        // 반환된 리스트의 크기가 전체 목록의 크기와 같아야 한다.
        assertThat(returnedList).hasSize(3);
        // 반환된 리스트는 원본 리스트와 동일한 객체를 참조하고 있어야 한다.
        assertThat(returnedList).isEqualTo(originalList);

    }
}
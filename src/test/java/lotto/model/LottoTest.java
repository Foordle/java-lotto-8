package lotto.model;

import org.junit.jupiter.api.DisplayName; // 말 그대로 consol에 보기 좋게 띄어줌
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class LottoTest {
    @DisplayName("로또 번호의 개수가 6개 초과이면 예외가 발생한다.")
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // --- countMatch(Lotto winningLotto) 테스트 (수정된 메서드) ---

    @DisplayName("당첨 로또 객체를 받아 일치 개수를 정확히 반환한다. (4개 일치)")
    @Test
    void countMatch_4개_일치() {
        // given
        Lotto myLotto = new Lotto(List.of(1, 10, 20, 30, 40, 45));
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 10, 20, 30)); // 1, 10, 20, 30 4개 일치

        // when
        int matchCount = myLotto.countMatch(winningLotto);

        // then
        assertThat(matchCount).isEqualTo(4);
    }

    @DisplayName("두 로또 번호가 전혀 일치하지 않으면 0을 반환한다.")
    @Test
    void countMatch_0개_일치() {
        // given
        Lotto myLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(7, 8, 9, 10, 11, 12));

        // when
        int matchCount = myLotto.countMatch(winningLotto);

        // then
        assertThat(matchCount).isEqualTo(0);
    }

    @DisplayName("toString() 메서드가 로또 번호 리스트의 문자열을 반환한다.")
    @Test
    void toString_test() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));

        // when
        String result = lotto.toString(); // getLottoToString() 대신 toString() 사용

        // then
        assertThat(result).isEqualTo("[1, 2, 3, 4, 5, 6]");
    }

    @DisplayName("getNumbers()가 불변 리스트를 반환하는지 확인한다.")
    @Test
    void getNumbers_shouldReturnUnmodifiableList() {
        // given
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> numbers = lotto.getNumbers();

        // when & then
        assertThatThrownBy(() -> numbers.add(7))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}

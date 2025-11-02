package lotto.model;

import org.junit.jupiter.api.DisplayName; // 말 그대로 consol에 보기 좋게 띄어줌
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;


class LottoTest {
    @DisplayName("로또 번호의 개수가 6개  초과이면 예외가 발생한다.")
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호의 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("로또 번호의 개수가 6개 미만이면 예외가 발생한다.")
    @Test
    void 로또_번호의_개수가_6개_미만이면_예외가_발생한다() {
        // [ERROR] 로또 번호는 6개여야 합니다.
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("로또 번호가 1~45 범위를 벗어나면 예외가 발생한다. (초과)")
    @Test
    void 로또_번호_범위_초과_예외_발생() {
        // [ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호가 1~45 범위를 벗어나면 예외가 발생한다. (미만)")
    @Test
    void 로또_번호_범위_미만_예외_발생() {
        // [ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.
        assertThatThrownBy(() -> new Lotto(List.of(0, 2, 3, 4, 5, 6)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유효한 로또 번호로 생성 성공 시 예외가 발생하지 않는다.")
    @Test
    void 로또_객체_정상_생성() {
        // given
        List<Integer> validNumbers = List.of(1, 20, 30, 40, 45, 10);
        // when & then
        // 예외가 발생하지 않는 것을 검증
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> new Lotto(validNumbers));
    }

    @DisplayName("특정 번호가 로또 번호에 포함되어 있는지 확인한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 30, 45})
    void contains_포함된_번호_확인(int number) {
        // given
        Lotto lotto = new Lotto(List.of(1, 10, 20, 30, 40, 45));
        // when & then
        assertThat(lotto.contains(number)).isTrue();
    }

    @DisplayName("특정 번호가 로또 번호에 포함되어 있지 않은지 확인한다.")
    @Test
    void contains_포함되지_않은_번호_확인() {
        // given
        Lotto lotto = new Lotto(List.of(1, 10, 20, 30, 40, 45));
        // when & then
        assertThat(lotto.contains(99)).isFalse();
    }


    @DisplayName("두 로또 번호의 일치 개수를 정확히 반환한다. (4개 일치)")
    @Test
    void lottoMatchCheck_4개_일치() {
        // given
        Lotto myLotto = new Lotto(List.of(1, 10, 20, 30, 40, 45));
        Lotto winningLotto = new Lotto(List.of(1, 2, 3, 10, 20, 30)); // 1, 10, 20, 30 4개 일치

        // when
        int matchCount = myLotto.lottoMatchCheck(winningLotto);
        // then
        assertThat(matchCount).isEqualTo(4);
    }

    @DisplayName("두 로또 번호가 완전히 일치하면 6을 반환한다.")
    @Test
    void lottoMatchCheck_6개_일치() {
        // given
        Lotto myLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(6, 5, 4, 3, 2, 1));

        // when
        int matchCount = myLotto.lottoMatchCheck(winningLotto);

        // then
        assertThat(matchCount).isEqualTo(6);
    }

    @DisplayName("두 로또 번호가 전혀 일치하지 않으면 0을 반환한다.")
    @Test
    void lottoMatchCheck_0개_일치() {
        // given
        Lotto myLotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        Lotto winningLotto = new Lotto(List.of(7, 8, 9, 10, 11, 12));

        // when
        int matchCount = myLotto.lottoMatchCheck(winningLotto);

        // then
        assertThat(matchCount).isEqualTo(0);
    }
}

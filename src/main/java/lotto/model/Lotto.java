package lotto.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lotto {
    private static final int LOTTO_SIZE = 6;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;

    private final List<Integer> lottoNumbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.lottoNumbers = sortNumbers(numbers);
    }

    // 유효성 검증 로직은 유지 및 정리

    private void validate(List<Integer> numbers) {
        if (numbers.size() != LOTTO_SIZE) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.\n");
        }
        if (!isNumbersInRange(numbers)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.\n");
        }
        if (hasDuplicateNumbers(numbers)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 중복될 수 없습니다.\n");
        }
    }

    private boolean isNumbersInRange(List<Integer> numbers) {
        // 기존의 isValidRange 메서드와 중복되므로 하나로 통일하고 명확하게 유지
        return numbers.stream()
                .allMatch(number -> number >= MIN_NUMBER && number <= MAX_NUMBER);
    }

    private boolean hasDuplicateNumbers(List<Integer> numbers) {
        Set<Integer> LottoNumbersSet = new HashSet<>(numbers);
        return  LottoNumbersSet.size() != numbers.size();
    }

    private List<Integer> sortNumbers(List<Integer> numbers) {
        // 내부 저장 시 정렬하여 불변성 확보
        List<Integer> sortedNumbers = new java.util.ArrayList<>(numbers);
        Collections.sort(sortedNumbers);
        return sortedNumbers;
    }

    // 비즈니스 로직 및 Getter
    public int countMatch(Lotto winningLotto) {
        return (int) lottoNumbers.stream()
                .filter(winningLotto::contains)
                .count();
    }


    public boolean contains(int number) {
        return lottoNumbers.contains(number);
    }

    // View를 위한 Getter
    public List<Integer> getNumbers() {
        return Collections.unmodifiableList(lottoNumbers);
    }


    @Override
    public String toString() {
        return lottoNumbers.toString();
    }
}
package lotto.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
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
        return numbers.stream()
                .allMatch(number -> number >= 1 && number <= 45);
    }
    private boolean hasDuplicateNumbers(List<Integer> numbers) {
        // Hashset을 이용해 size비교로 중복 체크
        Set<Integer> LottoNumbersSet = new HashSet<>(numbers);
        return  LottoNumbersSet.size() != numbers.size();
    }

    private List<Integer> sortNumbers(List<Integer> numbers) {
        // 복사+sort해서 Lottos로 넣어줄거임!
        List<Integer> sortedNumbers = new java.util.ArrayList<>(numbers);
        Collections.sort(sortedNumbers); // java.util.Collections 사용
        return sortedNumbers;
    }

    public boolean isContainsNumbers(int number) {
        return numbers.contains(number);
    }



}
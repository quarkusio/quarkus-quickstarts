package org.acme.jwt;

import java.util.List;

public class LottoNumbers {
    private List<Integer> numbers;

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return "LottoNumbers{" +
                "numbers=" + numbers +
                '}';
    }
}

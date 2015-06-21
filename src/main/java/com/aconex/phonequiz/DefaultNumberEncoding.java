package com.aconex.phonequiz;

import java.util.HashMap;
import java.util.Map;

public class DefaultNumberEncoding implements NumberEncoding {
    private final Map<Integer, char[]> encoding = new HashMap<>();
    private final Map<Character, Integer> reversedEncoding = new HashMap<>();

    public DefaultNumberEncoding() {
        addEncoding(2, 'A', 'B', 'C');
        addEncoding(3, 'D', 'E', 'F');
        addEncoding(4, 'G', 'H', 'I');
        addEncoding(5, 'J', 'K', 'L');
        addEncoding(6, 'M', 'N', 'O');
        addEncoding(7, 'P', 'Q', 'R', 'S');
        addEncoding(8, 'T', 'U', 'V');
        addEncoding(9, 'W', 'X', 'Y', 'Z');
    }

    private void addEncoding(int number, char... letters) {
        encoding.put(number, letters);
        for(char letter : letters) {
            reversedEncoding.put(letter, number);
        }
    }

    @Override
    public int getNumber(char letter) {
        return reversedEncoding.get(letter);
    }

}


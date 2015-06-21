package com.aconex.phonequiz;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dictionary {
    private Stream<String> words;
    private PhoneWordNode root;

    public Dictionary(Stream<String> words, NumberEncoding encoding) {
        this.words = words;
        this.root = new PhoneWordNode(encoding);
        buildDictionary();
    }

    public List<String> findPhoneWords(String number) {
        return root.findWords(cleanNumber(number), 0, root).stream()
                .map(phoneWords -> String.join("-", phoneWords))
                .collect(Collectors.toList());
    }

    private void buildDictionary() {
        words.map(this::cleanWord).forEach(word -> root.addWord(word, 0));
    }

    protected String cleanWord(String line) {
        return line.toUpperCase().replaceAll("[^A-Z]", "");
    }

    protected String cleanNumber(String number) {
        return number.replaceAll("[^0-9]", "");
    }
}

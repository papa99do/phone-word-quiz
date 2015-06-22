package com.aconex.phonequiz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dictionary {
    private Stream<String> words;
    private PhoneWordNode root;
    private Set<String> wordSet;

    public Dictionary(Stream<String> words, NumberEncoding encoding) {
        this.words = words;
        this.root = new PhoneWordNode(encoding);
        this.wordSet = new HashSet<>();
        buildDictionary();
    }

    public List<String> findPhoneWords(String number) {
        String realNumber = cleanNumber(number);
        List<List<String>> candidates = root.findWords(realNumber, 0, root);
        if (candidates.isEmpty()) {
            candidates = root.findWords(realNumber, 0, root, true);
        }

        return candidates.stream()
                .map(phoneWords -> String.join("-", phoneWords))
                .collect(Collectors.toList());
    }

    public int getSize() {
        return wordSet.size();
    }

    private void buildDictionary() {
        words.map(this::cleanWord)
                .filter(word -> !word.isEmpty() && !wordSet.contains(word))
                .forEach(word -> {
                    root.addWord(word, 0);
                    wordSet.add(word);
                });
    }

    protected String cleanWord(String line) {
        return line.toUpperCase().replaceAll("[^A-Z]", "");
    }

    protected String cleanNumber(String number) {
        return number.replaceAll("[^0-9]", "");
    }
}

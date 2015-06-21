package com.aconex.phonequiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneWordNode {
    private List<String> words = new ArrayList<>();
    private PhoneWordNode[] children = new PhoneWordNode[10];
    private NumberEncoding encoding;

    public PhoneWordNode(NumberEncoding encoding) {
        this.encoding = encoding;
    }

    public void addWord(String word, int level) {
        if (word.length() == level) {
            words.add(word);
            return;
        }

        int number = encoding.getNumber(word.charAt(level));

        if (children[number] == null) {
            children[number] = new PhoneWordNode(encoding);
        }

        children[number].addWord(word, level + 1);
    }

    public List<List<String>> findWords(String number, int level, PhoneWordNode rootNode) {
        List<List<String>> result = new ArrayList<>();

        if (number.length() == level) {
            result.addAll(words.stream().map(Arrays::asList).collect(Collectors.toList()));
        } else {
            int digit = number.charAt(level) - '0';
            if (children[digit] != null) {
                result.addAll(children[digit].findWords(number, level + 1, rootNode));
            }

            if (!words.isEmpty()) {
                result.addAll(crossJoinConcat(words.stream().map(Arrays::asList).collect(Collectors.toList()),
                        rootNode.findWords(number.substring(level), 0, rootNode)));

                // allow skip one digit
                result.addAll(crossJoinConcat(words.stream().map(word -> Arrays.asList(word, "" + digit))
                                .collect(Collectors.toList()),
                        rootNode.findWords(number.substring(level + 1), 0, rootNode)));
            }
        }

        return result;
    }

    private List<List<String>> crossJoinConcat(List<List<String>> heads, List<List<String>> tails) {
        return heads.stream().flatMap(head -> tails.stream().map(tail -> {
            List<String> result = new ArrayList<>();
            result.addAll(head);
            result.addAll(tail);
            return result;
        })).collect(Collectors.toList());
    }

    protected List<String> getWords() {
        return words;
    }

    protected PhoneWordNode[] getChildren() {
        return children;
    }
}

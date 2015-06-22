package com.aconex.phonequiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p><code>PhoneWordNode</code> represents a trie-like tree structure to store the encoded words. Each node has up to 10
 * children mapped to number 0-9, and a list of words mapped all the way down to this level. The diagram below
 * illustrates the design intention better.</p>
 *
 * <p>Say we have three words, and mapped to number based on encoding as following:<br>
 * ABD (223) <br>
 * AB  (22)  <br>
 * E   (3)   <br>
 * </p>
 * The generated tree will be like
 * <pre>
 * root -> 2 -> 2 [AB] -> 3 [ABD]
 *      -> 3 [E]
 * </pre>
 *
 * <p>When searching a match for a number, say 223, it start from the root node and traverse the tree according to
 * the individual digit. If a match found, and all digits of the number has been consumed, the words in the current
 * node will be added to the final result. If it's in the middle of a number, it will try to find a new word from that
 * position and join the result with the previously found word. The feature to skip a digit is supported by simply allow
 * skipping a digit when doing this recursive process.
 * </p>
 */
public class PhoneWordNode {
    private List<String> words = new ArrayList<>();
    private PhoneWordNode[] children = new PhoneWordNode[10];
    private NumberEncoding encoding;

    public PhoneWordNode(NumberEncoding encoding) {
        this.encoding = encoding;
    }

    public void addWord(String word) {
        addWord(word, 0);
    }

    public List<List<String>> findWordsNoSkip(String number) {
        return findWords(number, 0, this, false);
    }

    public List<List<String>> findWordsAllowSkip(String number) {
        return findWords(number, 0, this, true);
    }

    private void addWord(String word, int level) {
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

    private List<List<String>> findWords(String number, int level, PhoneWordNode rootNode, boolean skipOneDigit) {

        if (number.length() == level) {
            return words.stream().map(Arrays::asList).collect(Collectors.toList());
        }

        List<List<String>> result = new ArrayList<>();

        int digit = number.charAt(level) - '0';
        if (children[digit] != null) {
            result.addAll(children[digit].findWords(number, level + 1, rootNode, skipOneDigit));
        }

        if (!words.isEmpty()) {
            int nextWordStartIndex = skipOneDigit ? level + 1 : level;

            Function<String, List<String>> getMatchedWordList = skipOneDigit
                    ? word -> Arrays.asList(word, "" + digit)
                    : word -> Arrays.asList(word);

            result.addAll(crossJoinConcat(words.stream().map(getMatchedWordList).collect(Collectors.toList()),
                    rootNode.findWords(number.substring(nextWordStartIndex), 0, rootNode, skipOneDigit)));
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

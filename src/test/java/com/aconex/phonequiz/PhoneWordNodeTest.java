package com.aconex.phonequiz;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PhoneWordNodeTest {

    private PhoneWordNode root;

    @Before
    public void setup() {
        root = new PhoneWordNode(new DummyNumberEncoding());
    }

    @Test
    public void shouldBuildPhoneWordNodeCorrectly() {
        root.addWord("AA");
        root.addWord("ABA");

        assertChildrenAreNullExcept(root, Arrays.asList(2));

        PhoneWordNode nodeA = root.getChildren()[2];
        assertTrue(nodeA.getWords().isEmpty());
        assertChildrenAreNullExcept(nodeA, Arrays.asList(2, 3));

        PhoneWordNode nodeAA = nodeA.getChildren()[2];
        assertThat(nodeAA.getWords(), CoreMatchers.hasItem("AA"));
        assertChildrenAreNullExcept(nodeAA, Collections.emptyList());

        PhoneWordNode nodeAB = nodeA.getChildren()[3];
        assertTrue(nodeAB.getWords().isEmpty());
        assertChildrenAreNullExcept(nodeAB, Arrays.asList(2));

        PhoneWordNode nodeABA = nodeAB.getChildren()[2];
        assertThat(nodeABA.getWords(), CoreMatchers.hasItem("ABA"));
        assertChildrenAreNullExcept(nodeABA, Collections.emptyList());

    }

    @Test
    public void shouldFindSingleWord() {
        root.addWord("AA");
        root.addWord("ABA");

        List<List<String>> result = root.findWordsNoSkip("22");

        assertThat(result.size(), is(1));
        assertThat(result.get(0), hasItem("AA"));
    }

    @Test
    public void shouldFindTwoWords() {
        root.addWord("DDD"); // both mapped to 4
        root.addWord("EEE");

        List<List<String>> result = root.findWordsNoSkip("444");
        assertThat(result.size(), is(2));
        assertThat(result.get(0), hasItem("DDD"));
        assertThat(result.get(1), hasItem("EEE"));
    }

    @Test
    public void shouldFindTwoWordsPhrase() {
        root.addWord("AA");
        root.addWord("BB");

        List<List<String>> result = root.findWordsNoSkip("2233");
        assertThat(result.size(), is(1));
        assertThat(result.get(0), hasItems("AA", "BB"));

    }

    @Test
    public void shouldFindThreeWordsPhrase() {
        root.addWord("AA");
        root.addWord("BB");
        root.addWord("CC");

        List<List<String>> result = root.findWordsNoSkip("223344");
        assertThat(result.size(), is(1));
        assertThat(result.get(0), hasItems("AA", "BB", "CC"));
    }

    @Test
    public void shouldFindTwoPhrases() {
        root.addWord("AA");
        root.addWord("CC");
        root.addWord("DD");

        List<List<String>> result = root.findWordsNoSkip("2244");
        assertThat(result.size(), is(2));
        assertThat(result.get(0), hasItems("AA", "CC"));
        assertThat(result.get(1), hasItems("AA", "DD"));
    }

    @Test
    public void shouldSupportSkippingOneDigit() {
        root.addWord("AA");
        root.addWord("BB");

        assertThat(root.findWordsNoSkip("22433").size(), is(0));

        List<List<String>> result = root.findWordsAllowSkip("22433");
        assertThat(result.size(), is(1));
        assertThat(result.get(0), hasItems("AA", "4", "BB"));
    }

    @Test
    public void shouldNotGetErrorWhenMatchingEmptyNumber() {
        root.addWord("AA");
        root.addWord("BB");

        assertThat(root.findWordsNoSkip("").size(), is(0));
    }


    private void assertChildrenAreNullExcept(PhoneWordNode node, List<Integer> notNullChildrenIndexes) {

        for (int i = 0; i < 10; i++) {
            if (notNullChildrenIndexes.contains(i)) {
                assertThat(String.format("Expect child[%d] to be not null", i),
                        node.getChildren()[i], IsNull.notNullValue());
            } else {
                assertThat(String.format("Expect child[%d] to be null", i),
                        node.getChildren()[i], IsNull.nullValue());
            }
        }
    }

}
package com.aconex.phonequiz;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DictionaryTest {

    private NumberEncoding dummyEncoding = new DummyNumberEncoding();

    private Stream<String> words =Arrays.stream(new String[] {"aa", "aaa", "aaaaa'a"});

    @Test
    public void shouldCleanNumber() {
        Dictionary dictionary = new Dictionary(Stream.empty(), dummyEncoding);

        assertThat(dictionary.cleanNumber("222.222"), is("222222"));
    }

    @Test
    public void shouldCleanWord() {
        Dictionary dictionary = new Dictionary(Stream.empty(), dummyEncoding);

        assertThat(dictionary.cleanWord("what's up"), is("WHATSUP"));
    }

    @Test
    public void shouldFindFindPhoneWords() {
        Dictionary dictionary = new Dictionary(words, dummyEncoding);

        List<String> results = dictionary.findPhoneWords("222.222");

        assertThat(results, hasItems("AAAAAA", "AAA-AAA", "AA-2-AAA", "AAA-2-AA"));
    }
}
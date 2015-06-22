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
    public void shouldFindPhoneWords() {
        Dictionary dictionary = new Dictionary(words, dummyEncoding);

        List<String> results = dictionary.findPhoneWords("222.222");

        assertThat(results, hasItems("AAAAAA", "AAA-AAA"));
    }

    @Test
    public void shouldBeAbleToDealWithEmptyLineInDictionaryFile() {
        Stream<String> words =Arrays.stream(new String[] {"aa", "", "#$%^&*"});
        Dictionary dictionary = new Dictionary(words, dummyEncoding);

        List<String> results = dictionary.findPhoneWords("22");
        assertThat(results, hasItems("AA"));
    }

    @Test
    public void shouldBeAbleToFindWordsWithSkippedDigit() {
        Dictionary dictionary = new Dictionary(words, dummyEncoding);
        List<String> results = dictionary.findPhoneWords("222322");
        assertThat(results, hasItems("AAA-3-AA"));
    }

    @Test
    public void shouldReturnEmptyListIfNoMatchFound() {
        Dictionary dictionary = new Dictionary(words, dummyEncoding);
        List<String> results = dictionary.findPhoneWords("3333");
        assertThat(results.size(), is(0));
    }
}
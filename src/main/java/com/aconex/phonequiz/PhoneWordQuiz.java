package com.aconex.phonequiz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class PhoneWordQuiz {

    public static void main(String[] args) throws Exception {
        OptionParser parser = null;
        try {
            parser = new OptionParser(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        Dictionary dictionary = new Dictionary(lines(parser.getDictionaryFilePath()), new DefaultNumberEncoding());
        System.out.println("Dictionary has been initialized");

        Stream<String> numbers;
        if (parser.getPhoneNumberFilePath() != null) {
            numbers = lines(parser.getPhoneNumberFilePath());
            System.out.println("Reading numbers from file: " + parser.getPhoneNumberFilePath());
        } else {
            System.out.print("Please input a number > ");
            String number = new BufferedReader(new InputStreamReader(System.in)).readLine();
            numbers = Arrays.stream(new String[] {number});
        }

        numbers.forEach(number -> {
            List<String> candidates = dictionary.findPhoneWords(number);
            if (!candidates.isEmpty()) {
                System.out.println(String.format("Possible candidates for number %s: ", number));
                candidates.stream().forEach(System.out::println);
            }
        });
    }
}

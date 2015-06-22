package com.aconex.phonequiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class PhoneWordQuiz {

    public static void main(String[] args) throws Exception {
        PhoneWordQuizOptions opts = parseOptions(args);
        Dictionary dictionary = loadDictionary(opts);
        Stream<String> numbers = readNumbers(opts);

        numbers.forEach(number -> printOutPhoneWords(number, dictionary.findPhoneWords(number)));
    }

    private static void printOutPhoneWords(String number, List<String> candidates) {
        if (!candidates.isEmpty()) {
            System.out.println(String.format("Possible candidates for number %s: ", number));
            candidates.stream().forEach(System.out::println);
        }
    }

    private static Dictionary loadDictionary(PhoneWordQuizOptions opts) throws IOException, URISyntaxException {
        Dictionary dictionary = new Dictionary(lines(opts.getDictionaryFilePath()), new DefaultNumberEncoding());
        System.out.println(String.format("Dictionary has been initialized, %d words loaded", dictionary.getSize()));
        return dictionary;
    }

    private static Stream<String> readNumbers(PhoneWordQuizOptions opts) throws IOException {
        Stream<String> numbers;
        if (opts.getPhoneNumberFilePath() != null) {
            numbers = lines(opts.getPhoneNumberFilePath());
            System.out.println("Reading numbers from file: " + opts.getPhoneNumberFilePath());
        } else {
            System.out.print("Please input a number > ");
            String number = new BufferedReader(new InputStreamReader(System.in)).readLine();
            numbers = Arrays.stream(new String[] {number});
        }
        return numbers;
    }

    private static PhoneWordQuizOptions parseOptions(String[] args) {
        try {
            return new PhoneWordQuizOptions(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            PhoneWordQuizOptions.printUsage();
            System.exit(1);
            return null;
        }
    }
}

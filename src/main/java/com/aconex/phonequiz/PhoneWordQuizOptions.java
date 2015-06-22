package com.aconex.phonequiz;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PhoneWordQuizOptions {

    private static final String DEFAULT_DICTIONARY_PATH = "/words.txt";
    private static final String USAGE =
            "Usage: java -jar aconex-1800-quiz.jar [OPTIONS]\n\n"
            + "1800 phone number quiz\n\n"
            + "Options:\n"
            + "  -d <dictionary file>\t\t\tIf not provided, will use the default one\n"
            + "  -f <phone number file>\t\tIf not provided, will read input from stdin\n";

    private Path phoneNumberFilePath;
    private Path dictionaryFilePath;

    public PhoneWordQuizOptions(String[] args) {
        int index = 0;
        while (index < args.length) {
            switch(args[index]) {
                case "-h": printUsage(); System.exit(0); break;
                case "-f": this.phoneNumberFilePath = getPath(args, index + 1); index++; break;
                case "-d": this.dictionaryFilePath = getPath(args, index + 1); index++; break;
                default: break;
            }
            index++;
        }
    }

    private Path getPath(String[] args, int index) {
        if (index >= args.length) {
            throw new IllegalArgumentException();
        }

        Path path = new File(args[index]).toPath();
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exists: " + args[index]);
        }

        return path;
    }

    public Path getPhoneNumberFilePath() {
        return phoneNumberFilePath;
    }

    public Path getDictionaryFilePath() throws URISyntaxException, IOException {
        return dictionaryFilePath != null ? dictionaryFilePath : getDefaultDictionaryPath();
    }

    private Path getDefaultDictionaryPath() throws URISyntaxException, IOException {
        URI uri = PhoneWordQuizOptions.class.getResource(DEFAULT_DICTIONARY_PATH).toURI();
        String[] splitUri = uri.toString().split("!");
        if (splitUri.length > 1) {
            FileSystem fs = FileSystems.newFileSystem(URI.create(splitUri[0]), new HashMap<String, String>());
            return fs.getPath(splitUri[1]);
        } else {
            return Paths.get(uri);
        }

    }

    public static void printUsage() {
        System.out.println(USAGE);
    }

}

package com.aconex.phonequiz;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OptionParser {

    private static final String DEFAULT_DICTIONARY_PATH = "words.txt";

    private Path phoneNumberFilePath;
    private Path dictionaryFilePath;

    public OptionParser(String[] args) {
        int index = 0;
        while (index < args.length) {
            switch(args[index]) {
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

    public Path getDictionaryFilePath() throws URISyntaxException {
        return dictionaryFilePath != null ? dictionaryFilePath :
                Paths.get(ClassLoader.getSystemResource("words.txt").toURI());
    }

}

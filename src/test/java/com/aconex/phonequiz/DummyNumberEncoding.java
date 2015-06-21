package com.aconex.phonequiz;

public class DummyNumberEncoding implements NumberEncoding {
    @Override
    public int getNumber(char letter) {
        switch (letter) {
            case 'A' : return 2;
            case 'B' : return 3;
            default: return 4;
        }
    }
}

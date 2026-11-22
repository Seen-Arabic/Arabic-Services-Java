package io.github.seen_arabic.arabic_services;

import java.util.Objects;

final class LettersService {

    private LettersService() {
    }

    static String wordToLetters(String word) {
        Objects.requireNonNull(word, Data.TEXT_NULL_MESSAGE);
        String trimmedWord = word.trim();
        int[] codePoints = trimmedWord.codePoints().toArray();
        StringBuilder newWord = new StringBuilder();

        for (int i = 0; i < codePoints.length; i++) {
            String letter = new String(codePoints, i, 1);

            if (Data.PRONOUNCED_LETTERS.containsKey(letter)) {
                newWord.append(Data.PRONOUNCED_LETTERS.get(letter));
                if (i != codePoints.length - 1) {
                    newWord.append(' ');
                }
            } else if (Character.isWhitespace(codePoints[i])) {
                newWord.append(' ');
                while (i + 1 < codePoints.length && Character.isWhitespace(codePoints[i + 1])) {
                    i++;
                }
            } else {
                newWord.append(letter);
            }
        }

        return newWord.toString().trim();
    }
}

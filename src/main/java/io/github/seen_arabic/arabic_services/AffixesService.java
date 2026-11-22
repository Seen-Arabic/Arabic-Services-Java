package io.github.seen_arabic.arabic_services;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class AffixesService {

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{M}\\p{N}]+");
    private static final Pattern ARABIC_LETTER_PATTERN = Pattern.compile("[\\p{L}&&\\p{IsArabic}]");

    private AffixesService() {
    }

    static String removeArabicAffixes(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        String trimmed = text.trim();
        Matcher matcher = WORD_PATTERN.matcher(trimmed);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String word = matcher.group();
            String stripped = removeArabicAffixesFromWord(word);

            Matcher arabicLetterMatcher = ARABIC_LETTER_PATTERN.matcher(stripped);
            int arabicLetterCount = 0;
            while (arabicLetterMatcher.find()) {
                arabicLetterCount++;
                if (arabicLetterCount >= 3) {
                    break;
                }
            }

            String replacement = (arabicLetterCount >= 3) ? stripped : word;
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    static String removeArabicAffixesFromWord(String word) {
        word = word.trim();
        if (word.isEmpty()) {
            return word;
        }

        if (word.length() >= 2 && Data.ARABIC_PREFIXES.contains(word.substring(0, 2))) {
            word = word.substring(2);
        } else if (word.length() >= 1 && Data.ARABIC_PREFIXES.contains(word.substring(0, 1))) {
            word = word.substring(1);
        }

        if (word.length() >= 2 && Data.ARABIC_SUFFIXES.contains(word.substring(word.length() - 2))) {
            word = word.substring(0, word.length() - 2);
        } else if (word.length() >= 1 && Data.ARABIC_SUFFIXES.contains(word.substring(word.length() - 1))) {
            word = word.substring(0, word.length() - 1);
        }

        return word;
    }
}

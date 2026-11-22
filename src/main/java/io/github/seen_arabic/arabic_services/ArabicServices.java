package io.github.seen_arabic.arabic_services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArabicServices {

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{M}\\p{N}]+");
    private static final Pattern ARABIC_LETTER_PATTERN = Pattern.compile("[\\p{L}&&\\p{IsArabic}]");
    private static final Pattern WHITESPACE_TOKEN_PATTERN = Pattern.compile("(?U)(\\s+)");

    private static final String ALL_TASHKEEL_CHARS = String.join("", Data.TASHKEEL);

    private ArabicServices() {
    }

    /**
     * Remove all tashkeel from text.
     * 
     * For example
     * text: "الخَيْلُ وَاللّيْلُ وَالبَيْداءُ تَعرِفُني"
     * to
     * resulting string: "الخيل والليل والبيداء تعرفني"
     *
     * @param text The input string that contains tashkeel
     * @return The resulting string (text without tashkeel)
     */
    public static String removeTashkeel(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        String cleanText = text.trim();
        StringBuilder sb = new StringBuilder(cleanText.length());
        for (int i = 0; i < cleanText.length(); i++) {
            char c = cleanText.charAt(i);
            if (c == 'ٱ') {
                sb.append('ا');
            } else if (ALL_TASHKEEL_CHARS.indexOf(c) < 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Remove all tatweel from text.
     *
     * For example
     * text: "جميـــل"
     * to
     * resulting string: "جميل"
     *
     * @param text The input string that contains tatweel
     * @return The resulting string (text without tatweel)
     */
    public static String removeTatweel(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        return text.trim().replace("\u0640", "");
    }

    /**
     * Remove all dots and tashkeel from text with default options.
     *
     * @param text The input string that contains tashkeel and dots
     * @return The resulting string in Old Arabic script
     */
    public static String toOldArabic(String text) {
        return toOldArabic(text, OldArabicOptions.defaults());
    }

    /**
     * Remove all dots and tashkeel from text with custom {@link OldArabicOptions}.
     *
     * @param text    The input string that contains tashkeel and dots
     * @param options Custom conversion options (if null, default options are used)
     * @return The resulting string in Old Arabic script
     */
    public static String toOldArabic(String text, OldArabicOptions options) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        if (options == null) {
            options = OldArabicOptions.defaults();
        }

        String sentence = removeTashkeel(text);
        StringBuilder newSentence = new StringBuilder(sentence.length());

        for (int i = 0; i < sentence.length(); i++) {
            char currentChar = sentence.charAt(i);
            String currentStr = String.valueOf(currentChar);

            if (!Data.LETTERS_DICT.containsKey(currentStr)) {
                newSentence.append(currentChar);
            } else if (options.isReplaceMidNoonWithBah()
                    && currentChar == 'ن'
                    && i + 1 < sentence.length()
                    && (Data.LETTERS_DICT.containsKey(String.valueOf(sentence.charAt(i + 1)))
                            || sentence.charAt(i + 1) == 'ـ')) {
                newSentence.append(Data.LETTERS_DICT.get("ب"));
            } else if (options.isReplaceMidYahWithBah()
                    && currentChar == 'ي'
                    && i + 1 < sentence.length()
                    && (Data.LETTERS_DICT.containsKey(String.valueOf(sentence.charAt(i + 1)))
                            || sentence.charAt(i + 1) == 'ـ')) {
                newSentence.append(Data.LETTERS_DICT.get("ب"));
            } else {
                newSentence.append(Data.LETTERS_DICT.get(currentStr));
            }
        }

        return newSentence.toString();
    }

    /**
     * Remove all dots and tashkeel from text.
     *
     * @deprecated Use {@link #toOldArabic(String)} instead.
     *
     * @param text The input string that contains tashkeel and dots
     * @return The resulting string in Old Arabic script
     */
    @Deprecated
    public static String textToOldArabic(String text) {
        return toOldArabic(text);
    }

    /**
     * Tashfeer text with default obfuscation level (1).
     *
     * @param text The input string
     * @return The resulting obfuscated string
     */
    public static String tashfeer(String text) {
        return tashfeer(text, 1);
    }

    /**
     * Tashfeer text with a specific obfuscation level.
     *
     * @param text            The input string
     * @param levelOfTashfeer The obfuscation level (must be non-negative)
     * @return The resulting obfuscated string
     */
    public static String tashfeer(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        validateTashfeerLevel(levelOfTashfeer);
        return transformSentenceTokens(text, word -> tashfeerHandler(word, levelOfTashfeer));
    }

    /**
     * Converts words in text to pronounced letter names.
     *
     * @param word The input string
     * @return Pronounced letters separated by spaces
     */
    public static String wordToLetters(String word) {
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

    /**
     * Removes listed affixes from each word in text, retaining the word if
     * the stripped result has fewer than 3 Arabic-script letters.
     *
     * @param text The input text
     * @return Text with affixes removed where eligible
     */
    public static String removeArabicAffixes(String text) {
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

    /**
     * Legacy internal affix stripping on a single word without the 3-letter guard.
     * Used by {@link #checkIfBannedWord(String)} and {@link #removeArabicAffixes(String)}.
     */
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

    /**
     * Performs tashfeer on banned words with default level (2).
     *
     * @param text The input text
     * @return Text with banned words obfuscated
     */
    public static String tashfeerBannedWords(String text) {
        return tashfeerBannedWords(text, 2);
    }

    /**
     * Performs tashfeer on banned words with a custom level.
     *
     * @param text            The input text
     * @param levelOfTashfeer Obfuscation level (must be non-negative)
     * @return Text with banned words obfuscated
     */
    public static String tashfeerBannedWords(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        validateTashfeerLevel(levelOfTashfeer);
        return transformSentenceTokens(text, word -> checkIfBannedWord(word) ? tashfeerHandler(word, levelOfTashfeer) : word);
    }

    /**
     * Converts words in text to Old Arabic script and applies tashfeer to banned words with default level (2).
     *
     * @param text The input text
     * @return Processed text
     */
    public static String toOldArabicAndTashfeerBannedWords(String text) {
        return toOldArabicAndTashfeerBannedWords(text, 2);
    }

    /**
     * Converts words in text to Old Arabic script and applies tashfeer to banned words with custom level.
     *
     * @param text            The input text
     * @param levelOfTashfeer Obfuscation level (must be non-negative)
     * @return Processed text
     */
    public static String toOldArabicAndTashfeerBannedWords(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        validateTashfeerLevel(levelOfTashfeer);
        return transformSentenceTokens(text, word -> checkIfBannedWord(word) ? tashfeerHandler(word, levelOfTashfeer) : toOldArabic(word));
    }

    /**
     * Calculates Levenshtein similarity score between two strings (0.0 to 1.0).
     *
     * @param s1 First string
     * @param s2 Second string
     * @return Similarity score between 0.0 and 1.0
     */
    public static double similarityScore(String s1, String s2) {
        Objects.requireNonNull(s1, "s1 must be not null");
        Objects.requireNonNull(s2, "s2 must be not null");

        s1 = s1.toLowerCase(Locale.ROOT);
        s2 = s2.toLowerCase(Locale.ROOT);

        String longer = s1;
        String shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }

        return (double) (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * Sets character at specified index in a string (matching JavaScript setCharAt behavior).
     *
     * @param str   Input string
     * @param index Target index
     * @param chr   Replacement character
     * @return Modified string
     */
    public static String setCharAt(String str, int index, String chr) {
        Objects.requireNonNull(str, "str must be not null");
        Objects.requireNonNull(chr, "chr must be not null");
        if (index > str.length() - 1) {
            return str;
        }
        int start = Math.max(0, index);
        int end = Math.max(0, index + 1);
        return str.substring(0, start) + chr + str.substring(end);
    }

    private static void validateTashfeerLevel(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Level of tashfeer must be a non-negative integer");
        }
    }

    private interface WordTransformer {
        String transform(String word);
    }

    private static String transformSentenceTokens(String sentence, WordTransformer transformer) {
        String trimmed = sentence.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        Matcher matcher = WHITESPACE_TOKEN_PATTERN.matcher(trimmed);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            String word = trimmed.substring(lastEnd, matcher.start());
            if (!word.isEmpty()) {
                result.append(transformer.transform(word));
            }
            result.append(matcher.group(1));
            lastEnd = matcher.end();
        }

        if (lastEnd < trimmed.length()) {
            String remaining = trimmed.substring(lastEnd);
            result.append(transformer.transform(remaining));
        }

        return result.toString();
    }

    private static String tashfeerHandler(String word, int level) {
        int[] codePoints = word.codePoints().toArray();
        int wordLength = codePoints.length;
        int n = calculateEncryptionLevel(level, wordLength);
        Set<Integer> randomIndexes = getRandomIndexes(n, wordLength);
        return tashfeerWord(codePoints, randomIndexes);
    }

    private static int calculateEncryptionLevel(int level, int wordLength) {
        if (wordLength <= 0) {
            return 0;
        }
        long longWordLength = wordLength;
        if (wordLength <= 4) {
            return (int) Math.min(1L + level, longWordLength);
        } else if (wordLength < 8) {
            return (int) Math.min(2L + level, longWordLength);
        } else {
            return (int) Math.min(3L + level, longWordLength);
        }
    }

    private static Set<Integer> getRandomIndexes(int numOfIndexesToNeeded, int wordLength) {
        if (wordLength <= 0 || numOfIndexesToNeeded <= 0) {
            return Collections.emptySet();
        }

        int count = Math.min(numOfIndexesToNeeded, wordLength);
        Set<Integer> randomIndexes = new HashSet<>();
        Random random = new Random();

        while (randomIndexes.size() < count) {
            randomIndexes.add(random.nextInt(wordLength));
        }

        return randomIndexes;
    }

    private static String tashfeerWord(int[] codePoints, Set<Integer> randomIndexes) {
        StringBuilder outputWord = new StringBuilder();

        for (int i = 0; i < codePoints.length; i++) {
            int cp = codePoints[i];
            boolean isChar = (cp <= 0xFFFF);
            char character = isChar ? (char) cp : 0;

            if (isChar && Data.STANDARD_LETTERS.contains(character) && randomIndexes.contains(i)) {
                String replacement = tashfeerCharacter(character);

                char prevChar = 0;
                if (i > 0 && codePoints[i - 1] <= 0xFFFF) {
                    prevChar = (char) codePoints[i - 1];
                }

                if (Data.STANDARD_LETTERS.contains(prevChar) && !Data.ALONE_LETTERS.contains(prevChar)) {
                    outputWord.append('ـ');
                }

                outputWord.append(replacement);
            } else {
                outputWord.appendCodePoint(cp);
            }
        }

        return outputWord.toString();
    }

    private static String tashfeerCharacter(char character) {
        if (Data.ALEF.contains(character)) {
            character = 'ا';
        }
        if (Data.WAW.contains(character)) {
            character = 'و';
        }
        if (Data.YAA.contains(character)) {
            character = 'ي';
        }

        List<String> replacementList = Data.LETTERS_TASHFEER_REPLACEMENT_DICT.get(character);
        if (replacementList == null || replacementList.isEmpty()) {
            return String.valueOf(character);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(replacementList.size());
        return replacementList.get(randomIndex);
    }

    private static boolean checkIfBannedWord(String string) {
        for (String bannedWord : Data.BANNED_WORDS) {
            if (bannedWord.equals(string)) {
                return true;
            }
        }
        double stdRatio = 70.0;
        return bannedSimilarityRatio(removeArabicAffixesFromWord(string)) >= stdRatio;
    }

    private static double bannedSimilarityRatio(String string) {
        double maximumSimilarity = -1.0;
        for (String bannedWord : Data.BANNED_WORDS) {
            double calculatedSimilarity = similarityScore(string, bannedWord);
            if (calculatedSimilarity > maximumSimilarity) {
                maximumSimilarity = calculatedSimilarity;
            }
        }
        return maximumSimilarity * 100.0;
    }

    private static int editDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }
}

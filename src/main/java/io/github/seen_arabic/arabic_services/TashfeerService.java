package io.github.seen_arabic.arabic_services;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class TashfeerService {

    private static final Pattern WHITESPACE_TOKEN_PATTERN = Pattern.compile("(?U)(\\s+)");

    private TashfeerService() {
    }

    static void validateTashfeerLevel(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Level of tashfeer must be a non-negative integer");
        }
    }

    static String tashfeer(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        validateTashfeerLevel(levelOfTashfeer);
        return transformSentenceTokens(text, word -> tashfeerHandler(word, levelOfTashfeer));
    }

    static String tashfeerBannedWords(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        validateTashfeerLevel(levelOfTashfeer);
        return transformSentenceTokens(text, word -> checkIfBannedWord(word) ? tashfeerHandler(word, levelOfTashfeer) : word);
    }

    static String toOldArabicAndTashfeerBannedWords(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        validateTashfeerLevel(levelOfTashfeer);
        return transformSentenceTokens(text, word -> checkIfBannedWord(word) ? tashfeerHandler(word, levelOfTashfeer) : OldArabicService.toOldArabic(word, OldArabicOptions.defaults()));
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
        return bannedSimilarityRatio(AffixesService.removeArabicAffixesFromWord(string)) >= stdRatio;
    }

    private static double bannedSimilarityRatio(String string) {
        double maximumSimilarity = -1.0;
        for (String bannedWord : Data.BANNED_WORDS) {
            double calculatedSimilarity = StringUtils.similarityScore(string, bannedWord);
            if (calculatedSimilarity > maximumSimilarity) {
                maximumSimilarity = calculatedSimilarity;
            }
        }
        return maximumSimilarity * 100.0;
    }
}

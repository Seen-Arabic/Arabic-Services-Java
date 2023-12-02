package io.github.seen_arabic.arabic_services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class ArabicServices {

    private ArabicServices() {
    }

    /**
     * Remove all tashkeel form text.
     * 
     * For example
     * text: "الخَيْلُ وَاللّيْلُ وَالبَيْداءُ تَعرِفُني"
     * to
     * resulting string: "الخيل والليل والبيداء تعرفني"
     *
     * @param text The input string that contains tashkeel {@link String}
     * @return The resulting string (text without tashkeel) {@link String}
     */
    public static String removeTashkeel(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        String allTashkeel = String.join(",", Data.TASHKEEL);
        return text.replaceAll("[" + allTashkeel + "]", "")
                .replace("ٱ", "ا");
    }

    /**
     * Remove all tatweel from text.
     *
     * For example
     * text: "جميـــل"
     * to
     * resulting string: "جميل"
     *
     * @param text The input string that contains tatweel {@link String}
     * @return The resulting string (text without tatweel) {@link String}
     */
    public static String removeTatweel(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        return text.replace("ـ", "");
    }

    /**
     * Remove all dots and tashkeel from text.
     *
     * For example
     * text: "الخَيْلُ وَاللّيْلُ وَالبَيْداءُ تَعرِفُني"
     * to
     * resulting string: "الحىل واللىل والٮىدا ٮعرڡٮى"
     *
     * @param text The input string that contains tashkeel and dots {@link String}
     * @return The resulting string (text without tashkeel or dots) {@link String}
     */
    public static String textToOldArabic(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        text = removeTashkeel(text);
        text = handleNoonIssue(text);
        for (String letter : Data.LETTERS_DICT.keySet()) {
            text = text.replace(letter, Data.LETTERS_DICT.get(letter));
        }
        return text;
    }

    /**
     * Tashfeer text.
     *
     * For example
     * text: "هذه الجملة مشفرة"
     * to
     * resulting string: "ۂذۀ اڵجمـڵة مـښـڨـڕة"
     *
     * @param text The input string {@link String}
     * @return The resulting string {@link String}
     */
    public static String tashfeer(String text) {
        StringBuilder newSentence = new StringBuilder();
        int level = 1;
        for (String word : text.split(" ")) {
            newSentence.append(tashfeerHandler(word, level)).append(" ");
        }
        return newSentence.toString().trim();
    }

    /**
     * Word to letters.
     *
     * For example
     * text: "هذه جملة"
     * to
     * resulting string: "هاء ذال هاء جيم ميم لام تاء_مربوطة"
     *
     * @param word The input string {@link String}
     * @return The resulting string {@link String}
     */
    public static String wordToLetters(String word) {
        StringBuilder newWord = new StringBuilder();

        // Loop through each character in the input word
        for (int i = 0; i < word.length(); i++) {
            String letter = word.charAt(i) + "";

            // Check if the current letter has a pronunciation in PRONOUNCED_LETTERS
            if (Data.PRONOUNCED_LETTERS.containsKey(letter)) {
                newWord.append(Data.PRONOUNCED_LETTERS.get(letter));

                // Add a space after the pronounced letter unless it's the last letter in the
                // word
                if (i != word.length() - 1) {
                    newWord.append(' ');
                }
            } else {
                // If the letter is not in PRONOUNCED_LETTERS, keep it unchanged
                newWord.append(letter);
            }
        }

        return newWord.toString().trim();
    }

    /**
     * RemoveArabicAffixes
     * 
     * Removes predefined affixes (prefixes and suffixes) from an Arabic word if it
     * starts or ends with those affixes.
     * This function is designed specifically for processing Arabic text, where
     * certain affixes might need to be removed
     * for linguistic, stylistic, or morphological reasons.
     *
     * @param word - The Arabic word from which the affixes are to be
     *             removed. {@link String}
     * @return The word after removing any matching affixes. Returns the
     *         original word if no affix matches are found. {@link String}
     */
    public static String removeArabicAffixes(String word) {
        if (Data.ARABIC_PREFIXES.contains(word.substring(0, 2))) {
            // For: ALEF & LAM
            word = word.substring(2);
        } else if (Data.ARABIC_PREFIXES.contains(word.substring(0, 1))) {
            word = word.substring(1);
        }

        if (Data.ARABIC_SUFFIXES.contains(word.substring(word.length() - 2))) {
            word = word.substring(0, word.length() - 2);
        } else if (Data.ARABIC_SUFFIXES.contains(word.substring(word.length() - 1))) {
            word = word.substring(0, word.length() - 1);
        }

        return word.trim();
    }

    /**
     * Performs tashfeer encryption on a given text, but only for words that are
     * considered "banned" words.
     * Banned words are determined based on a predefined similarity ratio.
     *
     * @param text The input text to be encrypted.
     * @return The encrypted text with tashfeer applied and an encryption level
     *         is 2 to banned words.
     */
    public static String tashfeerBannedWords(String text) {
        return tashfeerBannedWords(text, 2);
    }

    /**
     * Performs tashfeer encryption on a given text, but only for words that are
     * considered "banned" words.
     * Banned words are determined based on a predefined similarity ratio.
     *
     * @param text            The input text to be encrypted.
     * @param levelOfTashfeer The encryption level (default is 2).
     * @return The encrypted text with tashfeer applied to banned words.
     */
    public static String tashfeerBannedWords(String text, int levelOfTashfeer) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        StringBuilder newText = new StringBuilder();
        for (String word : text.split(" ")) {
            if (checkIfBannedWord(word)) {
                newText.append(tashfeerHandler(word, levelOfTashfeer)).append(" ");
            } else {
                newText.append(word).append(" ");
            }
        }
        return newText.toString().trim();
    }

    private static String handleNoonIssue(String text) {
        String arabicLetters = String.join("", Data.LETTERS_DICT.keySet()) + "ـ";
        String regex = Data.NOON + "(" + "?=[^" + arabicLetters + "]" + ")|" + Data.NOON + "\\z";
        text = text.replaceAll(regex, Data.LETTERS_DICT.get(Data.NOON));
        return text.replace(Data.NOON, Data.LETTERS_DICT.get("ب"));
    }

    private static String tashfeerHandler(String word, int level) {
        int wordLength = word.length();
        int n = calculateEncryptionLevel(level, wordLength);
        List<Integer> randomIndexes = getRandomIndexes(n, wordLength);
        String outputWord = tashfeerWord(word, randomIndexes);
        return outputWord;
    }

    private static int calculateEncryptionLevel(int level, int wordLength) {
        // Check if the word length is less than or equal to 4
        if (wordLength <= 4) {
            // If so, return the minimum of (1 + level) and the word length
            return Math.min(1 + level, wordLength);
        } else if (wordLength < 8) {
            // If the word length is less than 8
            // Return the minimum of (2 + level) and the word length
            return Math.min(2 + level, wordLength);
        } else {
            // If the word length is 8 or greater
            // Return the minimum of (3 + level) and the word length
            return Math.min(3 + level, wordLength);
        }
    }

    private static List<Integer> getRandomIndexes(int numOfIndexesToNeeded, int wordLength) {
        // Create a Set to store unique random indexes
        Set<Integer> randomIndexes = new HashSet<>();

        // Continue generating random indexes until the desired number is reached
        while (randomIndexes.size() != numOfIndexesToNeeded) {
            // Generate a random index within the word length
            randomIndexes.add(new Random().nextInt(wordLength));
        }

        // Convert the Set to a TreeSet to sort the indexes
        Set<Integer> sortedIndexes = new TreeSet<>(randomIndexes);

        // Convert the TreeSet to an array
        List<Integer> resultArray = new ArrayList<>();
        int index = 0;
        for (int value : sortedIndexes) {
            resultArray.add(index++, value);
        }

        return resultArray;
    }

    private static String tashfeerWord(String word, List<Integer> randomIndexes) {
        StringBuilder outputWord = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            // Check if the character is a standard Arabic letter and needs to be replaced
            // and ignore any other character such as Latin or digits
            // Also, check if the current index is in the list of random indexes for
            // replacement
            if (Data.STANDARD_LETTERS.contains(word.charAt(i))
                    && randomIndexes.contains(i)) {
                // Get the replacement letter for the current character
                char letter = tashfeerCharacter(word.charAt(i));

                // Check if the previous character is not an "alone" letter
                if (i != 0 && !Data.ALONE_LETTERS.contains(word.charAt(i - 1))) {
                    // Add a Maddah character for better readability
                    outputWord.append('ـ');
                }

                // Add the replacement letter to the encrypted word
                outputWord.append(letter);
            } else {
                // If the character doesn't need to be replaced, add it as it is
                outputWord.append(word.charAt(i));
            }
        }

        return outputWord.toString();
    }

    private static char tashfeerCharacter(char character) {
        if (Data.ALEF.contains(character)) {
            character = 'ا';
        }
        if (Data.WAW.contains(character)) {
            character = 'و';
        }
        if (Data.YAA.contains(character)) {
            character = 'ي';
        }

        // Get the list of possible replacement characters for the input character
        String[] replacementCharList = Data.LETTERS_TASHFEER_REPLACEMENT_DICT.get(character);
        // Generate a random index to select a replacement character
        Random random = new Random();
        int randomIndex = random.nextInt(replacementCharList.length);
        // Get the randomly selected replacement character
        char replacementCharacter = replacementCharList[randomIndex].charAt(0);
        return replacementCharacter;
    }

    private static double bannedSimilarityRatio(String string) {
        double maximumSimilarity = -1;
        for (String bannedWord : Data.BANNED_WORDS) {
            double calculatedSimilarity = similarityScore(string, bannedWord);
            if (calculatedSimilarity > maximumSimilarity) {
                maximumSimilarity = calculatedSimilarity;
            }
        }
        return maximumSimilarity * 100;
    }

    private static boolean checkIfBannedWord(String string) {
        double stdRatio = 70;
        return bannedSimilarityRatio(removeArabicAffixes(string)) >= stdRatio;
    }

    private static double similarityScore(String s1, String s2) {
        String longer = s1;
        String shorter = s2;

        // swap them if s1 is bigger than s2
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();

        // if both are empty strings return 1 (100% similarity)
        if (longerLength == 0) {
            return 1.0;
        }

        // calculate the similarity score
        return (double) (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

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

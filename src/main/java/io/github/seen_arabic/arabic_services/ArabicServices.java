package io.github.seen_arabic.arabic_services;

/**
 * Main public entry point (Facade) for Arabic text processing utilities.
 * <p>
 * This class delegates all operations to specialized services while preserving
 * full backward compatibility and static API contracts.
 */
public final class ArabicServices {

    private ArabicServices() {
    }

    /**
     * Remove all tashkeel from text.
     *
     * @param text The input string that contains tashkeel
     * @return The resulting string without tashkeel
     */
    public static String removeTashkeel(String text) {
        return TashkeelService.removeTashkeel(text);
    }

    /**
     * Remove all tatweel from text.
     *
     * @param text The input string that contains tatweel
     * @return The resulting string without tatweel
     */
    public static String removeTatweel(String text) {
        return TashkeelService.removeTatweel(text);
    }

    /**
     * Remove all dots and tashkeel from text with default options.
     *
     * @param text The input string that contains tashkeel and dots
     * @return The resulting string in Old Arabic script
     */
    public static String toOldArabic(String text) {
        return OldArabicService.toOldArabic(text, OldArabicOptions.defaults());
    }

    /**
     * Remove all dots and tashkeel from text with custom {@link OldArabicOptions}.
     *
     * @param text    The input string that contains tashkeel and dots
     * @param options Custom conversion options (if null, default options are used)
     * @return The resulting string in Old Arabic script
     */
    public static String toOldArabic(String text, OldArabicOptions options) {
        return OldArabicService.toOldArabic(text, options);
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
        return TashfeerService.tashfeer(text, 1);
    }

    /**
     * Tashfeer text with a specific obfuscation level.
     *
     * @param text            The input string
     * @param levelOfTashfeer The obfuscation level (must be non-negative)
     * @return The resulting obfuscated string
     */
    public static String tashfeer(String text, int levelOfTashfeer) {
        return TashfeerService.tashfeer(text, levelOfTashfeer);
    }

    /**
     * Converts words in text to pronounced letter names.
     *
     * @param word The input string
     * @return Pronounced letters separated by spaces
     */
    public static String wordToLetters(String word) {
        return LettersService.wordToLetters(word);
    }

    /**
     * Removes listed affixes from each word in text, retaining the word if
     * the stripped result has fewer than 3 Arabic-script letters.
     *
     * @param text The input text
     * @return Text with affixes removed where eligible
     */
    public static String removeArabicAffixes(String text) {
        return AffixesService.removeArabicAffixes(text);
    }

    /**
     * Performs tashfeer on banned words with default level (2).
     *
     * @param text The input text
     * @return Text with banned words obfuscated
     */
    public static String tashfeerBannedWords(String text) {
        return TashfeerService.tashfeerBannedWords(text, 2);
    }

    /**
     * Performs tashfeer on banned words with a custom level.
     *
     * @param text            The input text
     * @param levelOfTashfeer Obfuscation level (must be non-negative)
     * @return Text with banned words obfuscated
     */
    public static String tashfeerBannedWords(String text, int levelOfTashfeer) {
        return TashfeerService.tashfeerBannedWords(text, levelOfTashfeer);
    }

    /**
     * Converts words in text to Old Arabic script and applies tashfeer to banned words with default level (2).
     *
     * @param text The input text
     * @return Processed text
     */
    public static String toOldArabicAndTashfeerBannedWords(String text) {
        return TashfeerService.toOldArabicAndTashfeerBannedWords(text, 2);
    }

    /**
     * Converts words in text to Old Arabic script and applies tashfeer to banned words with custom level.
     *
     * @param text            The input text
     * @param levelOfTashfeer Obfuscation level (must be non-negative)
     * @return Processed text
     */
    public static String toOldArabicAndTashfeerBannedWords(String text, int levelOfTashfeer) {
        return TashfeerService.toOldArabicAndTashfeerBannedWords(text, levelOfTashfeer);
    }

    /**
     * Calculates Levenshtein similarity score between two strings (0.0 to 1.0).
     *
     * @param s1 First string
     * @param s2 Second string
     * @return Similarity score between 0.0 and 1.0
     */
    public static double similarityScore(String s1, String s2) {
        return StringUtils.similarityScore(s1, s2);
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
        return StringUtils.setCharAt(str, index, chr);
    }
}

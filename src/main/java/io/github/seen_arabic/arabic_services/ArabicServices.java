package io.github.seen_arabic.arabic_services;

import java.util.Objects;

public class ArabicServices {

    public ArabicServices() {
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
    public String removeTashkeel(String text) {
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
    public String removeTatweel(String text) {
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
    public String textToOldArabic(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        text = removeTashkeel(text);
        text = handleNoonIssue(text);
        for (String letter : Data.LETTERS_DICT.keySet()) {
            text = text.replace(letter, Data.LETTERS_DICT.get(letter));
        }
        return text;
    }

    private String handleNoonIssue(String text) {
        String arabicLetters = String.join("", Data.LETTERS_DICT.keySet()) + "ـ";
        String regex = Data.NOON + "(" + "?=[^" + arabicLetters + "]" + ")|" + Data.NOON + "\\z";
        text = text.replaceAll(regex, Data.LETTERS_DICT.get(Data.NOON));
        return text.replace(Data.NOON, Data.LETTERS_DICT.get("ب"));
    }
}

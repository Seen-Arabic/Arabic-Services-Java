package io.github.seen_arabic.arabic_services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class ArabicServices {
    private static final String NOON = "ن";
    private static final String TEXT_NULL_MESSAGE = "text must be not null";

    private LettersData lettersData;

    public ArabicServices() {
        try (Reader reader = new InputStreamReader(new FileInputStream("src/main/res/data.json"), "utf-8")) {
            Gson gson = new Gson();
            lettersData = gson.fromJson(new JsonReader(reader), LettersData.class);
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove all tashkeel form text.
     * 
     * For example
     * text: "الخَيْلُ وَاللّيْلُ وَالبَيْداءُ تَعرِفُني"
     * to
     * resulting string: "الخيل والليل والبيداء تعرفني"
     *
     * @param text The input string that contains tashkeel
     * @return The resulting string (text without tashkeel)
     */
    public String removeTashkeel(String text) {
        Objects.requireNonNull(text, TEXT_NULL_MESSAGE);
        String allTashkeel = String.join(",", lettersData.getTashkeel());
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
     * @param text The input string that contains tatweel
     * @return The resulting string (text without tatweel)
     */
    public String removeTatweel(String text) {
        Objects.requireNonNull(text, TEXT_NULL_MESSAGE);
        return text.replace("ـ", "");
    }

    /**
     * Remove all dots & tashkeel from text.
     *
     * For example
     * text: "الخَيْلُ وَاللّيْلُ وَالبَيْداءُ تَعرِفُني"
     * to
     * resulting string: "الحىل واللىل والٮىدا ٮعرڡٮى"
     *
     * @param text The input string that contains tashkeel and dots
     * @return The resulting string (text without tashkeel or dots)
     */
    public String textToOldArabic(String text) {
        Objects.requireNonNull(text, TEXT_NULL_MESSAGE);
        text = removeTashkeel(text);
        text = handleNoonIssue(text);
        for (String letter : lettersData.getLettersDict().keySet()) {
            text = text.replace(letter, lettersData.getLettersDict().get(letter));
        }
        return text;
    }

    private String handleNoonIssue(String text) {
        String arabicLetters = String.join("", lettersData.getLettersDict().keySet()) + "ـ";
        String regex = NOON + "(" + "?=[^" + arabicLetters + "]" + ")|" + NOON + "\\z";
        text = text.replaceAll(regex, lettersData.getLettersDict().get(NOON));
        return text.replace(NOON, lettersData.getLettersDict().get("ب"));
    }
}

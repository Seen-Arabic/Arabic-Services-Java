package io.github.seen_arabic.arabic_services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class ArabicServices {
    private static final String NOON = "ن";

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
        String textWithoutTashkeel = removeTashkeel(text);
        String oldArabicText = handleNoonIssue(textWithoutTashkeel);
        for (String letter : lettersData.getLettersDict().keySet()) {
            oldArabicText = oldArabicText.replace(letter, lettersData.getLettersDict().get(letter));
        }
        return oldArabicText;
    }

    private String handleNoonIssue(String text) {
        boolean isLastLetterNoon = text.lastIndexOf(NOON) == text.length() - 1;
        if (isLastLetterNoon) {
            text = text.substring(0, text.length() - 1) + lettersData.getLettersDict().get(NOON);
        }
        text = text.replace(NOON + " ", lettersData.getLettersDict().get(NOON) + " ");
        text = text.replace(NOON, lettersData.getLettersDict().get("ب"));
        return text;
    }
}

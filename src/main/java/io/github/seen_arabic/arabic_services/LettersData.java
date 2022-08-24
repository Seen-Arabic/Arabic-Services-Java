package io.github.seen_arabic.arabic_services;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class LettersData {

    @SerializedName("LETTERS")
    private String[] letters;

    @SerializedName("LETTERS_DICT")
    private Map<String, String> lettersDict;

    @SerializedName("TASHKEEL")
    private String[] tashkeel;

    /**
     * @param letters
     * @param lettersDict
     * @param tashkeel
     */
    public LettersData(String[] letters, Map<String, String> lettersDict, String[] tashkeel) {
        this.letters = letters;
        this.lettersDict = lettersDict;
        this.tashkeel = tashkeel;
    }

    /**
     * @return the letters
     */
    public String[] getLetters() {
        return letters;
    }

    /**
     * @param letters the letters to set
     */
    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    /**
     * @return the lettersDict
     */
    public Map<String, String> getLettersDict() {
        return lettersDict;
    }

    /**
     * @param lettersDict the lettersDict to set
     */
    public void setLettersDict(Map<String, String> lettersDict) {
        this.lettersDict = lettersDict;
    }

    /**
     * @return the tashkeel
     */
    public String[] getTashkeel() {
        return tashkeel;
    }

    /**
     * @param tashkeel the tashkeel to set
     */
    public void setTashkeel(String[] tashkeel) {
        this.tashkeel = tashkeel;
    }

}

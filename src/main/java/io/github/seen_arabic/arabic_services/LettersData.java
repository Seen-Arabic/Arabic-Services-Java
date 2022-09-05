package io.github.seen_arabic.arabic_services;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

class LettersData {

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
    LettersData(String[] letters, Map<String, String> lettersDict, String[] tashkeel) {
        this.letters = letters;
        this.lettersDict = lettersDict;
        this.tashkeel = tashkeel;
    }

    /**
     * @return the letters
     */
    String[] getLetters() {
        return letters;
    }

    /**
     * @param letters the letters to set
     */
    void setLetters(String[] letters) {
        this.letters = letters;
    }

    /**
     * @return the lettersDict
     */
    Map<String, String> getLettersDict() {
        return lettersDict;
    }

    /**
     * @param lettersDict the lettersDict to set
     */
    void setLettersDict(Map<String, String> lettersDict) {
        this.lettersDict = lettersDict;
    }

    /**
     * @return the tashkeel
     */
    String[] getTashkeel() {
        return tashkeel;
    }

    /**
     * @param tashkeel the tashkeel to set
     */
    void setTashkeel(String[] tashkeel) {
        this.tashkeel = tashkeel;
    }

}

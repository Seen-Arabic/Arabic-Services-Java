package io.github.seen_arabic.arabic_services;

import java.util.HashMap;
import java.util.Map;

class Data {
    static final String NOON = "ن";
    static final String TEXT_NULL_MESSAGE = "text must be not null";

    static final Map<String, String> LETTERS_DICT;

    static final String[] TASHKEEL = {
            "\u0600",
            "\u0601",
            "\u0602",
            "\u0603",
            "\u0604",
            "\u0605",
            "\u0610",
            "\u0611",
            "\u0612",
            "\u0613",
            "\u0614",
            "\u0615",
            "\u0616",
            "\u0617",
            "\u0618",
            "\u0619",
            "\u061A",
            "\u064B",
            "\u064C",
            "\u064D",
            "\u064E",
            "\u064F",
            "\u0650",
            "\u0651",
            "\u0652",
            "\u0653",
            "\u0654",
            "\u0655",
            "\u0656",
            "\u0657",
            "\u0658",
            "\u0659",
            "\u065A",
            "\u065B",
            "\u065C",
            "\u065D",
            "\u065E",
            "\u065F",
            "\u0670",
            "\u0674",
            "\u06D6",
            "\u06D7",
            "\u06D8",
            "\u06D9",
            "\u06DA",
            "\u06DB",
            "\u06DC",
            "\u06DF",
            "\u06E0",
            "\u06E1",
            "\u06E2",
            "\u06E3",
            "\u06E4",
            "\u06E5",
            "\u06E6",
            "\u06E7",
            "\u06E8",
            "\u06EA",
            "\u06EB",
            "\u06EC",
            "\u06ED" };

    static {
        LETTERS_DICT = new HashMap<>();
        LETTERS_DICT.put("ا", "ا");
        LETTERS_DICT.put("أ", "ا");
        LETTERS_DICT.put("إ", "ا");
        LETTERS_DICT.put("آ", "ا");
        LETTERS_DICT.put("ٱ", "ا");
        LETTERS_DICT.put("ء", "");
        LETTERS_DICT.put("ب", "ٮ");
        LETTERS_DICT.put("پ", "ٮ");
        LETTERS_DICT.put("ت", "ٮ");
        LETTERS_DICT.put("ث", "ٮ");
        LETTERS_DICT.put("ج", "ح");
        LETTERS_DICT.put("چ", "ح");
        LETTERS_DICT.put("خ", "ح");
        LETTERS_DICT.put("ح", "ح");
        LETTERS_DICT.put("د", "د");
        LETTERS_DICT.put("ذ", "د");
        LETTERS_DICT.put("ر", "ر");
        LETTERS_DICT.put("ز", "ر");
        LETTERS_DICT.put("ژ", "ر");
        LETTERS_DICT.put("س", "س");
        LETTERS_DICT.put("ش", "س");
        LETTERS_DICT.put("ص", "ص");
        LETTERS_DICT.put("ض", "ص");
        LETTERS_DICT.put("ط", "ط");
        LETTERS_DICT.put("ظ", "ط");
        LETTERS_DICT.put("ع", "ع");
        LETTERS_DICT.put("غ", "ع");
        LETTERS_DICT.put("ف", "ڡ");
        LETTERS_DICT.put("ڤ", "ڡ");
        LETTERS_DICT.put("ق", "ٯ");
        LETTERS_DICT.put("ك", "ک");
        LETTERS_DICT.put("گ", "ک");
        LETTERS_DICT.put("ل", "ل");
        LETTERS_DICT.put("م", "م");
        LETTERS_DICT.put("ن", "ں");
        LETTERS_DICT.put("ه", "ه");
        LETTERS_DICT.put("و", "و");
        LETTERS_DICT.put("ؤ", "و");
        LETTERS_DICT.put("ة", "ه");
        LETTERS_DICT.put("ى", "ى");
        LETTERS_DICT.put("ي", "ى");
        LETTERS_DICT.put("ئ", "ى");
    }

    private Data() {
    }
}
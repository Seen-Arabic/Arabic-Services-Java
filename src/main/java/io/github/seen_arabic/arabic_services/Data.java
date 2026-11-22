package io.github.seen_arabic.arabic_services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Data {
    static final String NOON = "ن";
    static final List<Character> ALEF = Collections.unmodifiableList(Arrays.asList('أ', 'إ', 'آ'));
    static final List<Character> YAA = Collections.unmodifiableList(Arrays.asList('ى', 'ئ'));
    static final List<Character> WAW = Collections.unmodifiableList(Arrays.asList('ؤ'));

    /**
     * List of common Arabic prefixes. These prefixes are used in the
     * removeArabicPrefix method
     * to identify and remove them from the beginning of Arabic words.
     */
    public static final List<String> ARABIC_PREFIXES = Collections
            .unmodifiableList(Arrays.asList("أ", "ا", "إ", "ال", "ي", "ت", "ن", "ب"));

    /**
     * List of common Arabic suffixes. These suffixes might be used in other
     * functions
     * to identify and manipulate them at the end of Arabic words.
     */
    public static final List<String> ARABIC_SUFFIXES = Collections
            .unmodifiableList(Arrays.asList("ة", "ه", "ي", "ى", "ية", "ين", "ون", "هم"));

    static final String TEXT_NULL_MESSAGE = "text must be not null";

    static final Map<String, String> LETTERS_DICT;
    static final Map<Character, List<String>> LETTERS_TASHFEER_REPLACEMENT_DICT;
    static final Map<String, String> PRONOUNCED_LETTERS;

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
            "\u06ED",
            "\u08F0",
            "\u08F1",
            "\u08F2" };

    static final List<Character> STANDARD_LETTERS = Collections.unmodifiableList(Arrays.asList(
            'ا',
            'أ',
            'آ',
            'إ',
            'ب',
            'ت',
            'ث',
            'ح',
            'ج',
            'خ',
            'د',
            'ذ',
            'ر',
            'ز',
            'س',
            'ش',
            'ص',
            'ض',
            'ط',
            'ظ',
            'ع',
            'غ',
            'ف',
            'ق',
            'ك',
            'ل',
            'م',
            'ن',
            'ه',
            'و',
            'ؤ',
            'ي',
            'ى',
            'ئ'));

    static final List<Character> ALONE_LETTERS = Collections
            .unmodifiableList(Arrays.asList('د', 'ذ', 'ر', 'ز', 'و', 'ا', 'أ', 'إ', 'ء', 'ؤ', 'آ'));

    static final String[] BANNED_WORDS = {
            "فلسطين",
            "عرب",
            "هود",
            "صهيون",
            "سرائيل",
            "دول",
            "كيان",
            "حتل",
            "هتلر",
            "خنازير",
            "حي",
            "شيخ",
            "جراح",
            "سقط",
            "قدس",
            "قصى",
            "طبع",
            "قتل",
            "خان",
            "كتائب",
            "عز",
            "دين",
            "قسام",
            "جهاد",
            "جاهد",
            "سلام",
            "خوارزم",
            "لوغاريزم",
            "كتاب",
            "بدون",
            "نقط",
            "مارك",
            "لعن",
            "حقير",
            "موت",
            "قاوم",
            "زوربير",
            "عاصم",
            "حر",
            "رهاب",
            "قرد",
            "دعم",
            "غز",
            "نقذ",
            "نتهك",
            "معايير",
            "صل",
            "بان",
            "دون",
            "جيش",
            "عدو",
            "حماس",
            "كر"
    };

    static {
        Map<String, String> letters = new HashMap<>();
        letters.put("ا", "ا");
        letters.put("أ", "ا");
        letters.put("إ", "ا");
        letters.put("آ", "ا");
        letters.put("ٱ", "ا");
        letters.put("ء", "");
        letters.put("ب", "ٮ");
        letters.put("پ", "ٮ");
        letters.put("ت", "ٮ");
        letters.put("ث", "ٮ");
        letters.put("ج", "ح");
        letters.put("چ", "ح");
        letters.put("خ", "ح");
        letters.put("ح", "ح");
        letters.put("د", "د");
        letters.put("ذ", "د");
        letters.put("ر", "ر");
        letters.put("ز", "ر");
        letters.put("ژ", "ر");
        letters.put("س", "س");
        letters.put("ش", "س");
        letters.put("ص", "ص");
        letters.put("ض", "ص");
        letters.put("ط", "ط");
        letters.put("ظ", "ط");
        letters.put("ع", "ع");
        letters.put("غ", "ع");
        letters.put("ف", "ڡ");
        letters.put("ڤ", "ڡ");
        letters.put("ق", "ٯ");
        letters.put("ك", "ک");
        letters.put("گ", "ک");
        letters.put("ل", "ل");
        letters.put("م", "م");
        letters.put("ن", "ں");
        letters.put("ه", "ه");
        letters.put("و", "و");
        letters.put("ؤ", "و");
        letters.put("ة", "ه");
        letters.put("ى", "ى");
        letters.put("ي", "ى");
        letters.put("ئ", "ى");
        LETTERS_DICT = Collections.unmodifiableMap(letters);

        Map<Character, List<String>> replacements = new HashMap<>();
        replacements.put('ا', Arrays.asList("|", "1", "!", "ן", "ן", "ו", "ⴶ"));
        replacements.put('ب', Arrays.asList("ٮ", "ٻ", "پ", "ڀ", "ٹ", "ޞ", "ސ", "ݐ", "ݒ", "ݕ"));
        replacements.put('ت', Arrays.asList("ٮ", "ٹ", "ٺ", "ټ", "ٽ", "ٿ", "ސ", "ڌ", "ݓ", "ݖ", "ݑ"));
        replacements.put('ث', Arrays.asList("ٮ", "ٹ", "ٽ", "ٿ", "ޝ", "ސ", "ݖ", "ݑ"));
        replacements.put('ح', Arrays.asList("ځ", "7", "ݗ", "ڂ"));
        replacements.put('ج', Arrays.asList("ڃ", "ڄ", "چ", "ڇ", "ݘ"));
        replacements.put('خ', Arrays.asList("ح", "ځ", "ݗ"));
        replacements.put('د', Arrays.asList("ڈ", "ډ", "ڊ", "ڍ", "ݙ", "ݚ", "ב", "כ"));
        replacements.put('ذ', Arrays.asList("ڈ", "ڏ", "ۮ", "נ"));
        replacements.put('ر', Arrays.asList("ړ", "ڕ", "ږ", "ݛ"));
        replacements.put('ز', Arrays.asList("ڑ", "ڙ", "ر", "ژ", "ږ", "ۯ", "ݫ", "ݬ"));
        replacements.put('س', Arrays.asList("ނ", "ښ", "ݜ", "ݭ"));
        replacements.put('ش', Arrays.asList("ښ", "ݜ", "ݭ", "שׂ"));
        replacements.put('ص', Arrays.asList("ڝ"));
        replacements.put('ض', Arrays.asList("ص", "ض"));
        replacements.put('ط', Arrays.asList("ط"));
        replacements.put('ظ', Arrays.asList("ط", "ظ"));
        replacements.put('ع', Arrays.asList("ݟ", "ݝ"));
        replacements.put('غ', Arrays.asList("ڠ", "ݝ", "ݞ", "ݟ", "ع"));
        replacements.put('ف', Arrays.asList("ڡ", "ڤ", "ڦ", "ڨ", "ݠ", "ݡ", "ڡْ"));
        replacements.put('ق', Arrays.asList("ٯ", "ڨ"));
        replacements.put('ك', Arrays.asList("ڬ", "ڭ", "ک", "ڪ", "ګ", "گ", "ڱ", "ڳ", "ݤ"));
        replacements.put('ل', Arrays.asList("ڵ", "ݪ"));
        replacements.put('م', Arrays.asList("ݥ", "ݦ", "ޘ", "ތ", "ס", "ם"));
        replacements.put('ن', Arrays.asList("ں", "ڻ", "ڼ", "ڽ", "ݔ", "ݖ", "ݧ", "ݨ", "ݩ", "ٽ"));
        replacements.put('ه', Arrays.asList("ۀ", "ہ", "ۂ", "ۃ", "ۿ"));
        replacements.put('و', Arrays.asList("ۅ", "ۆ", "ۇ", "ۈ", "ۏ", "ۉ", "ۋ", "ޥ"));
        replacements.put('ي', Arrays.asList("ۍ", "ێ", "ې", "ے", "ی۪"));

        Map<Character, List<String>> unmodReplacements = new HashMap<>();
        for (Map.Entry<Character, List<String>> entry : replacements.entrySet()) {
            unmodReplacements.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        LETTERS_TASHFEER_REPLACEMENT_DICT = Collections.unmodifiableMap(unmodReplacements);

        Map<String, String> pronounced = new HashMap<>();
        pronounced.put("ا", "ألف");
        pronounced.put("إ", "ألف_مكسورة");
        pronounced.put("أ", "ألف");
        pronounced.put("آ", "ألف_مد");
        pronounced.put("ء", "همزة");
        pronounced.put("ب", "باء");
        pronounced.put("ت", "تاء");
        pronounced.put("ث", "ثاء");
        pronounced.put("ج", "جيم");
        pronounced.put("ح", "حاء");
        pronounced.put("خ", "خاء");
        pronounced.put("د", "دال");
        pronounced.put("ذ", "ذال");
        pronounced.put("ر", "راء");
        pronounced.put("ز", "زاي");
        pronounced.put("س", "سين");
        pronounced.put("ش", "شين");
        pronounced.put("ص", "صاد");
        pronounced.put("ض", "ضاد");
        pronounced.put("ط", "طاء");
        pronounced.put("ظ", "ظاء");
        pronounced.put("ع", "عين");
        pronounced.put("غ", "غين");
        pronounced.put("ف", "فاء");
        pronounced.put("ق", "قاف");
        pronounced.put("ك", "كاف");
        pronounced.put("ل", "لام");
        pronounced.put("م", "ميم");
        pronounced.put("ن", "نون");
        pronounced.put("ه", "هاء");
        pronounced.put("و", "واو");
        pronounced.put("ؤ", "همزة_متوسطة_مضمومة");
        pronounced.put("ى", "ألف_لينة");
        pronounced.put("ي", "ياء");
        pronounced.put("ئ", "همزة_متوسطة_مكسورة");
        pronounced.put("ة", "تاء_مربوطة");
        PRONOUNCED_LETTERS = Collections.unmodifiableMap(pronounced);
    }

    private Data() {
    }
}

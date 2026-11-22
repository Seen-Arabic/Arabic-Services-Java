package io.github.seen_arabic.arabic_services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArabicServicesTest {

    // ==========================================
    // 1. removeTashkeel Tests
    // ==========================================
    @Test
    public void testRemoveTashkeel() {
        // Poetry text
        String poetry = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        assertEquals("الخيـل والليـل والبيـداء تعرفني والسيف والرمح والقرطاس والقلـم", ArabicServices.removeTashkeel(poetry));

        // Quran text (Wasla normalization ٱ -> ا)
        String quran = "وَقَالُواْ ٱلۡحَمۡدُ لِلَّهِ ٱلَّذِيٓ أَذۡهَبَ عَنَّا ٱلۡحَزَنَۖ إِنَّ رَبَّنَا لَغَفُورٞ شَكُورٌ";
        assertEquals("وقالوا الحمد لله الذي أذهب عنا الحزن إن ربنا لغفور شكور", ArabicServices.removeTashkeel(quran));

        // Sequential Tanween (\u08F0, \u08F1, \u08F2)
        String tanween = "كِتَابٌ\u08F0 كِتَابٍ\u08F1 كِتَابًا\u08F2";
        assertEquals("كتاب كتاب كتابا", ArabicServices.removeTashkeel(tanween));

        // Preserves English comma and trims whitespace
        assertEquals("مرحبا, يا عالم!", ArabicServices.removeTashkeel("  مَرْحَبًا, يَا عَالَمُ!  "));

        // Stacked diacritics
        assertEquals("محمد وعلي", ArabicServices.removeTashkeel("مُحَمَّدٌ وَعَلِيٌّ"));

        // Mixed text with emojis, numbers, and Latin
        assertEquals("🎉 Hello 123! الخيل والليل (Test 456) ✨",
                ArabicServices.removeTashkeel("🎉 Hello 123! الخَيْلُ وَاللّيْلُ (Test 456) ✨"));

        // Empty string
        assertEquals("", ArabicServices.removeTashkeel(""));
    }

    // ==========================================
    // 2. removeTatweel Tests
    // ==========================================
    @Test
    public void testRemoveTatweel() {
        assertEquals("جميل", ArabicServices.removeTatweel("جميــــل"));
        assertEquals("رائع", ArabicServices.removeTatweel("   رائــــع   "));
        assertEquals("قال", ArabicServices.removeTatweel("قـــــــــــــــــال"));
        assertEquals("", ArabicServices.removeTatweel(""));

        // Combined with Tashkeel
        String cleaned = ArabicServices.removeTatweel(ArabicServices.removeTashkeel("رَائـِـــــعٌ"));
        assertEquals("رائع", cleaned);
    }

    // ==========================================
    // 3. toOldArabic & OldArabicOptions Tests
    // ==========================================
    @Test
    public void testToOldArabic() {
        // Poetry text
        String poetry = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        assertEquals("الحىـل واللىـل والٮىـدا ٮعرڡٮى والسىڡ والرمح والٯرطاس والٯلـم", ArabicServices.toOldArabic(poetry));

        // Quran text
        String quran = "وَقَالُواْ ٱلۡحَمۡدُ لِلَّهِ ٱلَّذِيٓ أَذۡهَبَ عَنَّا ٱلۡحَزَنَۖ إِنَّ رَبَّنَا لَغَفُورٞ شَكُورٌ";
        assertEquals("وٯالوا الحمد لله الدى ادهٮ عٮا الحرں اں رٮٮا لعڡور سکور", ArabicServices.toOldArabic(quran));

        // Noon tests (default replaceMidNoonWithBah=true)
        assertEquals("ٮٮں", ArabicServices.toOldArabic("ننن"));
        assertEquals("ٮــٮــں", ArabicServices.toOldArabic("نــنــن"));
        assertEquals("اٮـــں5 ٮوں. ٮںa", ArabicServices.toOldArabic("أنـــن5 نون. ننa"));

        // Non-Arabic preservation
        assertEquals("User123: کٮٮ email@example.com!", ArabicServices.toOldArabic("User123: كَتَبَ email@example.com!"));

        // Empty string
        assertEquals("", ArabicServices.toOldArabic(""));
        assertEquals("کٮاٮ", ArabicServices.toOldArabic("   كِتَابٌ   "));
    }

    @Test
    public void testToOldArabicWithOptions() {
        // Option: replaceMidNoonWithBah = false
        OldArabicOptions noMidNoon = OldArabicOptions.builder().replaceMidNoonWithBah(false).build();
        assertEquals("ںںں", ArabicServices.toOldArabic("ننن", noMidNoon));
        assertEquals("\u06BA\u06BA\u06BA", ArabicServices.toOldArabic("ننن", noMidNoon));
        assertEquals("ںــںــں", ArabicServices.toOldArabic("نــنــن", noMidNoon));

        // Option: replaceMidYahWithBah = true
        OldArabicOptions withMidYah = OldArabicOptions.builder().replaceMidYahWithBah(true).build();
        assertEquals("ٮٮى", ArabicServices.toOldArabic("ييي", withMidYah));
        assertEquals("\u066E\u066E\u0649", ArabicServices.toOldArabic("ييي", withMidYah));
        assertEquals("ٮــٮــى", ArabicServices.toOldArabic("يــيــي", withMidYah));
        assertEquals("ٮٮٮ", ArabicServices.toOldArabic("بيت", withMidYah));

        // Default Yah behavior (replaceMidYahWithBah = false)
        assertEquals("ىىى", ArabicServices.toOldArabic("ييي"));
        assertEquals("\u0649\u0649\u0649", ArabicServices.toOldArabic("ييي"));
        assertEquals("ىــىــى", ArabicServices.toOldArabic("يــيــي"));
        assertEquals("ٮىٮ", ArabicServices.toOldArabic("بيت"));

        // Poetry with replaceMidYahWithBah
        String poetry = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        assertEquals("الحٮـل واللٮـل والٮٮـدا ٮعرڡٮى والسٮڡ والرمح والٯرطاس والٯلـم",
                ArabicServices.toOldArabic(poetry, withMidYah));

        // null options falls back to defaults
        assertEquals("ٮىٮ", ArabicServices.toOldArabic("بيت", null));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testDeprecatedTextToOldArabic() {
        assertEquals("ٮٮں", ArabicServices.textToOldArabic("ننن"));
        assertEquals("الحىـل واللىـل والٮىـدا ٮعرڡٮى والسىڡ والرمح والٯرطاس والٯلـم",
                ArabicServices.textToOldArabic("الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ"));
    }

    // ==========================================
    // 4. tashfeer Tests
    // ==========================================
    @Test
    public void testTashfeerGeneral() {
        String inputWord = "هذه الجملة مشفرة";
        String result = ArabicServices.tashfeer(inputWord);
        assertNotNull(result);
        assertNotEquals(inputWord, result);

        // Preserves non-Arabic prefix
        String mixed = "Code 99: سلاح";
        String resMixed = ArabicServices.tashfeer(mixed);
        assertTrue(resMixed.startsWith("Code 99:"));

        // Level 0
        String level0 = ArabicServices.tashfeer(inputWord, 0);
        assertNotNull(level0);

        // Level 5
        String level5 = ArabicServices.tashfeer(inputWord, 5);
        assertNotNull(level5);

        // Long words (>= 8 chars)
        String longWord = "المسؤوليات";
        String resLong = ArabicServices.tashfeer(longWord, 1);
        assertNotNull(resLong);
        assertTrue(resLong.length() >= longWord.length());

        // Single letter variants
        for (String c : new String[]{"أ", "ؤ", "ئ"}) {
            String resChar = ArabicServices.tashfeer(c, 1);
            assertNotNull(resChar);
            assertFalse(resChar.isEmpty());
        }

        // Empty string
        assertEquals("", ArabicServices.tashfeer(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTashfeerNegativeLevelThrows() {
        ArabicServices.tashfeer("سلام", -1);
    }

    @Test
    public void testTashfeerMultilineAndWhitespacePreservation() {
        String multiline = "سلاح\nقتل\tحرب";
        for (int level = 1; level <= 5; level++) {
            String result = ArabicServices.tashfeer(multiline, level);
            assertTrue(result.contains("\n"));
            assertTrue(result.contains("\t"));
            assertFalse(result.contains("\nـ"));
        }
    }

    @Test
    public void testTashfeerEmojisAndSurrogatePairs() {
        String emojiText = "😀سلاح";
        for (int i = 0; i < 10; i++) {
            String res = ArabicServices.tashfeer(emojiText, 5);
            assertTrue(res.startsWith("😀"));
            assertFalse(res.contains("😀ـ"));
        }
    }

    // ==========================================
    // 5. tashfeerBannedWords Tests
    // ==========================================
    @Test
    public void testTashfeerBannedWords() {
        String sentence = "جيش العدو يقتل الأطفال";
        String result = ArabicServices.tashfeerBannedWords(sentence);
        assertNotEquals(sentence, result);
        assertTrue(result.contains("الأطفال"));
        assertFalse(result.contains("جيش"));
        assertFalse(result.contains("العدو"));
        assertFalse(result.contains("يقتل"));

        // Exact match preservation of surrounding words
        for (String word : new String[]{"فلسطين", "غزة", "حرية"}) {
            String testSentence = "هذه " + word + " جميلة";
            String obfuscated = ArabicServices.tashfeerBannedWords(testSentence);
            assertFalse(obfuscated.contains(word));
            assertTrue(obfuscated.startsWith("هذه "));
            assertTrue(obfuscated.endsWith(" جميلة"));
        }

        // Non-banned words remain untouched
        String nonBanned = "هذه جملة غير مشفرة";
        assertEquals(nonBanned, ArabicServices.tashfeerBannedWords(nonBanned));

        // Multiline with banned words
        String multiline = "سلاح\nمرحبا";
        String resBanned = ArabicServices.tashfeerBannedWords(multiline);
        assertTrue(resBanned.contains("\n"));
        assertTrue(resBanned.contains("مرحبا"));
        assertFalse(resBanned.startsWith("سلاح"));

        // Empty string
        assertEquals("", ArabicServices.tashfeerBannedWords(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTashfeerBannedWordsNegativeLevelThrows() {
        ArabicServices.tashfeerBannedWords("هذا السلاح ممنوع", -1);
    }

    // ==========================================
    // 6. toOldArabicAndTashfeerBannedWords Tests
    // ==========================================
    @Test
    public void testToOldArabicAndTashfeerBannedWords() {
        String sentence = "جيش العدو يقتل الأطفال";
        String result = ArabicServices.toOldArabicAndTashfeerBannedWords(sentence);
        assertNotEquals(sentence, result);
        assertTrue(result.contains("الاطڡال"));
        assertFalse(result.contains("جيش"));
        assertFalse(result.contains("العدو"));
        assertFalse(result.contains("يقتل"));

        // Empty string
        assertEquals("", ArabicServices.toOldArabicAndTashfeerBannedWords(""));

        // Multiline
        String input = "سلاح\nمرحبا";
        String resOldArabic = ArabicServices.toOldArabicAndTashfeerBannedWords(input);
        assertTrue(resOldArabic.contains("\n"));
        assertFalse(resOldArabic.startsWith("سلاح"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToOldArabicAndTashfeerBannedWordsNegativeLevelThrows() {
        ArabicServices.toOldArabicAndTashfeerBannedWords("هذا السلاح ممنوع", -2);
    }

    // ==========================================
    // 7. wordToLetters Tests
    // ==========================================
    @Test
    public void testWordToLetters() {
        String input = "هذه جملة عربية";
        String result = ArabicServices.wordToLetters(input);
        assertNotNull(result);
        assertNotEquals(input, result);

        assertEquals("", ArabicServices.wordToLetters(""));
        assertEquals("12345 not in Arabic letters", ArabicServices.wordToLetters("12345 not in Arabic letters"));
        assertEquals("حاء باء", ArabicServices.wordToLetters("   حب   "));
        assertEquals("هاء ذال هاء  جيم ميم لام تاء_مربوطة  ألف خاء راء ألف_لينة",
                ArabicServices.wordToLetters("هذه جملة اخرى"));

        // Consecutive whitespace normalization (2 spaces between words, 1 between letters)
        assertEquals("ألف باء", ArabicServices.wordToLetters("أب"));
        assertEquals("ألف حاء ميم دال", ArabicServices.wordToLetters("أحمد"));
        assertEquals("هاء ذال هاء  جيم ميم لام تاء_مربوطة", ArabicServices.wordToLetters("هذه    جملة"));
        assertEquals("باء  تاء", ArabicServices.wordToLetters("ب    ت"));
        // Non-breaking space (\u00A0) normalization
        assertEquals("باء  تاء", ArabicServices.wordToLetters("ب\u00A0\u00A0ت"));
    }

    // ==========================================
    // 8. removeArabicAffixes Comprehensive Tests
    // ==========================================
    @Test
    public void testRemoveArabicAffixesRetainsShortResults() {
        // Words whose stripped result has fewer than three Arabic letters are retained
        String[] shortWords = {
            "نور", "بنت", "اليد", "الدم", "بيده", "الري", "الكي",
            "غزة", "حرية", "عزة", "بِنْت", "نـور", "نور🙂",
            "نabc", "ال12a", "ال🙂a", "ال𞸀ت", "أمل", "يوم", "نجم", "بيت", "ال", "و"
        };
        for (String word : shortWords) {
            assertEquals("Word '" + word + "' should be retained", word, ArabicServices.removeArabicAffixes(word));
        }
    }

    @Test
    public void testRemoveArabicAffixesStripsEligibleWords() {
        // Words with >= 3 Arabic letters after stripping
        assertEquals("مرأ", ArabicServices.removeArabicAffixes("امرأة"));
        assertEquals("نسان", ArabicServices.removeArabicAffixes("إنسان"));
        assertEquals("كتاب", ArabicServices.removeArabicAffixes("الكتاب"));
        assertEquals("كتاب", ArabicServices.removeArabicAffixes("كتابهم"));
        assertEquals("كتاب", ArabicServices.removeArabicAffixes("كتابة"));
        assertEquals("فاح", ArabicServices.removeArabicAffixes("تفاح"));
        assertEquals("مدرس", ArabicServices.removeArabicAffixes("مدرسة"));
        assertEquals("فلسط", ArabicServices.removeArabicAffixes("فلسطين"));
        assertEquals("كالسيوم", ArabicServices.removeArabicAffixes("كالسيوم"));
        assertEquals("والدان", ArabicServices.removeArabicAffixes("والدان"));
        assertEquals("جدي", ArabicServices.removeArabicAffixes("جديه"));
        assertEquals("ذهب", ArabicServices.removeArabicAffixes("ذهبي"));
        assertEquals("منزل", ArabicServices.removeArabicAffixes("منزلي"));
        assertEquals("علم", ArabicServices.removeArabicAffixes("علمية"));
        assertEquals("موظف", ArabicServices.removeArabicAffixes("موظفين"));
        assertEquals("موظف", ArabicServices.removeArabicAffixes("موظفون"));
        assertEquals("طلاب", ArabicServices.removeArabicAffixes("طلابهم"));
        assertEquals("𞸀بت", ArabicServices.removeArabicAffixes("ال𞸀بت"));
    }

    @Test
    public void testRemoveArabicAffixesInSentencesAndSeparators() {
        assertEquals("نور    كتاب  بنت", ArabicServices.removeArabicAffixes("  نور    الكتاب  بنت  "));
        assertEquals("مدرس يحبون طلاب", ArabicServices.removeArabicAffixes("المدرسون يحبون طلابهم"));
        assertEquals("نور\nبنت", ArabicServices.removeArabicAffixes("نور\nبنت"));
        assertEquals("نور\tبنت", ArabicServices.removeArabicAffixes("نور\tبنت"));
        assertEquals("نور،بنت", ArabicServices.removeArabicAffixes("نور،بنت"));
        assertEquals("نور—بنت", ArabicServices.removeArabicAffixes("نور—بنت"));
        assertEquals("كتاب،طلاب", ArabicServices.removeArabicAffixes("الكتاب،طلابهم"));
        assertEquals("(كتاب)", ArabicServices.removeArabicAffixes("(الكتاب)"));
        assertEquals("نور🙂كتاب", ArabicServices.removeArabicAffixes("نور🙂كتابهم"));
        assertEquals("نور \t كتاب\r\nبنت", ArabicServices.removeArabicAffixes("  نور \t الكتاب\r\nبنت  "));

        // Boundary cases
        assertEquals("", ArabicServices.removeArabicAffixes(""));
        assertEquals("", ArabicServices.removeArabicAffixes("   "));
    }

    // ==========================================
    // 9. similarityScore & setCharAt Tests
    // ==========================================
    @Test
    public void testSimilarityScore() {
        assertEquals(1.0, ArabicServices.similarityScore("مرحبا", "مرحبا"), 0.001);
        assertEquals(0.0, ArabicServices.similarityScore("شجرة", "طفل"), 0.001);
        assertEquals(1.0, ArabicServices.similarityScore("", ""), 0.001);
        assertEquals(0.0, ArabicServices.similarityScore("مرحبا", ""), 0.001);
        assertEquals(0.0, ArabicServices.similarityScore("", "مرحبا"), 0.001);

        // Case-insensitivity across scripts
        assertEquals(1.0, ArabicServices.similarityScore("A", "a"), 0.001);
        assertEquals(0.0, ArabicServices.similarityScore("İ", "a"), 0.001);
        assertEquals(0.5, ArabicServices.similarityScore("İ", "i"), 0.001);
        assertEquals(0.0, ArabicServices.similarityScore("İ", ""), 0.001);
        assertEquals(1.0, ArabicServices.similarityScore("İ", "i\u0307"), 0.001);

        assertTrue(ArabicServices.similarityScore("مرحبا", "مرحب") > 0.0);
        assertTrue(ArabicServices.similarityScore("مرحبا", "مرحب") < 1.0);
    }

    @Test
    public void testSetCharAt() {
        assertEquals("كتاب", ArabicServices.setCharAt("كتاب", 10, "ـ"));
        assertEquals("ـتاب", ArabicServices.setCharAt("كتاب", 0, "ـ"));
        assertEquals("كتـب", ArabicServices.setCharAt("كتاب", 2, "ـ"));
        // Boundary: Large Integer levels (integer overflow prevention)
        String resMax = ArabicServices.tashfeer("ب", Integer.MAX_VALUE);
        assertNotEquals("ب", resMax);

        // Negative index clamping like JS
        assertEquals("Xabc", ArabicServices.setCharAt("abc", -1, "X"));
    }

    // ==========================================
    // 10. Performance / Large Text Resilience
    // ==========================================
    @Test
    public void testPerformanceOnLargeText() {
        String unit = "الخَيْلُ وَاللّيْلُ وَالبَيْداءُ تَعرِفُني، وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ! ";
        StringBuilder sb = new StringBuilder(unit.length() * 600);
        for (int i = 0; i < 600; i++) {
            sb.append(unit);
        }
        String largeText = sb.toString();
        assertTrue(largeText.length() > 50000);

        long start = System.currentTimeMillis();
        String noTashkeel = ArabicServices.removeTashkeel(largeText);
        long tashkeelTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        String noTatweel = ArabicServices.removeTatweel(largeText);
        long tatweelTime = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        String oldArabic = ArabicServices.toOldArabic(largeText);
        long oldArabicTime = System.currentTimeMillis() - start;

        assertTrue(noTashkeel.length() < largeText.length());
        assertTrue(noTatweel.length() < largeText.length());
        assertTrue(oldArabic.length() > 0);

        assertTrue("Tashkeel stripping should take < 500ms, took " + tashkeelTime + "ms", tashkeelTime < 500);
        assertTrue("Tatweel stripping should take < 500ms, took " + tatweelTime + "ms", tatweelTime < 500);
        assertTrue("toOldArabic should take < 500ms, took " + oldArabicTime + "ms", oldArabicTime < 500);
    }
}

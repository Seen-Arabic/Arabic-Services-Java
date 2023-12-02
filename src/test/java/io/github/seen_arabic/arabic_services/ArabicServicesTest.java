package io.github.seen_arabic.arabic_services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArabicServicesTest {

    @Test
    public void testRemoveTashkeel() {
        itShouldRemoveAllTashkeelFromPoetryText();
        itShouldRemoveAllTashkeelFromQuranText();
    }

    public void itShouldRemoveAllTashkeelFromPoetryText() {
        String input = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        String actual = ArabicServices.removeTashkeel(input);
        String expected = "الخيـل والليـل والبيـداء تعرفني والسيف والرمح والقرطاس والقلـم";
        assertEquals(expected, actual);
    }

    public void itShouldRemoveAllTashkeelFromQuranText() {
        String input = "وَقَالُواْ ٱلۡحَمۡدُ لِلَّهِ ٱلَّذِيٓ أَذۡهَبَ عَنَّا ٱلۡحَزَنَۖ إِنَّ رَبَّنَا لَغَفُورٞ شَكُورٌ";
        String actual = ArabicServices.removeTashkeel(input);
        String expected = "وقالوا الحمد لله الذي أذهب عنا الحزن إن ربنا لغفور شكور";
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveTatweel() {
        itShouldRemoveAllTatweelFromText();
    }

    public void itShouldRemoveAllTatweelFromText() {
        String input = "جميــــل";
        String actual = ArabicServices.removeTatweel(input);
        String expected = "جميل";
        assertEquals(expected, actual);
    }

    @Test
    public void testTextToOldArabic() {
        itShouldHandleNoon();
        itShouldHandleNoonWithTatweel();
        itShouldRemoveAllDotsAndTashkeelFromPoetryText();
        itShouldRemoveAllDotsAndTashkeelFromQuranText();
        itShouldRemoveAllDotsAndTashkeelFromArabicTextWithOtherText();
    }

    public void itShouldHandleNoon() {
        String input = "ننن";
        String actual = ArabicServices.textToOldArabic(input);
        String expected = "ٮٮں";
        assertEquals(expected, actual);
    }

    public void itShouldHandleNoonWithTatweel() {
        String input = "نــنــن";
        String actual = ArabicServices.textToOldArabic(input);
        String expected = "ٮــٮــں";
        assertEquals(expected, actual);
    }

    public void itShouldRemoveAllDotsAndTashkeelFromPoetryText() {
        String input = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        String actual = ArabicServices.textToOldArabic(input);
        String expected = "الحىـل واللىـل والٮىـدا ٮعرڡٮى والسىڡ والرمح والٯرطاس والٯلـم";
        assertEquals(expected, actual);
    }

    public void itShouldRemoveAllDotsAndTashkeelFromQuranText() {
        String input = "وَقَالُواْ ٱلۡحَمۡدُ لِلَّهِ ٱلَّذِيٓ أَذۡهَبَ عَنَّا ٱلۡحَزَنَۖ إِنَّ رَبَّنَا لَغَفُورٞ شَكُورٌ";
        String actual = ArabicServices.textToOldArabic(input);
        String expected = "وٯالوا الحمد لله الدى ادهٮ عٮا الحرں اں رٮٮا لعڡور سکور";
        assertEquals(expected, actual);
    }

    private void itShouldRemoveAllDotsAndTashkeelFromArabicTextWithOtherText() {
        String input = "أنـــن5 نون. ننa";
        String actual = ArabicServices.textToOldArabic(input);
        String expected = "اٮـــں5 ٮوں. ٮںa";
        assertEquals(expected, actual);
    }

    @Test
    public void testTashfeer() {
        String inputWord = "هذه الجملة مشفرة";
        String result = ArabicServices.tashfeer(inputWord);
        assertNotNull(result);
        assertNotEquals(result, inputWord);
    }

    @Test
    public void testWordToLetters() {
        itShouldReturnAStringWithPronouncedArabicLetters();
        itShouldHandleEmptyInput();
        itShouldHandleInputWithNoPronouncedArabicLetters();
        itShouldHandleInputWithSpaces();
    }

    private void itShouldReturnAStringWithPronouncedArabicLetters() {
        String input = "هذه جملة عربية";
        String result = ArabicServices.wordToLetters(input);
        assertNotNull(result);
        assertNotEquals(input, result);
    }

    private void itShouldHandleEmptyInput() {
        String input = "";
        String result = ArabicServices.wordToLetters(input);
        assertEquals("", result);
    }

    private void itShouldHandleInputWithNoPronouncedArabicLetters() {
        String input = "12345 not in Arabic letters";
        String result = ArabicServices.wordToLetters(input);
        assertEquals(input, result);
    }

    private void itShouldHandleInputWithSpaces() {
        String input = "هذه جملة اخرى";
        String result = ArabicServices.wordToLetters(input);
        assertEquals("هاء ذال هاء  جيم ميم لام تاء_مربوطة  ألف خاء راء ألف_لينة", result);
    }

    @Test
    public void testRemoveArabicAffixes() {
        itShouldRemoveAlfPrefixFromAWord();
        itShouldRemoveAlefPrefixAndTaaSuffixFromAWord();
        itShouldRemoveAlefHamzaBelowPrefixFromAWord();
        itShouldRemoveAlPrefixFromAWord();
        itShouldRemoveYaPrefixFromAWord();
        itShouldRemoveTaPrefixFromAWord();
        itShouldRemoveNunPrefixFromAWord();
        itShouldRemoveBaPrefixFromAWord();
        itShouldRemoveTaSuffixFromAWord();
        itShouldRemoveHaSuffixFromAWord();
        itShouldRemoveYaSuffixFromAWord();
        itShouldRemoveAlefMaksuraSuffixFromAWord();
        itShouldRemoveYaAlefSuffixFromAWord();
        itShouldRemoveYaNunSuffixFromAWord();
        itShouldRemoveWawNunSuffixFromAWord();
        itShouldRemoveHumSuffixFromAWord();
    }

    private void itShouldRemoveAlfPrefixFromAWord() {
        String word = "أمل";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("مل", result);
    }

    private void itShouldRemoveAlefPrefixAndTaaSuffixFromAWord() {
        String word = "امرأة";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("مرأ", result);
    }

    private void itShouldRemoveAlefHamzaBelowPrefixFromAWord() {
        String word = "إنسان";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("نسان", result);
    }

    private void itShouldRemoveAlPrefixFromAWord() {
        String word = "الكتاب";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("كتاب", result);
    }

    private void itShouldRemoveYaPrefixFromAWord() {
        String word = "يوم";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("وم", result);
    }

    private void itShouldRemoveTaPrefixFromAWord() {
        String word = "تفاح";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("فاح", result);
    }

    private void itShouldRemoveNunPrefixFromAWord() {
        String word = "نجم";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("جم", result);
    }

    private void itShouldRemoveBaPrefixFromAWord() {
        String word = "بيت";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("يت", result);
    }

    private void itShouldRemoveTaSuffixFromAWord() {
        String word = "كتابة";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("كتاب", result);
    }

    private void itShouldRemoveHaSuffixFromAWord() {
        String word = "جديه";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("جدي", result);
    }

    private void itShouldRemoveYaSuffixFromAWord() {
        String word = "ذهبي";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("ذهب", result);
    }

    private void itShouldRemoveAlefMaksuraSuffixFromAWord() {
        String word = "منزلي";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("منزل", result);
    }

    private void itShouldRemoveYaAlefSuffixFromAWord() {
        String word = "علمية";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("علم", result);
    }

    private void itShouldRemoveYaNunSuffixFromAWord() {
        String word = "موظفين";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("موظف", result);
    }

    private void itShouldRemoveWawNunSuffixFromAWord() {
        String word = "موظفون";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("موظف", result);
    }

    private void itShouldRemoveHumSuffixFromAWord() {
        String word = "طلابهم";
        String result = ArabicServices.removeArabicAffixes(word);
        assertEquals("طلاب", result);
    }

    @Test
    public void itShouldPerformTashfeerEncryptionOnBannedWordsOnly() {
        String sentence = "جيش العدو يقتل الأطفال";
        String result = ArabicServices.tashfeerBannedWords(sentence);
        assertNotEquals(sentence, result);
        assertTrue(result.contains("الأطفال"));
        assertFalse(result.contains("جيش"));
        assertFalse(result.contains("العدو"));
        assertFalse(result.contains("يقتل"));
    }

    @Test
    public void itShouldNotPerformTashfeerEncryptionOnNonBannedWords() {
        String sentence = "هذه جملة غير مشفرة";
        String result = ArabicServices.tashfeerBannedWords(sentence);
        assertEquals(sentence, result);
    }

    @Test
    public void tashfeerBannedWordsShouldHandleEmptyInput() {
        String sentence = "";
        String result = ArabicServices.tashfeerBannedWords(sentence);
        assertEquals("", result);
    }
}

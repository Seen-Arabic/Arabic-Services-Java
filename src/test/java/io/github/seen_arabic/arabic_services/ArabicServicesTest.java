package io.github.seen_arabic.arabic_services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArabicServicesTest {

    @Test
    public void testRemoveTashkeel() {
        itShouldRemoveAllTashkeelFromPoetryText();
        itShouldRemoveAllTashkeelFromQuranText();
    }

    public void itShouldRemoveAllTashkeelFromPoetryText() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        String actual = arabicServices.removeTashkeel(input);
        String expected = "الخيـل والليـل والبيـداء تعرفني والسيف والرمح والقرطاس والقلـم";
        assertEquals(expected, actual);
    }

    public void itShouldRemoveAllTashkeelFromQuranText() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "وَقَالُواْ ٱلۡحَمۡدُ لِلَّهِ ٱلَّذِيٓ أَذۡهَبَ عَنَّا ٱلۡحَزَنَۖ إِنَّ رَبَّنَا لَغَفُورٞ شَكُورٌ";
        String actual = arabicServices.removeTashkeel(input);
        String expected = "وقالوا الحمد لله الذي أذهب عنا الحزن إن ربنا لغفور شكور";
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveTatweel() {
        itShouldRemoveAllTatweelFromText();
    }

    public void itShouldRemoveAllTatweelFromText() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "جميــــل";
        String actual = arabicServices.removeTatweel(input);
        String expected = "جميل";
        assertEquals(expected, actual);
    }

    @Test
    public void testTextToOldArabic() {
        itShouldHandleNoon();
        itShouldHandleNoonWithTatweel();
        itShouldRemoveAllDotsAndTashkeelFromPoetryText();
        itShouldRemoveAllDotsAndTashkeelFromQuranText();
    }

    public void itShouldHandleNoon() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "ننن";
        String actual = arabicServices.textToOldArabic(input);
        String expected = "ٮٮں";
        assertEquals(expected, actual);
    }

    public void itShouldHandleNoonWithTatweel() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "نــنــن";
        String actual = arabicServices.textToOldArabic(input);
        String expected = "ٮــٮــں";
        assertEquals(expected, actual);
    }

    public void itShouldRemoveAllDotsAndTashkeelFromPoetryText() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "الخَيْـلُ وَاللّيْـلُ وَالبَيْـداءُ تَعرِفُني وَالسّيفُ وَالرّمحُ والقرْطاسُ وَالقَلَـمُ";
        String actual = arabicServices.textToOldArabic(input);
        String expected = "الحىـل واللىـل والٮىـدا ٮعرڡٮى والسىڡ والرمح والٯرطاس والٯلـم";
        assertEquals(expected, actual);
    }

    public void itShouldRemoveAllDotsAndTashkeelFromQuranText() {
        ArabicServices arabicServices = new ArabicServices();
        String input = "وَقَالُواْ ٱلۡحَمۡدُ لِلَّهِ ٱلَّذِيٓ أَذۡهَبَ عَنَّا ٱلۡحَزَنَۖ إِنَّ رَبَّنَا لَغَفُورٞ شَكُورٌ";
        String actual = arabicServices.textToOldArabic(input);
        String expected = "وٯالوا الحمد لله الدى ادهٮ عٮا الحرں اں رٮٮا لعڡور سکور";
        assertEquals(expected, actual);
    }
}

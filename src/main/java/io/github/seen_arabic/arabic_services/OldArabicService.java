package io.github.seen_arabic.arabic_services;

import java.util.Objects;

final class OldArabicService {

    private OldArabicService() {
    }

    static String toOldArabic(String text, OldArabicOptions options) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        if (options == null) {
            options = OldArabicOptions.defaults();
        }

        String sentence = TashkeelService.removeTashkeel(text);
        StringBuilder newSentence = new StringBuilder(sentence.length());

        for (int i = 0; i < sentence.length(); i++) {
            char currentChar = sentence.charAt(i);
            String currentStr = String.valueOf(currentChar);

            if (!Data.LETTERS_DICT.containsKey(currentStr)) {
                newSentence.append(currentChar);
            } else if (options.isReplaceMidNoonWithBah()
                    && currentChar == 'ن'
                    && i + 1 < sentence.length()
                    && (Data.LETTERS_DICT.containsKey(String.valueOf(sentence.charAt(i + 1)))
                            || sentence.charAt(i + 1) == 'ـ')) {
                newSentence.append(Data.LETTERS_DICT.get("ب"));
            } else if (options.isReplaceMidYahWithBah()
                    && currentChar == 'ي'
                    && i + 1 < sentence.length()
                    && (Data.LETTERS_DICT.containsKey(String.valueOf(sentence.charAt(i + 1)))
                            || sentence.charAt(i + 1) == 'ـ')) {
                newSentence.append(Data.LETTERS_DICT.get("ب"));
            } else {
                newSentence.append(Data.LETTERS_DICT.get(currentStr));
            }
        }

        return newSentence.toString();
    }
}

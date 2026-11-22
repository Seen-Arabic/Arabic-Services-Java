package io.github.seen_arabic.arabic_services;

import java.util.Objects;
import java.util.regex.Pattern;

final class TashkeelService {

    private static final String ALL_TASHKEEL_CHARS = String.join("", Data.TASHKEEL);

    private TashkeelService() {
    }

    static String removeTashkeel(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        String cleanText = text.trim();
        StringBuilder sb = new StringBuilder(cleanText.length());
        for (int i = 0; i < cleanText.length(); i++) {
            char c = cleanText.charAt(i);
            if (c == 'ٱ') {
                sb.append('ا');
            } else if (ALL_TASHKEEL_CHARS.indexOf(c) < 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static String removeTatweel(String text) {
        Objects.requireNonNull(text, Data.TEXT_NULL_MESSAGE);
        return text.trim().replace("\u0640", "");
    }
}

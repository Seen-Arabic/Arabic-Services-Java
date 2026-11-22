package io.github.seen_arabic.arabic_services;

import java.util.Locale;
import java.util.Objects;

final class StringUtils {

    private StringUtils() {
    }

    static double similarityScore(String s1, String s2) {
        Objects.requireNonNull(s1, "s1 must be not null");
        Objects.requireNonNull(s2, "s2 must be not null");

        s1 = s1.toLowerCase(Locale.ROOT);
        s2 = s2.toLowerCase(Locale.ROOT);

        String longer = s1;
        String shorter = s2;

        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }

        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }

        return (double) (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    static String setCharAt(String str, int index, String chr) {
        Objects.requireNonNull(str, "str must be not null");
        Objects.requireNonNull(chr, "chr must be not null");
        if (index > str.length() - 1) {
            return str;
        }
        int start = Math.max(0, index);
        int end = Math.max(0, index + 1);
        return str.substring(0, start) + chr + str.substring(end);
    }

    private static int editDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }
}

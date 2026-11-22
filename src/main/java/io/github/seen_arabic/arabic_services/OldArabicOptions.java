package io.github.seen_arabic.arabic_services;

import java.util.Objects;

/**
 * Configuration options for converting text to Old Arabic script.
 * Instances of this class are immutable and thread-safe.
 */
public final class OldArabicOptions {

    private final boolean replaceMidNoonWithBah;
    private final boolean replaceMidYahWithBah;

    private static final OldArabicOptions DEFAULT_OPTIONS = new OldArabicOptions(true, false);

    private OldArabicOptions(boolean replaceMidNoonWithBah, boolean replaceMidYahWithBah) {
        this.replaceMidNoonWithBah = replaceMidNoonWithBah;
        this.replaceMidYahWithBah = replaceMidYahWithBah;
    }

    /**
     * Returns the default Old Arabic conversion options:
     * - replaceMidNoonWithBah: true
     * - replaceMidYahWithBah: false
     *
     * @return default options
     */
    public static OldArabicOptions defaults() {
        return DEFAULT_OPTIONS;
    }

    /**
     * Creates a new builder for {@link OldArabicOptions}.
     *
     * @return a new builder initialized with default values
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Whether medial Noon (ن) is replaced with dotless Bah (ٮ).
     *
     * @return true if enabled
     */
    public boolean isReplaceMidNoonWithBah() {
        return replaceMidNoonWithBah;
    }

    /**
     * Whether medial Yah (ي) is replaced with dotless Bah (ٮ).
     *
     * @return true if enabled
     */
    public boolean isReplaceMidYahWithBah() {
        return replaceMidYahWithBah;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OldArabicOptions)) {
            return false;
        }
        OldArabicOptions that = (OldArabicOptions) o;
        return replaceMidNoonWithBah == that.replaceMidNoonWithBah
                && replaceMidYahWithBah == that.replaceMidYahWithBah;
    }

    @Override
    public int hashCode() {
        return Objects.hash(replaceMidNoonWithBah, replaceMidYahWithBah);
    }

    @Override
    public String toString() {
        return "OldArabicOptions{" +
                "replaceMidNoonWithBah=" + replaceMidNoonWithBah +
                ", replaceMidYahWithBah=" + replaceMidYahWithBah +
                '}';
    }

    /**
     * Builder for {@link OldArabicOptions}.
     */
    public static final class Builder {
        private boolean replaceMidNoonWithBah = true;
        private boolean replaceMidYahWithBah = false;

        public Builder() {
        }

        public Builder replaceMidNoonWithBah(boolean replaceMidNoonWithBah) {
            this.replaceMidNoonWithBah = replaceMidNoonWithBah;
            return this;
        }

        public Builder replaceMidYahWithBah(boolean replaceMidYahWithBah) {
            this.replaceMidYahWithBah = replaceMidYahWithBah;
            return this;
        }

        public OldArabicOptions build() {
            if (replaceMidNoonWithBah && !replaceMidYahWithBah) {
                return DEFAULT_OPTIONS;
            }
            return new OldArabicOptions(replaceMidNoonWithBah, replaceMidYahWithBah);
        }
    }
}

package com.riversoforion.numeris;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * {@code RomanNumeral} implements a standard Roman numeral, in the range of 1 to 3,999.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(
        of = "numericValue",
        cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY,
        doNotUseGetters = true
)
public final class RomanNumeral {

    /**
     * Retrieves the numeric value of the Roman numeral (e.g. 11 for 'XI').
     */
    private final long numericValue;
    /**
     * Retrieves the Roman representation (e.g. 'XI' for 11).
     */
    private final String stringValue;

    /**
     * Creates a new {@code RomanNumeral} with the given numeric value.
     *
     * @param numericValue The numeric (integral) value of the new Roman numeral.
     * @return The newly constructed Roman numeral value.
     */
    public static RomanNumeral of(long numericValue) {

        return new RomanNumeral(numericValue, "");
    }

    /**
     * Parses the given Roman representation to create a new {@code RomanNumeral}, if possible.
     *
     * @param stringValue The Roman representation of the new Roman numeral.
     * @return The newly constructed Roman numeral value.
     */
    public static RomanNumeral parse(String stringValue) {

        return new RomanNumeral(0, stringValue);
    }
}

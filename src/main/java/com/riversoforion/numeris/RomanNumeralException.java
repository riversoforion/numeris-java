package com.riversoforion.numeris;

import lombok.Getter;

import java.util.Optional;
import java.util.OptionalLong;


/**
 * Exception class to indicate an error converting between Roman numerals and integral values.
 */
@Getter
@SuppressWarnings({ "OptionalUsedAsFieldOrParameterType", "java:S1948" })
public final class RomanNumeralException extends Exception {

    private final OptionalLong numericValue;
    private final Optional<String> stringValue;

    RomanNumeralException(String message) {

        this(message, null, OptionalLong.empty(), Optional.empty());
    }

    RomanNumeralException(String message, OptionalLong numericValue) {

        this(message, null, numericValue, Optional.empty());
    }

    RomanNumeralException(String message, Optional<String> stringValue) {

        this(message, null, OptionalLong.empty(), stringValue);
    }

    RomanNumeralException(String message, Throwable cause, OptionalLong numericValue, Optional<String> stringValue) {

        super(message, cause);
        this.numericValue = numericValue;
        this.stringValue = stringValue;
    }

    /**
     * Value provided cannot be converted because it was empty.
     */
    public static RomanNumeralException emptyValue() {

        return new RomanNumeralException("Empty value");
    }

    /**
     * Value provided cannot be converted because it was too small.
     *
     * @param value The value that could not be converted
     * @see RomanNumeral#MIN_VALUE
     */
    public static RomanNumeralException valueTooSmall(long value) {

        return new RomanNumeralException(String.format("%d is too small", value), OptionalLong.empty());
    }

    /**
     * Value provided cannot be converted because it was too large.
     *
     * @param value The value that could not be converted
     * @see RomanNumeral#MAX_VALUE
     */
    public static RomanNumeralException valueTooLarge(long value) {

        return new RomanNumeralException(String.format("%d is too large", value), OptionalLong.empty());
    }

    /**
     * Value provided did not represent a valid Roman numeral.
     *
     * @param value The value that could not be converted
     */
    public static RomanNumeralException unparseable(String value) {

        return new RomanNumeralException(String.format("%s is not a valid a Roman numeral", value), Optional.of(value));
    }
}

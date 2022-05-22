package com.riversoforion.numeris;

import lombok.Getter;

import java.util.Optional;
import java.util.OptionalLong;


@Getter
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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

    public static RomanNumeralException emptyValue() {

        return new RomanNumeralException("Empty value");
    }

    public static RomanNumeralException valueTooSmall(long value) {

        return new RomanNumeralException(String.format("%d is too small", value), OptionalLong.empty());
    }

    public static RomanNumeralException valueTooLarge(long value) {

        return new RomanNumeralException(String.format("%d is too large", value), OptionalLong.empty());
    }

    public static RomanNumeralException unparseable(String value) {

        return new RomanNumeralException(String.format("%s is not a valid a Roman numeral", value), Optional.of(value));
    }
}

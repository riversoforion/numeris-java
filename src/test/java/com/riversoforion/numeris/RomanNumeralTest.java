package com.riversoforion.numeris;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


class RomanNumeralTest {

    @ParameterizedTest
    @CsvSource(textBlock = """
                           1,      I
                           21,     XXI
                           506,    DVI
                           2181,   MMCLXXXI
                           """)
    void of(long numericValue, String expectedRomanValue) throws RomanNumeralException {

        assertThat(RomanNumeral.of(numericValue).stringValue()).isEqualTo(expectedRomanValue);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
                           I,          1
                           IV,         4
                           XXI,        21
                           ' dvi    ', 506
                           """)
    void parse(String romanValue, long expectedNumericValue) throws RomanNumeralException {

        assertThat(RomanNumeral.parse(romanValue).numericValue()).isEqualTo(expectedNumericValue);
    }

    @Test
    void hashCodeAndEquals() throws RomanNumeralException {

        var rnFromNumber = RomanNumeral.of(258);
        var rnFromString = RomanNumeral.parse("CCLVIII");
        assertThat(rnFromNumber).hasSameHashCodeAs(rnFromString)
                                .isEqualTo(rnFromString)
                                .isNotSameAs(rnFromString);
    }
}

package com.riversoforion.numeris;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static com.riversoforion.numeris.Atom.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@DisplayName("Atom enum class")
class AtomTest {

    @Test
    @DisplayName("iteration over enum values")
    void valueIteration() {

        var iter = Arrays.stream(values()).iterator();
        assertEquals(M, iter.next());
        assertEquals(CM, iter.next());
        assertEquals(D, iter.next());
        assertEquals(CD, iter.next());
        assertEquals(C, iter.next());
        assertEquals(XC, iter.next());
        assertEquals(L, iter.next());
        assertEquals(XL, iter.next());
        assertEquals(X, iter.next());
        assertEquals(IX, iter.next());
        assertEquals(V, iter.next());
        assertEquals(IV, iter.next());
        assertEquals(I, iter.next());
        assertFalse(iter.hasNext());
    }

    @ParameterizedTest(name = "{0} is configured correctly")
    @CsvSource(textBlock = """
                           M,   1000, 3
                           CM,  900,  1
                           D,   500,  1
                           CD,  400,  1
                           C,   100,  3
                           XC,  90,   1
                           L,   50,   1
                           XL,  40,   1
                           X,   10,   3
                           IX,  9,    1
                           V,   5,    1
                           IV,  4,    1
                           I,   1,    3
                           """)
    @DisplayName("atoms are correctly configured")
    void valuesCorrectlySet(Atom atom, long expectedValue, int expectedMaxGroup) {

        assertThat(atom.value()).isEqualTo(expectedValue);
        assertThat(atom.maxGroup()).isEqualTo(expectedMaxGroup);
    }

    @Test
    @DisplayName("iteration over digits")
    void digitIteration() {

        var digits = digits().iterator();
        assertThat(digits.next()).isEqualTo(M.value());
        assertThat(digits.next()).isEqualTo(CM.value());
        assertThat(digits.next()).isEqualTo(D.value());
        assertThat(digits.next()).isEqualTo(CD.value());
        assertThat(digits.next()).isEqualTo(C.value());
        assertThat(digits.next()).isEqualTo(XC.value());
        assertThat(digits.next()).isEqualTo(L.value());
        assertThat(digits.next()).isEqualTo(XL.value());
        assertThat(digits.next()).isEqualTo(X.value());
        assertThat(digits.next()).isEqualTo(IX.value());
        assertThat(digits.next()).isEqualTo(V.value());
        assertThat(digits.next()).isEqualTo(IV.value());
        assertThat(digits.next()).isEqualTo(I.value());
    }

    @ParameterizedTest(name = "{0} is converted to {1}")
    @CsvSource(textBlock = """
                           1,    I
                           4,    IV
                           9,    IX
                           10,   X
                           40,   XL
                           50,   L
                           90,   XC
                           100,  C
                           400,  CD
                           500,  D
                           900,  CM
                           1000, M
                           """)
    @DisplayName("symbols are derived from values")
    void symbolsFromValue(long value, String expectedSymbol) {

        assertThat(symbolFromValue(value)).get().isEqualTo(expectedSymbol);
    }
}

package com.riversoforion.numeris;

import com.diffplug.common.base.Either;
import org.assertj.core.api.Condition;
import org.assertj.core.condition.MappedCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;


@DisplayName("Roman numeral-to-integer conversion")
class RomanToIntegerTest {

    private RomanToInteger rtoi;
    private final Condition<Either<Long, RomanNumeralException>> ok = new Condition<>(Either::isLeft, "is error");
    private final Function<Either<Long, RomanNumeralException>, Long> okResult = Either::getLeft;
    private final Condition<Either<Long, RomanNumeralException>> err = new Condition<>(Either::isRight, "is error");

    @BeforeEach
    void setup() {

        this.rtoi = new RomanToInteger();
    }

    @Nested
    @DisplayName("functional usage with wrapped long values")
    class WrappedFunctional {

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               I, 1
                               V, 5
                               X, 10
                               L, 50
                               C, 100
                               D, 500
                               M, 1000
                               """)
        @DisplayName("simple numerals")
        void simpleNumerals(String romanNumeral, Long expectedNumericValue) {

            var correctResult = correctResultCondition(expectedNumericValue);
            assertThat(rtoi.apply(romanNumeral)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               II,        2
                               XV,        15
                               XLII,      42
                               CXXIII,    123
                               CMLXXXVII, 987
                               MMMCMXCIX, 3999
                               """)
        @DisplayName("compound values")
        void compoundValues(String romanNumeral, Long expectedNumericValue) {

            var correctResult = correctResultCondition(expectedNumericValue);
            assertThat(rtoi.apply(romanNumeral)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }

        @ParameterizedTest(name = "{0} is empty")
        @NullAndEmptySource
        @ValueSource(strings = { " ", "\t", "\n", "\r", "    ", " \t   \n " })
        @DisplayName("empty values")
        void emptyValues(String emptyValue) {

            assertThat(rtoi.apply(emptyValue)).isNotNull()
                                         .is(err);
        }

        @ParameterizedTest(name = "{0} is invalid")
        @ValueSource(strings = { "ABCDEF", "MMDL1", "934;-)" })
        @DisplayName("invalid values")
        void invalidValues(String invalidValue) {

            assertThat(rtoi.apply(invalidValue)).isNotNull()
                                                .is(err);
        }

        @ParameterizedTest(name = "{0} is the wrong format")
        @ValueSource(strings = { "CMM", "ID", "MMCCD", "XLXL" })
        @DisplayName("invalid format")
        void invalidFormat(String invalidValue) {

            assertThat(rtoi.apply(invalidValue)).isNotNull()
                                                .is(err);
        }

        @ParameterizedTest(name = "{0} is accepted")
        @CsvSource(textBlock = """
                               mcmxl,     1940
                               ' cclxi ', 261
                               mmCCxXiI,  2222
                               """)
        @DisplayName("other valid numerals")
        void otherValidNumerals(String romanNumeral, Long expectedNumericValue) {

            var correctResult = correctResultCondition(expectedNumericValue);
            assertThat(rtoi.apply(romanNumeral)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }
    }

    @Nested
    @DisplayName("imperative usage")
    class Imperative {

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               I, 1
                               V, 5
                               X, 10
                               L, 50
                               C, 100
                               D, 500
                               M, 1000
                               """)
        @DisplayName("simple values")
        void simpleNumerals(String romanNumeral, Long expectedNumericValue) throws RomanNumeralException {

            assertThat(rtoi.convert(romanNumeral)).isEqualTo(expectedNumericValue);
        }

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               II,        2
                               XV,        15
                               XLII,      42
                               CXXIII,    123
                               CMLXXXVII, 987
                               MMMCMXCIX, 3999
                               """)
        @DisplayName("compound values")
        void compoundValues(String romanNumeral, Long expectedNumericValue) throws RomanNumeralException {

            var correctResult = correctResultCondition(expectedNumericValue);
            assertThat(rtoi.convert(romanNumeral)).isEqualTo(expectedNumericValue);
        }

        @ParameterizedTest(name = "{0} is empty")
        @NullAndEmptySource
        @ValueSource(strings = { " ", "\t", "\n", "\r", "    ", " \t   \n " })
        @DisplayName("empty values")
        void emptyValues(String emptyValue) {

            assertThatExceptionOfType(RomanNumeralException.class)
                    .isThrownBy(() -> rtoi.convert(emptyValue))
                    .withMessage("Empty value");
        }

        @ParameterizedTest(name = "{0} is invalid")
        @ValueSource(strings = { "ABCDEF", "MMDL1", "934;-)" })
        @DisplayName("invalid values")
        void invalidValues(String invalidValue) {

            assertThatExceptionOfType(RomanNumeralException.class)
                    .isThrownBy(() -> rtoi.convert(invalidValue))
                    .withMessage(String.format("%s is not a valid a Roman numeral", invalidValue));
        }

        @ParameterizedTest(name = "{0} is the wrong format")
        @ValueSource(strings = { "CMM", "ID", "MMCCD", "XLXL" })
        @DisplayName("invalid format")
        void invalidFormat(String invalidValue) {

            assertThatExceptionOfType(RomanNumeralException.class)
                    .isThrownBy(() -> rtoi.convert(invalidValue))
                    .withMessage(String.format("%s is not a valid a Roman numeral", invalidValue));
        }

        @ParameterizedTest(name = "{0} is accepted")
        @CsvSource(textBlock = """
                               mcmxl,     1940
                               ' cclxi ', 261
                               mmCCxXiI,  2222
                               """)
        @DisplayName("other valid numerals")
        void otherValidNumerals(String romanNumeral, Long expectedNumericValue) throws RomanNumeralException {

            assertThat(rtoi.convert(romanNumeral)).isEqualTo(expectedNumericValue);
        }
    }

    private MappedCondition<Either<Long, RomanNumeralException>, Long> correctResultCondition(Long expectedNumericValue) {

        return MappedCondition.mappedCondition(
                okResult,
                new Condition<>((res) -> res.equals(expectedNumericValue), String.format("equals %s", expectedNumericValue))
        );
    }
}

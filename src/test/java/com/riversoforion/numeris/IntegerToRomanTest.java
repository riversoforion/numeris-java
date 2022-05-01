package com.riversoforion.numeris;

import com.diffplug.common.base.Either;
import org.assertj.core.api.Condition;
import org.assertj.core.condition.MappedCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.riversoforion.numeris.RomanNumeral.MAX_VALUE;
import static com.riversoforion.numeris.RomanNumeral.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@DisplayName("Integer-to-Roman numeral conversion")
class IntegerToRomanTest {

    private IntegerToRoman itor;
    Condition<Either<String, RomanNumeralException>> ok = new Condition<>(Either::isLeft, "is error");
    Function<Either<String, RomanNumeralException>, String> okResult = Either::getLeft;
    Condition<Either<String, RomanNumeralException>> err = new Condition<>(Either::isRight, "is error");

    @BeforeEach
    void setup() {

        this.itor = new IntegerToRoman();
    }

    @Nested
    @DisplayName("functional usage with primitive long values")
    class PrimitiveFunctional {

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               1,    I
                               5,    V
                               10,   X
                               50,   L
                               100,  C
                               500,  D
                               1000, M
                               """)
        @DisplayName("simple values")
        void simpleValues(long numericValue, String expectedStringValue) {

            var correctResult = correctResultCondition(expectedStringValue);
            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               2,    II
                               15,   XV
                               42,   XLII
                               123,  CXXIII
                               987,  CMLXXXVII
                               3999, MMMCMXCIX
                               """)
        @DisplayName("compound values")
        void compoundValues(long numericValue, String expectedStringValue) {

            var correctResult = correctResultCondition(expectedStringValue);
            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }

        @ParameterizedTest(name = "{0} is not allowed")
        @ValueSource(longs = {
                MIN_VALUE - 1, MIN_VALUE - 2, -934359934
        })
        @DisplayName("lower boundaries, i.e. < 0")
        void lowerBoundaries(long numericValue) {

            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(err);
        }

        @ParameterizedTest(name = "{0} is not allowed")
        @ValueSource(longs = {
                MAX_VALUE + 1, MAX_VALUE + 2, 2333048
        })
        @DisplayName("upper boundaries, i.e. > 3999")
        void upperBoundaries(long numericValue) {

            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(err);
        }

        @Test
        @DisplayName("functional stream demo")
        void functionalStreamDemo() {

            LongStream.rangeClosed(1, 10)
                      .mapToObj(new IntegerToRoman())
                      .map(Either::asOptionalLeft)
                      .flatMap(Optional::stream)
                      .forEach(System.out::println);
        }
    }

    @Nested
    @DisplayName("functional usage with wrapped Long values")
    class WrappedFunctional {

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               1,    I
                               5,    V
                               10,   X
                               50,   L
                               100,  C
                               500,  D
                               1000, M
                               """)
        @DisplayName("simple values")
        void simpleValues(Long numericValue, String expectedStringValue) {

            var correctResult = correctResultCondition(expectedStringValue);
            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               2,    II
                               15,   XV
                               42,   XLII
                               123,  CXXIII
                               987,  CMLXXXVII
                               3999, MMMCMXCIX
                               """)
        @DisplayName("compound values")
        void compoundValues(Long numericValue, String expectedStringValue) {

            var correctResult = correctResultCondition(expectedStringValue);
            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(ok)
                                                .is(correctResult);
        }

        @Test
        @DisplayName("null values")
        void nullValue() {

            assertThat(itor.apply(null)).isNotNull()
                                        .is(err);
        }

        @ParameterizedTest(name = "{0} is not allowed")
        @ValueSource(longs = {
                MIN_VALUE - 1, MIN_VALUE - 2, -934359934
        })
        @DisplayName("lower boundaries, i.e. < 0")
        void lowerBoundaries(Long numericValue) {

            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(err);
        }

        @ParameterizedTest(name = "{0} is not allowed")
        @ValueSource(longs = {
                MAX_VALUE + 1, MAX_VALUE + 2, 2333048
        })
        @DisplayName("upper boundaries, i.e. > 3999")
        void upperBoundaries(Long numericValue) {

            assertThat(itor.apply(numericValue)).isNotNull()
                                                .is(err);
        }
    }

    @Nested
    @DisplayName("imperative usage")
    class Imperative {

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               1,    I
                               5,    V
                               10,   X
                               50,   L
                               100,  C
                               500,  D
                               1000, M
                               """)
        @DisplayName("simple values")
        void simpleValues(long numericValue, String expectedStringValue) throws RomanNumeralException {

            assertThat(itor.convert(numericValue)).isEqualTo(expectedStringValue);
        }

        @ParameterizedTest(name = "{0} converts to {1}")
        @CsvSource(textBlock = """
                               2,    II
                               15,   XV
                               42,   XLII
                               123,  CXXIII
                               987,  CMLXXXVII
                               3999, MMMCMXCIX
                               """)
        @DisplayName("compound values")
        void compoundValues(long numericValue, String expectedStringValue) throws RomanNumeralException {

            assertThat(itor.convert(numericValue)).isEqualTo(expectedStringValue);
        }

        @ParameterizedTest(name = "{0} is not allowed")
        @ValueSource(longs = {
                MIN_VALUE - 1, MIN_VALUE - 2, -934359934
        })
        @DisplayName("lower boundaries, i.e. < 0")
        void lowerBoundaries(long numericValue) {

            assertThatExceptionOfType(RomanNumeralException.class)
                    .isThrownBy(() -> itor.convert(numericValue))
                    .withMessage(String.format("%d is too small", numericValue));
        }

        @ParameterizedTest(name = "{0} is not allowed")
        @ValueSource(longs = {
                MAX_VALUE + 1, MAX_VALUE + 2, 2333048
        })
        @DisplayName("upper boundaries, i.e. > 3999")
        void upperBoundaries(long numericValue) {

            assertThatExceptionOfType(RomanNumeralException.class)
                    .isThrownBy(() -> itor.convert(numericValue))
                    .withMessage(String.format("%d is too large", numericValue));
        }
    }

    @Nested
    @DisplayName("implementation details")
    class ImplDetails {

        @ParameterizedTest(name = "{0} resolves to atom value {1}")
        @CsvSource(textBlock = """
                               3999, 1000
                               1250, 1000
                               901,  900
                               837,  500
                               529,  500
                               250,  100
                               68,   50
                               50,   50
                               42,   40
                               37,   10
                               6,    5
                               1,    1
                               """)
        @DisplayName("extracting digits from numeric values")
        public void extractDigits(long value, long expectedDigit) {

            assertThat(IntegerToRoman.digitExtractor(value)).hasValue(expectedDigit);
        }

        @ParameterizedTest(name = "{0} resolves to empty value")
        @ValueSource(longs = { 0, -1 - 30 })
        @DisplayName("extracting digits from invalid values")
        public void extractDigitsFromInvalidValues(long value) {

            assertThat(IntegerToRoman.digitExtractor(value)).isEmpty();
        }

        @ParameterizedTest(name = "unfolding {0} to {1}")
        @MethodSource
        public void unfoldDigitsFromValue(long value, long[] expectedDigits) {

            long[] results = IntegerToRoman.unfold(value).toArray();
            assertThat(results).containsExactly(expectedDigits);
        }

        static Stream<Arguments> unfoldDigitsFromValue() {

            return Stream.of(
                    arguments(1021L, new long[]{ 1000, 10, 10, 1 }),
                    arguments(357L, new long[]{ 100, 100, 100, 50, 5, 1, 1 }),
                    arguments(42L, new long[]{ 40, 1, 1 }),
                    arguments(5L, new long[]{ 5 })
            );
        }
    }

    private MappedCondition<Either<String, RomanNumeralException>, String> correctResultCondition(String expectedStringValue) {

        return MappedCondition.mappedCondition(
                okResult,
                new Condition<>((res) -> res.equals(expectedStringValue), String.format("equals %s", expectedStringValue))
        );
    }
}

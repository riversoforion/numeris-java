package com.riversoforion.numeris;

import com.diffplug.common.base.Either;
import org.assertj.core.api.Condition;
import org.assertj.core.condition.MappedCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


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
    }

    private MappedCondition<Either<Long, RomanNumeralException>, Long> correctResultCondition(Long expectedNumericValue) {

        return MappedCondition.mappedCondition(
                okResult,
                new Condition<>((res) -> res.equals(expectedNumericValue), String.format("equals %s", expectedNumericValue))
        );
    }
}

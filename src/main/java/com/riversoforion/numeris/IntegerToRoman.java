package com.riversoforion.numeris;

import com.diffplug.common.base.Either;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.riversoforion.numeris.SharedConstants.MAX_VALUE;
import static com.riversoforion.numeris.SharedConstants.MIN_VALUE;


/**
 * Implements the conversion from a numeric ({@code long} value) to a Roman numeral. This converter supports two styles
 * of operation: functional & imperative. The functional style also supports primitive values (via the
 * {@code LongFunction} interface), and wrapped values (via the {@code Function} interface).
 * <p>
 * The following general rules apply to all conversion methods:
 * </p>
 * <ul>
 *     <li>Must not be {@code null}</li>
 *     <li>Must not be less than {@link com.riversoforion.numeris.RomanNumeral#MIN_VALUE}</li>
 *     <li>Must not be greater than {@link com.riversoforion.numeris.RomanNumeral#MAX_VALUE}</li>
 * </ul>
 * <p>
 * <em><strong>NOTE:</strong></em> This class is stateless and thread-safe.
 * </p>
 *
 * <h2>Functional Usage</h2>
 * <p>
 * The functional style supports inserting this converter into a stream pipeline:
 * </p>
 * <pre>
 *     List<String> romanNumerals = LongStream.rangeClosed(1, 10)
 *                                    .mapToObj(new IntegerToRoman())
 *                                    .map(Either::asOptionalLeft)
 *                                    .flatMap(Optional::stream)
 *                                    .collect(toList());
 * </pre>
 *
 * <h2>Imperative Usage</h2>
 * <p>
 * The imperative style supports "classic" Java code:
 * </p>
 * <pre>
 * try {
 *     String romanNumeral = new IntegerToRoman().convert(aNumber);
 * }
 * catch (RomanNumeralException e) {
 *     // handle it
 * }
 * </pre>
 */
public final class IntegerToRoman implements
        Function<Long, Either<String, RomanNumeralException>>,
        LongFunction<Either<String, RomanNumeralException>> {

    /**
     * Implementation of the {@link LongFunction functional interface}.
     *
     * @param numericValue The numeric value to convert
     * @return Either the Roman numeral as a string, or an exception describing why conversion failed
     */
    @Override
    public Either<String, RomanNumeralException> apply(long numericValue) {

        if (numericValue < MIN_VALUE) {
            return Either.createRight(RomanNumeralException.valueTooSmall(numericValue));
        }
        else if (numericValue > MAX_VALUE) {
            return Either.createRight(RomanNumeralException.valueTooLarge(numericValue));
        }

        return Either.createLeft(
                unfold(numericValue)
                        .mapToObj(Atom::symbolFromValue)
                        .flatMap(Optional::stream)
                        .collect(Collectors.joining(""))
        );
    }

    /**
     * Implementation of the {@link Function functional interface}.
     *
     * @param numericValue The numeric value to convert. Must not be {@code null}.
     * @return Either the Roman numeral as a string, or an exception describing why conversion failed
     */
    @Override
    public Either<String, RomanNumeralException> apply(Long numericValue) {

        if (numericValue == null) {
            return Either.createRight(RomanNumeralException.emptyValue());
        }
        return this.apply(numericValue.longValue());
    }

    /**
     * Converts the given numeric value to a Roman numeral.
     *
     * @param numericValue The numeric value to convert
     * @return The Roman numeral as a string
     * @throws RomanNumeralException If the value cannot be converted
     */
    public String convert(long numericValue) throws RomanNumeralException {

        var result = this.apply(numericValue);
        if (result.isRight()) {
            throw result.getRight();
        }
        return result.getLeft();
    }

    // Workaround for the lack of a good "unfold" implementation for Java (especially over primitive types).
    static LongStream unfold(long value) {

        return LongStream.of(value).mapMulti((remaining, consumer) -> {
            var result = digitExtractor(remaining);
            while (result.isPresent()) {
                var digit = result.getAsLong();
                consumer.accept(digit);
                remaining -= digit;
                result = digitExtractor(remaining);
            }
        });
    }

    // Pure function to find the largest Roman numeral digit that "fits" into the given value
    static OptionalLong digitExtractor(long numericValue) {

        if (numericValue <= 0) {
            return OptionalLong.empty();
        }
        long nextDigit = Atom.digits()
                             .filter((digit) -> numericValue >= digit)
                             .findFirst()
                             .orElse(1);
        return OptionalLong.of(nextDigit);
    }
}

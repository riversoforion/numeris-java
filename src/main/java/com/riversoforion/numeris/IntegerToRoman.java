package com.riversoforion.numeris;

import com.diffplug.common.base.Either;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.riversoforion.numeris.RomanNumeral.MAX_VALUE;
import static com.riversoforion.numeris.RomanNumeral.MIN_VALUE;


public class IntegerToRoman implements Function<Long, Either<String, RomanNumeralException>>, LongFunction<Either<String, RomanNumeralException>> {

    @Override
    public Either<String, RomanNumeralException> apply(long value) {

        if (value < MIN_VALUE) {
            return Either.createRight(RomanNumeralException.valueTooSmall(value));
        }
        else if (value > MAX_VALUE) {
            return Either.createRight(RomanNumeralException.valueTooLarge(value));
        }

        return Either.createLeft(
                unfold(value)
                        .mapToObj(Atom::symbolFromValue)
                        .flatMap(Optional::stream)
                        .collect(Collectors.joining(""))
        );
    }

    @Override
    public Either<String, RomanNumeralException> apply(Long numericValue) {

        if (numericValue == null) {
            return Either.createRight(RomanNumeralException.emptyValue());
        }
        return this.apply(numericValue.longValue());
    }

    public String convert(long numericValue) throws RomanNumeralException {

        var result = this.apply(numericValue);
        if (result.isRight()) {
            throw result.getRight();
        }
        return result.getLeft();
    }

    static OptionalLong digitExtractor(long value) {

        if (value <= 0) {
            return OptionalLong.empty();
        }
        long nextDigit = Atom.digits()
                             .filter((digit) -> value >= digit)
                             .findFirst()
                             .orElse(1);
        return OptionalLong.of(nextDigit);
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
}

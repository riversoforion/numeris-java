package com.riversoforion.numeris;

import com.diffplug.common.base.Either;

import java.util.function.Function;


public class RomanToInteger implements Function<String, Either<Long, RomanNumeralException>> {

    @Override
    public Either<Long, RomanNumeralException> apply(String romanValue) {

        return Either.createLeft(Atom.valueOf(romanValue).value());
    }

    public long convert(String romanValue) throws RomanNumeralException {

        var result = apply(romanValue);
        if (result.isLeft()) {
            return result.getLeft();
        }
        else {
            throw result.getRight();
        }
    }
}

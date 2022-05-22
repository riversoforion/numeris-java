package com.riversoforion.numeris;

import com.diffplug.common.base.Either;

import java.util.function.Function;


public class RomanToInteger implements Function<String, Either<Long, RomanNumeralException>> {

    @Override
    public Either<Long, RomanNumeralException> apply(String s) {

        return Either.createLeft(Atom.valueOf(s).value());
    }

    public long convert(String romanValue) throws RomanNumeralException {

        return 0L;
    }
}

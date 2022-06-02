package com.riversoforion.numeris;

import com.diffplug.common.base.Either;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.LongStream;


public class RomanToInteger implements Function<String, Either<Long, RomanNumeralException>> {

    private static final Pattern VALID_SYMBOLS = Pattern.compile("^[IVXLCDM]+$");

    @Override
    public Either<Long, RomanNumeralException> apply(String romanValue) {

        String normalized = normalizeNumeralValue(romanValue);
        Optional<RomanNumeralException> validationResult = checkNumeralValue(normalized);
        if (validationResult.isPresent()) {
            return Either.createRight(validationResult.get());
        }
        Either<long[], RomanNumeralException> decomposed = decomposeNumeral(normalized);
        return decomposed.mapLeft(digits -> LongStream.of(digits).sum());
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

    private String normalizeNumeralValue(String romanValue) {

        if (romanValue == null) {
            return "";
        }
        return romanValue.trim().toUpperCase(Locale.ROOT);
    }

    private Optional<RomanNumeralException> checkNumeralValue(String romanValue) {

        if (romanValue.isEmpty()) {
            return Optional.of(RomanNumeralException.emptyValue());
        }
        if (!VALID_SYMBOLS.matcher(romanValue).matches()) {
            return Optional.of(RomanNumeralException.unparseable(romanValue));
        }
        return Optional.empty();
    }

    private Either<long[], RomanNumeralException> decomposeNumeral(String romanValue) {

        final ParseState parseState = new ParseState(romanValue);
        final List<Long> numbers = new LinkedList<>();
        while (!parseState.isParsingComplete()) {
            final Atom current = parseState.currentNumeral();
            if (parseState.remaining().startsWith(current.name())) {
                numbers.add(current.value());
                parseState.removeCurrent();
                if (!current.allowMultiples()) {
                    parseState.advanceNumeral();
                }
            }
            else {
                parseState.advanceNumeral();
            }
        }
        if (!parseState.remaining().isEmpty()) {
            return Either.createRight(RomanNumeralException.unparseable(romanValue));
        }
        return Either.createLeft(numbers.stream().mapToLong(Long::longValue).toArray());
    }

    private static class ParseState {

        private final Deque<Atom> remainingNumerals;
        private String remainingToParse;

        ParseState(String romanValue) {

            this.remainingNumerals = new ArrayDeque<>(Arrays.asList(Atom.values()));
            this.remainingToParse = romanValue;
        }

        String remaining() {

            return this.remainingToParse;
        }

        Atom currentNumeral() {

            return this.remainingNumerals.peekFirst();
        }

        void advanceNumeral() {

            this.remainingNumerals.pop();
        }

        boolean isParsingComplete() {

            return this.remainingNumerals.isEmpty();
        }

        void removeCurrent() {

            this.remainingToParse = this.remainingToParse.substring(this.currentNumeral().name().length());
        }
    }
}

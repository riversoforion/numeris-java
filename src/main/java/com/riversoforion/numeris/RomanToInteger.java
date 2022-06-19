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


/**
 * Implements the conversion from a Roman numeral to a numeric ({@code long}) value. This converter supports two
 * styles of operation: functional & imperative. The functional style only supports wrapped values (via the
 * {@code Function} interface).
 * <p>
 * The following general rules apply to all conversion methods:
 * </p>
 * <ul>
 *     <li>Must not be {@code null}</li>
 *     <li>Must not be an empty or blank string</li>
 *     <li>Must not have any characters that are not valid Roman numerals (other than whitespace): {@code IVXLCDM}</li>
 *     <li>Must not convert to a value less than {@link com.riversoforion.numeris.RomanNumeral#MIN_VALUE}</li>
 *     <li>Must not convert to a value greater than {@link com.riversoforion.numeris.RomanNumeral#MAX_VALUE}</li>
 *     <li>May have leading and/or trailing spaces</li>
 *     <li>May be any combination of upper and lower characters</li>
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
 * List<Long> numbers = Stream.of("XII", "V", "CMLV")
 *         .map(new RomanToInteger())
 *         .map(Either::getLeft)
 *         .toList();
 *
 * long[] longs = Stream.of("XII", "V", "BBCCDD", "CMLV")
 *                      .map(new RomanToInteger())
 *                      .filter(Either::isLeft)
 *                      .mapToLong(Either::getLeft)
 *                      .toArray();
 * </pre>
 * <p>
 * Note that, unlike {@code IntegerToRoman}, this class only supports boxed
 * {@code Long} values, not primitive {@code longs}.
 * </p>
 *
 * <h2>Imperative Usage</h2>
 * <p>
 * The imperative style supports "classic" Java code:
 * </p>
 * <pre>
 * try {
 *     long numericValue = new RomanToInteger().convert("XII");
 * }
 * catch (RomanNumeralException e) {
 *     // handle it
 * }
 * </pre>
 */
public class RomanToInteger implements Function<String, Either<Long, RomanNumeralException>> {

    private static final Pattern VALID_SYMBOLS = Pattern.compile("^[CDILMVX]+$");

    /**
     * Implementation of the {@link Function functional interface}.
     *
     * @param romanValue The Roman numeral to convert
     * @return Either the numeric value of the Roman numeral, or an exception describing why the conversion failed
     */
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

    /**
     * Converts the given Roman numeral to a numeric value.
     *
     * @param romanValue The Roman numeral to convert
     * @return The numeric value of the Roman numeral
     * @throws RomanNumeralException If the numeral cannot be converted
     */
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

    // Parses the Roman numeral value into its corresponding digit values
    private Either<long[], RomanNumeralException> decomposeNumeral(String romanValue) {

        final ParseState parseState = new ParseState(romanValue);
        final List<Long> numbers = new LinkedList<>();
        while (!parseState.isParsingComplete()) {
            final Atom current = parseState.currentNumeral();
            if (parseState.remaining().startsWith(current.name())) {
                numbers.add(current.value());
                parseState.removeCurrent();
                if (!current.allowsMultiples()) {
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

    /**
     * Holds the state of a Roman numeral parsing operation.
     */
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

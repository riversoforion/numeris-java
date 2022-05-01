package com.riversoforion.numeris;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.LongStream;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter(AccessLevel.PACKAGE)
enum Atom {

    M(1000, 3),
    CM(900, 1),
    D(500, 1),
    CD(400, 1),
    C(100, 3),
    XC(90, 1),
    L(50, 1),
    XL(40, 1),
    X(10, 3),
    IX(9, 1),
    V(5, 1),
    IV(4, 1),
    I(1, 3);

    private static final Map<Long, String> valuesToSymbols = new LinkedHashMap<>();

    static {
        Arrays.stream(Atom.values()).forEach((atom) -> Atom.valuesToSymbols.put(atom.value, atom.name()));
    }

    private final long value;
    private final int maxGroup;

    static LongStream digits() {

        return Arrays.stream(Atom.values()).mapToLong(Atom::value);
    }

    static Optional<String> symbolFromValue(long value) {

        if (Atom.valuesToSymbols.containsKey(value)) {
            return Optional.of(Atom.valuesToSymbols.get(value));
        }
        else {
            return Optional.empty();
        }
    }
}

package com.riversoforion.numeris;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
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

    private final long value;
    private final int maxGroup;
}

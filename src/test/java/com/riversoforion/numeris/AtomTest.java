package com.riversoforion.numeris;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.riversoforion.numeris.Atom.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class AtomTest {

    @Test
    public void testIteration() {

        var iter = Arrays.stream(values()).iterator();
        assertEquals(M, iter.next());
        assertEquals(CM, iter.next());
        assertEquals(D, iter.next());
        assertEquals(CD, iter.next());
        assertEquals(C, iter.next());
        assertEquals(XC, iter.next());
        assertEquals(L, iter.next());
        assertEquals(XL, iter.next());
        assertEquals(X, iter.next());
        assertEquals(IX, iter.next());
        assertEquals(V, iter.next());
        assertEquals(IV, iter.next());
        assertEquals(I, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testRandomValues() {

        // M
        assertEquals(M.value(), 1000);
        assertEquals(M.maxGroup(), 3);
        // CD
        assertEquals(CD.value(), 400);
        assertEquals(CD.maxGroup(), 1);
        // IX
        assertEquals(IX.value(), 9);
        assertEquals(IX.maxGroup(), 1);
        // V
        assertEquals(V.value(), 5);
        assertEquals(V.maxGroup(), 1);
        // I
        assertEquals(I.value(), 1);
        assertEquals(I.maxGroup(), 3);
    }
}

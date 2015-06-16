package ru.eventflow.ccg.data;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertTrue;

public class BitSetFormResolverTest {

    @Test
    public void testSmth() {
        BitSet bitset1 = new BitSet(5);
        bitset1.set(0);
        System.out.println(bitset1.toString());

        BitSet bitset2 = new BitSet(5);
        bitset2.set(3);
        System.out.println(bitset2.toString());

        bitset1.or(bitset2);
        System.out.println(bitset1);

        // assertTrue(bitset1.equals(bitset2));
    }
}

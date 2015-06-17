package ru.eventflow.ccg.data;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class BitSetFormResolverTest {

    @Test
    public void testSmth() {
        ExportableBitSet bitset1 = new ExportableBitSet(5);
        bitset1.set(0);
        bitset1.set(10);
        bitset1.set(12);
        bitset1.set(27);
        byte[] bytes = bitset1.toByteArray();
        ExportableBitSet bs2 = new ExportableBitSet(bytes);
        System.out.println(bs2.toString());

        assertEquals(bs2, bitset1);

        BitSet bitset2 = new BitSet(5);
        bitset2.set(3);
        System.out.println(bitset2.toString());

        bitset1.or(bitset2);
        System.out.println(bitset1);

        // assertTrue(bitset1.equals(bitset2));
    }
}


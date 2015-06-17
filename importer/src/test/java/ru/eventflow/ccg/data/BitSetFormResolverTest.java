package ru.eventflow.ccg.data;

import org.junit.Test;
import ru.eventflow.ccg.data.corpus.BitSetFormResolver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testResolver() throws SQLException {
        final String url = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
        Connection conn = DriverManager.getConnection(url);
        BitSetFormResolver resolver = new BitSetFormResolver(conn);
        List<String> grammemes = new ArrayList<>();
        grammemes.add("NUMR");
        grammemes.add("accs");
        int id = resolver.resolve(79531, grammemes); // "девять"
        assertTrue(id != -1);
        System.out.println(id);
    }
}


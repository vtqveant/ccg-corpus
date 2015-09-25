package ru.eventflow.ccg.data;

import org.junit.Test;
import ru.eventflow.ccg.data.corpus.SQLDataBridge;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormResolverTest {

    @Test
    public void testSmth() {
        BitSet bitset1 = new BitSet(5);
        bitset1.set(0);
        bitset1.set(10);
        bitset1.set(12);
        bitset1.set(27);
        byte[] bytes = bitset1.toByteArray();
        BitSet bs2 = BitSet.valueOf(bytes);
        System.out.println(bs2.toString());

        assertEquals(bs2, bitset1);
    }

//    @Test
    public void testResolver() throws SQLException {
        final String url = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
        SQLDataBridge bridge = new SQLDataBridge(url);
        List<String> grammemes = new ArrayList<>();
        grammemes.add("NUMR");
        grammemes.add("accs");
        int id = bridge.resolve(79531, grammemes); // "девять"
        assertTrue(id != -1);
        System.out.println(id);
    }
}


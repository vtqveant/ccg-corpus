package ru.eventflow.unification;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnificationTest {

    private Node q2;
    private Node q1;
    private Node q0;

    /**
     * [F [1][H sg]]
     * [G [1]      ]
     */
    @Before
    public void setUp() {
        q2 = new Node();
        q2.label = "sg";

        q1 = new Node();
        q1.features.put("H", q2);

        q0 = new Node();
        q0.features.put("F", q1);
        q0.features.put("G", q1);
    }

    @Test
    public void testSink() {
        assertTrue(Unification.isSink(q2));
        assertFalse(Unification.isSink(q1));
    }

    @Test
    public void testLabels() {
        assertTrue(Unification.isLabeled(q2));
        assertFalse(Unification.isLabeled(q1));
    }

    @Test
    public void testUnify() {
        // [1][F [1]]
        Node q4 = new Node();
        q4.features.put("F", q4);

        Node result = Unification.unify(q0, q4);
        System.out.println(result);

        //    [F [1]]
        // [1][G [1]]
        //    [H sg ]
        assertEquals(3, result.features.size());
        assertEquals(result, result.clazz);
        assertEquals(result, result.features.get("F"));
        assertEquals(result, result.features.get("G"));
        assertEquals("sg", result.features.get("H").label);
    }

    @Test
    public void testUnificationCommutative() {
        // [1][F [1]]
        Node q4 = new Node();
        q4.features.put("F", q4);

        Node result = Unification.unify(q4, q0);
        System.out.println(result);

        //    [F [1]]
        // [1][G [1]]
        //    [H sg ]
        assertEquals(3, result.features.size());
        assertEquals(result, result.clazz);
        assertEquals(result, result.features.get("F"));
        assertEquals(result, result.features.get("G"));
        assertEquals("sg", result.features.get("H").label);
    }

    @Test
    public void testContradiction() {
        // [F sg]
        Node q0_0 = new Node();
        q0_0.label = "sg";
        Node q0_1 = new Node();
        q0_1.features.put("F", q0_0);

        // [F pl]
        Node q0_2 = new Node();
        q0_2.label = "pl";
        Node q0_3 = new Node();
        q0_3.features.put("F", q0_2);

        Node result = Unification.unify(q0_1, q0_3);
        assertNull(result);
    }

    /**
     * [] = $\top$ must unify with anything except contradiction
     */
    @Test
    public void testTop() {
        Node top = new Node();

        Node q0 = new Node();
        q0.label = "a";

        Node result = Unification.unify(top, q0);
        assertNotNull(result);
    }

    /**
     * unifying anything with contradiction is contradiction
     */
    @Test
    public void testBottom() {
        Node top = new Node();
        Node result = Unification.unify(top, Unification.CONTRADICTION);
        assertEquals(Unification.CONTRADICTION, result);
    }

}

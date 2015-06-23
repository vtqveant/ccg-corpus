package ru.eventflow.unification;

import org.junit.Test;

public class HLDSUnificationTest {

    @Test
    public void testUnifyFormulae() {
        Node q2 = new Node();
        q2.label = "sg";

        Node q1 = new Node();
        q1.features.put("H", q2);

        Node q0 = new Node();
        q0.features.put("F", q1);
        q0.features.put("G", q1);
        System.out.println(q0);

        Node q4 = new Node();
        q4.features.put("F", q4);
        System.out.println(q4);

        Node result = Unification.unify(q0, q4);
        System.out.println(result);
    }
}

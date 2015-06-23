package ru.eventflow.unification;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Test;
import ru.eventflow.modelchecker.HLFormula;
import ru.eventflow.modelchecker.HLFormulaLexer;
import ru.eventflow.modelchecker.HLFormulaParser;

public class HLDSUnificationTest {

    @Test
    public void testHybridLogic() throws RecognitionException {
        Node q2 = new Node();
        q2.label = "sg";

        Node q1 = new Node();
        q1.features.put("H", q2);

        Node q0 = new Node();
        q0.features.put("F", q1);
        q0.features.put("G", q1);

        // @q0(<F>(q1) & <G>(q1 & <H>(q2 & sg)))
        System.out.println(q0.toString());

        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream(q0.toString()));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HLFormulaParser parser = new HLFormulaParser(tokens);
        HLFormula hlFormula = parser.expression();

        System.out.println(hlFormula.toString());
    }

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

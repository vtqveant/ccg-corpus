package ru.eventflow.parser;

import org.junit.Test;
import ru.eventflow.ccg.parser.*;
import ru.eventflow.ccg.parser.rules.BackwardApplication;
import ru.eventflow.ccg.parser.rules.ForwardApplication;
import ru.eventflow.ccg.parser.rules.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CCGTest {

    @Test
    public void testRules() {
        SynCat s = CategoryBuilder.build("S");

        Rule rule = ForwardApplication.getInstance();
        rule.setLeft(CategoryBuilder.build("s/np"));
        rule.setRight(CategoryBuilder.build("np"));
        assertTrue(rule.apply().equals(s));

        rule = BackwardApplication.getInstance();
        rule.setLeft(CategoryBuilder.build("np"));
        rule.setRight(CategoryBuilder.build("s\\np"));
        assertTrue(rule.apply().equals(s));

        rule = ForwardApplication.getInstance();
        rule.setLeft(CategoryBuilder.build("np"));
        rule.setRight(CategoryBuilder.build("s\\np"));
        assertNull(rule.apply());
    }

    @Test
    public void testLexicon() {
        Set<Entry> lexicon = new HashSet<Entry>();
        lexicon.add(new Entry("john", new SynCat("np")));
        lexicon.add(new Entry("mary", new SynCat("np")));
        lexicon.add(new Entry("loves", CategoryBuilder.build("(s\\np)/np")));
    }

    @Test
    public void testCGGParser() {
        String sentence = "John loves Mary";

        List<Tok> tokens = new ArrayList<Tok>();
        for (String s : sentence.split("\\s+")) {
            tokens.add(new Tok(s.toLowerCase()));
        }

        Set<Entry> lexicon = new HashSet<Entry>();
        SynCat np = CategoryBuilder.build("np");
        lexicon.add(new Entry("john", np));
        lexicon.add(new Entry("mary", np));
        lexicon.add(new Entry("loves", CategoryBuilder.build("(s\\np)/np")));

        Parser parser = new Parser(lexicon);
        List<Item> parses = parser.parse(tokens);
        Utils.print(parses);
    }

    @Test
    public void testCompareItems() {
        SynCat s = new SynCat("S");
        SynCat n = new SynCat("n");

        SynCat manual = CategoryBuilder.normalize(new SynCat(s, new SynCat(s, n, true), false));
        SynCat auto = CategoryBuilder.build("s\\(s/n)");

        assertEquals(manual, auto);
    }

}

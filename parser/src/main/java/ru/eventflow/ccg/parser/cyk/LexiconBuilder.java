package ru.eventflow.ccg.parser.cyk;

import java.util.HashSet;
import java.util.Set;

public class LexiconBuilder {
    public static Set<Entry> getLexicon() {
        Set<Entry> lexicon = new HashSet<Entry>();
        lexicon.add(entry("john", "np"));
        lexicon.add(entry("mary", "np"));
        lexicon.add(entry("bob", "np"));
        lexicon.add(entry("peter", "np"));
        lexicon.add(entry("and", "(n\\n)/n"));
        lexicon.add(entry("and", "(s\\s)/s"));
        lexicon.add(entry("and", "(np\\np)/np"));
        lexicon.add(entry("loves", "(s\\np)/np"));
        lexicon.add(entry("love", "(s\\np)/np"));
        lexicon.add(entry("hates", "(s\\np)/np"));
        lexicon.add(entry("saw", "(s\\np)/np"));
        lexicon.add(entry("man", "n"));
        lexicon.add(entry("woman", "n"));
        lexicon.add(entry("that", "(n\\n)/(s/np)"));
        return lexicon;
    }

    private static Entry entry(String s, String cat) {
        return new Entry(s, CategoryBuilder.build(cat));
    }


}

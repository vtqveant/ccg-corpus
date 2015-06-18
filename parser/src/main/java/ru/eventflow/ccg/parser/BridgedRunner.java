package ru.eventflow.ccg.parser;

import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.DataManagerImpl;
import ru.eventflow.ccg.datasource.DataSource;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.corpus.Variant;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.parser.cyk.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BridgedRunner {

    public static void main(String[] args) {
        DataManager dataManager = new DataManagerImpl(DataSource.DEFAULT);
        Sentence sentence = dataManager.getSentenceById(1);

        System.out.println(sentence.getSource());

        List<T> ts = new ArrayList<>();
        Set<Entry> lexicon = new HashSet<>();
        for (Token token : sentence.getTokens()) {
            ts.add(new T(token.getOrthography().toLowerCase()));

            for (Variant variant : token.getVariants()) {
                Form form = variant.getForm();
                for (Entry entry : map(form)) {
                    lexicon.add(entry);
                }
            }
        }

        List<Item> parses = new Parser(lexicon).parse(ts);

        System.out.println(sentenceToString(ts));
        System.out.println(parses.size() + " parse(s)");
        Utils.print(parses);
    }

    private static List<Entry> map(Form form) {
        List<String> grammemes = new ArrayList<>();
        for (Grammeme grammeme : form.getGrammemes()) {
            grammemes.add(grammeme.getName());
        }
        for (Grammeme grammeme : form.getLexeme().getLemma().getGrammemes()) {
            grammemes.add(grammeme.getName());
        }

        List<String> cats = new ArrayList<>();
        if (grammemes.contains("NOUN")) {
            cats.add("n");         // школа
        }
        if (grammemes.contains("NOUN") && grammemes.contains("gent")) {
            cats.add("n\\n");      // (школа) злословия
        }
        if (grammemes.contains("ADJF")) {
            cats.add("n/n");
        }
        if (grammemes.contains("VERB") && grammemes.contains("tran")) {
            cats.add("(s\\n)/np"); // (мальчик) читает (книгу)
            cats.add("(s\\n)/s");  // учит (прикусить язык)
        }
        if (grammemes.contains("INFN")) {
            cats.add("s/n");
        }
        if (grammemes.contains("PNCT")) {
            cats.add("n/n");       // << школа
            cats.add("n\\n");      // школа_злословия >>
            cats.add("(n\\n)/n");  // шпаги , деньги
        }
        if (grammemes.contains("CONJ")) {
            cats.add("(n\\n)/n");  // шпаги и деньги
        }

        List<Entry> entries = new ArrayList<>();
        for (String cat : cats) {
            entries.add(new Entry(form.getOrthography().toLowerCase(), CategoryBuilder.build(cat)));
        }
        return entries;
    }

    private static String sentenceToString(List<T> ts) {
        StringBuilder sb = new StringBuilder();
        for (T t : ts) {
            sb.append(t.getValue());
            sb.append(" ");
        }
        return sb.toString();
    }
}

package ru.eventflow.ccg.parser;

import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.DataManagerImpl;
import ru.eventflow.ccg.datasource.DataSource;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.corpus.Variant;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Example parser with manual mapping of grammmemes to syntactic categories
 */
public class BridgedRunner {

    public static void main(String[] args) {
        DataManager dataManager = new DataManagerImpl(DataSource.DEFAULT);
        Sentence sentence = dataManager.getSentenceById(1);

        List<Tok> toks = new ArrayList<>();
        Set<Entry> lexicon = new HashSet<>();
        for (Token token : sentence.getTokens()) {
            toks.add(new Tok(token.getOrthography().toLowerCase()));

            // build lexicon
            for (Variant variant : token.getVariants()) {
                int formId = variant.getForm().getId();
                for (SynCat cat : Mapper.map(formId)) {
                    lexicon.add(new Entry(variant.getForm().getOrthography().toLowerCase(), cat));
                }
            }
        }

        List<Item> parses = new Parser(lexicon).parse(toks);

        System.out.println(sentence.getSource());
        System.out.println(parses.size() + " parse(s)");
        Utils.print(parses);
    }
}

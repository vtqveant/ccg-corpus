package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import java.util.List;

/**
 * A model for lexicon tree table representing a morphological form
 * (i.e. all categories plus unassigned)
 */
public class LexiconEntry extends AbstractLexiconEntry {

    public LexiconEntry(Form form, List<String> grammemes, int count) {
        super(form, grammemes, count);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String grammeme : grammemes) {
            sb.append(grammeme);
            sb.append(".");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public Category getCategory() {
        return null;
    }
}

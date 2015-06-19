package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractLexiconEntry {
    protected Form form;
    protected int count;
    protected List<String> grammemes = new ArrayList<>();

    public AbstractLexiconEntry(Form form, List<String> grammemes, int count) {
        this.count = count;
        this.form = form;
        this.grammemes.addAll(grammemes);
        Collections.sort(this.grammemes); // TODO
    }

    public Form getForm() {
        return form;
    }

    public String getLemma() {
        return form.getLexeme().getLemma().getOrthography();
    }

    public int getCount() {
        return count;
    }

    public List<String> getGrammemes() {
        return grammemes;
    }

    public abstract Category getCategory();

}

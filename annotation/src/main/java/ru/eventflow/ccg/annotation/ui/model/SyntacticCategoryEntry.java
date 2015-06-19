package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;

public class SyntacticCategoryEntry extends LexiconEntry {

    private String name;

    public SyntacticCategoryEntry(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public Form getForm() {
        return null;
    }

    @Override
    public String getLemma() {
        return "";
    }

    @Override
    public String toString() {
        return name;
    }
}

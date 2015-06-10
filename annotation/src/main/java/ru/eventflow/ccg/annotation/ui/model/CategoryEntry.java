package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;

public class CategoryEntry extends LexiconEntry {

    private String category;

    public CategoryEntry(String category, int count) {
        this.category = category;
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
        return category;
    }
}

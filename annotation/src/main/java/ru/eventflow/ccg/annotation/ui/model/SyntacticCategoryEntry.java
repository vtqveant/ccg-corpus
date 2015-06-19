package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import java.util.List;

public class SyntacticCategoryEntry extends LexiconEntry {

    private Category category;

    public SyntacticCategoryEntry(Form form, List<String> grammemes, Category category, int count) {
        super(form, grammemes, count);
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String getLemma() {
        return "";
    }

    @Override
    public String toString() {
        return category.getName();
    }
}

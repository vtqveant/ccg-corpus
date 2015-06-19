package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import java.util.List;

/**
 * A model for lexicon tree table representing a syntactic category
 */
public class CategoryEntry extends AbstractLexiconEntry {

    private Category category;

    public CategoryEntry(Form form, List<String> grammemes, Category category, int count) {
        super(form, grammemes, count);
        this.category = category;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return category.getName();
    }
}

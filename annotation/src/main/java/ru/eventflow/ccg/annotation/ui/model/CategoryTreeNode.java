package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;

public class CategoryTreeNode extends LexiconTreeNode {

    private String category;

    public CategoryTreeNode(String category, int count) {
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

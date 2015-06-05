package ru.eventflow.ccg.annotation.ui.model;

public class SyntacticCategoryTreeNode extends LexiconTreeNode {

    private String category;

    public SyntacticCategoryTreeNode(String category, int count) {
        this.category = category;
        this.count = count;
    }

    @Override
    public String getForm() {
        return "";
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

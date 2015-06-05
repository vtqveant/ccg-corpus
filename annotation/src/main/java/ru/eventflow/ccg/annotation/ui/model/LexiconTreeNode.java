package ru.eventflow.ccg.annotation.ui.model;

import java.util.ArrayList;
import java.util.List;

public class LexiconTreeNode {
    protected String form;
    protected String lemma;
    protected int count;
    protected java.util.List<String> grammemes = new ArrayList<>();
    protected java.util.List<LexiconTreeNode> children = new ArrayList<>();
    protected boolean leaf = false;

    public LexiconTreeNode() {
    }

    public LexiconTreeNode(String form, String lemma, java.util.List<String> grammemes, int count) {
        this.count = count;
        this.form = form;
        this.lemma = lemma;
        this.grammemes.addAll(grammemes);
    }

    public String getForm() {
        return form;
    }

    public String getLemma() {
        return lemma;
    }

    public List<LexiconTreeNode> getChildren() {
        return children;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isLeaf() {
        return children.size() == 0 && leaf;
    }

    public int getCount() {
        return count;
    }

    public List<String> getGrammemes() {
        return grammemes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String grammeme : grammemes) {
            sb.append(grammeme);
            sb.append(", ");
        }
        if (grammemes.size() > 0) sb.delete(sb.length() - 2, sb.length() - 1);
        return sb.toString();
    }
}

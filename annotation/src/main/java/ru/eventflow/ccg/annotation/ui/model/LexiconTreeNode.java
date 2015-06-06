package ru.eventflow.ccg.annotation.ui.model;

import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.util.ArrayList;
import java.util.List;

public class LexiconTreeNode {
    protected Form form;
    protected int count;
    protected List<String> grammemes = new ArrayList<>();
    protected List<LexiconTreeNode> children = new ArrayList<>();
    protected boolean leaf = false;

    public LexiconTreeNode() {
    }

    public LexiconTreeNode(Form form, List<String> grammemes, int count) {
        this.count = count;
        this.form = form;
        this.grammemes.addAll(grammemes);
    }

    public Form getForm() {
        return form;
    }

    public String getLemma() {
        return form.getLexeme().getLemma().getOrthography();
    }

    public List<LexiconTreeNode> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.size() == 0 && leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
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

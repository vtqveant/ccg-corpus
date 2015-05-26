package ru.eventflow.ccg.datasource.model.disambig;

import java.util.ArrayList;
import java.util.List;

public class Variant {

    private int id;

    private String lemma;

    private int lemmaId;

    private List<String> grammemes = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public int getLemmaId() {
        return lemmaId;
    }

    public void setLemmaId(int lemmaId) {
        this.lemmaId = lemmaId;
    }

    public List<String> getGrammemes() {
        return grammemes;
    }

    public void addGrammeme(String grammeme) {
        grammemes.add(grammeme);
    }
}

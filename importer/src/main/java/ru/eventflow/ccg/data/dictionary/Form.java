package ru.eventflow.ccg.data.dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Lemma is a special kind of form, it contains a normal form orphograhy
 * and a set of grammemes common to all other forms.
 */
public class Form {

    private List<String> grammemes = new ArrayList<String>();

    private String orthography;

    public List<String> getGrammemes() {
        return grammemes;
    }

    protected void addGrammeme(String grammeme) {
        grammemes.add(grammeme);
    }

    public String getOrthography() {
        return orthography;
    }

    protected void setOrthography(String orthography) {
        this.orthography = orthography;
    }
}

package ru.eventflow.ccg.parser;

/**
 * a lexicon entry
 */
public class Entry {
    private String orthography;
    private SynCat synCat;

    public Entry(String orthography, SynCat synCat) {
        this.orthography = orthography;
        this.synCat = synCat;
    }

    public String getOrthography() {
        return orthography;
    }

    public SynCat getSynCat() {
        return synCat;
    }
}


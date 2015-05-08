package ru.eventflow.ccg.parser;

/**
 * a lexicon entry
 */
public class Entry {
    private String orthography;
    private Category category;

    public Entry(String orthography, Category category) {
        this.orthography = orthography;
        this.category = category;
    }

    public String getOrthography() {
        return orthography;
    }

    public Category getCategory() {
        return category;
    }
}


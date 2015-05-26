package ru.eventflow.ccg.datasource.model.disambig;

import java.util.ArrayList;
import java.util.List;

public class Token {

    private List<Variant> variants = new ArrayList<>();

    private int id;

    private String orthography;

    public List<Variant> getVariants() {
        return variants;
    }

    public void addRevision(Variant variant) {
        variants.add(variant);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrthography() {
        return orthography;
    }

    public void setOrthography(String orthography) {
        this.orthography = orthography;
    }

}

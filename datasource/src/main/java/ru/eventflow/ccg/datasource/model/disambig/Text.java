package ru.eventflow.ccg.datasource.model.disambig;

import java.util.ArrayList;
import java.util.List;

public class Text {

    private List<String> tags = new ArrayList<>();

    private List<Paragraph> paragraphs = new ArrayList<>();

    private int id;

    private int parent;

    String name;

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void addParagraph(Paragraph paragraph) {
        paragraphs.add(paragraph);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }
}

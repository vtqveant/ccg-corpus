package ru.eventflow.annotation.model;

public class Document {
    private int id;
    private String url;
    private String text;

    public Document(int id, String url, String text) {
        this.id = id;
        this.url = url;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

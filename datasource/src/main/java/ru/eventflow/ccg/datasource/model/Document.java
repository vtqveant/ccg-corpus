package ru.eventflow.ccg.datasource.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "document")
public class Document {

    @Id
    private int id;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    public Document() {
    }

    public Document(int id, String url, String text) {
        this.url = url;
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

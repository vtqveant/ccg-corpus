package ru.eventflow.ccg.datasource.model.corpus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "corpus", name = "document")
public class Document {

    @Id
    private int id;

    @Column(name = "parent_id")
    private int parentId;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    public Document() {
    }

    public Document(int id, String name, int parentId, String url, String text) {
        this.url = url;
        this.text = text;
        this.id = id;
        this.parentId = parentId;
        this.name = name;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

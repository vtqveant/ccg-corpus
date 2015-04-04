package ru.eventflow.fts.datasource;

import javax.persistence.*;

@Entity
@Table(name = "document")
public class Document {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * CLOB is for compatibility with H2
     */
    @Column(name = "url", columnDefinition = "CLOB")
    @Lob
    private String url;

    @Column(name = "text", columnDefinition = "CLOB")
    @Lob
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

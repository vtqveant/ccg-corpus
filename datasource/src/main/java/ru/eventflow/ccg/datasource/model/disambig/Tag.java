package ru.eventflow.ccg.datasource.model.disambig;

import javax.persistence.*;

@Entity
@Table(schema = "corpus", name = "tag")
public class Tag {

    @Id
    @Column(columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "source", columnDefinition = "TEXT")
    private String value;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "text_id", nullable = false)
    private Text text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}

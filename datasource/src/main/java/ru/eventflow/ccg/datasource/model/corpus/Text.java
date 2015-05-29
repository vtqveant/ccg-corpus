package ru.eventflow.ccg.datasource.model.corpus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "corpus", name = "text")
public class Text {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(targetEntity = Tag.class, mappedBy = "text", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(targetEntity = Paragraph.class, mappedBy = "text", cascade = CascadeType.ALL)
    private List<Paragraph> paragraphs = new ArrayList<>();

    @ManyToOne(targetEntity = Text.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", nullable = true)
    private Text parent;

    public void addTag(Tag tag) {
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

    public Text getParent() {
        return parent;
    }

    public void setParent(Text parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }
}

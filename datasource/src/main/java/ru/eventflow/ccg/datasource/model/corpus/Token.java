package ru.eventflow.ccg.datasource.model.corpus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "corpus", name = "token")
public class Token {

    @Id
    private int id;

    @OneToMany(targetEntity = Variant.class, mappedBy = "token", cascade = CascadeType.ALL)
    private List<Variant> variants = new ArrayList<>();

    @Column(name = "orthography")
    private String orthography;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sentence_id", nullable = false)
    private Sentence sentence;

    @Column(name = "revision")
    private int revision;

    @Column(name = "pos")
    private int position;

    public List<Variant> getVariants() {
        return variants;
    }

    public void addVariant(Variant variant) {
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

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

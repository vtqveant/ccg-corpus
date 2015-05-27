package ru.eventflow.ccg.datasource.model.disambig;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "corpus", name = "token")
public class Token {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @OneToMany(targetEntity = Variant.class, mappedBy = "token", cascade = CascadeType.ALL)
    private List<Variant> variants = new ArrayList<>();

    @Column(name = "orthography")
    private String orthography;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sentence_id", nullable = false)
    private Sentence sentence;

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

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }
}

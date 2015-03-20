package ru.eventflow.fts.datasource;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "query")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "orthography")
    private String orthography;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(targetEntity = Assessment.class, cascade = CascadeType.MERGE)
    private Set<Assessment> assessments = new HashSet<Assessment>();

    public Query() {
    }

    public Query(String orthography, String description) {
        this.orthography = orthography;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(Set<Assessment> assessments) {
        this.assessments = assessments;
    }
}

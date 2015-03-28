package ru.eventflow.fts.score;

import java.util.ArrayList;
import java.util.List;

public class Result implements Comparable<Result> {

    private String request;
    private List<Integer> documents = new ArrayList<>();

    public Result(String request, List<Integer> documents) {
        this.request = request.trim();
        this.documents = documents;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public List<Integer> getDocuments() {
        return documents;
    }

    public void addDocument(Integer document) {
        documents.add(document);
    }

    public void setDocuments(List<Integer> documents) {
        this.documents = documents;
    }

    @Override
    public int compareTo(Result o) {
        return request.compareTo(o.getRequest());
    }
}
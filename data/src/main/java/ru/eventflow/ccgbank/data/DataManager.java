package ru.eventflow.ccgbank.data;

import ru.eventflow.ccgbank.model.Document;

import java.util.List;

public interface DataManager {
    List<Document> getAllDocuments();

    void setRelevant(Document document);

    void setNonrelevant(Document document);
}

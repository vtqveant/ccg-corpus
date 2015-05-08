package ru.eventflow.ccgbank.data;

import ru.eventflow.ccgbank.model.Document;

import java.util.List;

/**
 * Created by transcend on 08.05.15.
 */
public interface DataManager {
    List<Document> getAllDocuments();

    void setRelevant(Document document);

    void setNonrelevant(Document document);
}

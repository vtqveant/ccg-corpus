package ru.eventflow.annotation;

import ru.eventflow.fts.datasource.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

/**
 * Ходим в БД
 */
public class DataManager {

    private static final EntityManager em = Persistence.createEntityManagerFactory("h2-openjpa").createEntityManager();

    public static List<ru.eventflow.annotation.model.Document> getAllDocuments() {
        List<Document> documents = em.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();

        List<ru.eventflow.annotation.model.Document> models = new ArrayList<>();
        for (Document document : documents) {
            ru.eventflow.annotation.model.Document model =
                    new ru.eventflow.annotation.model.Document(document.getId(), document.getUrl(), document.getText());
            models.add(model);
        }
        return models;
    }

    public static void setRelevant(int documentId) {
        // TODO
        // em.createQuery("UPDATE ");
    }

}

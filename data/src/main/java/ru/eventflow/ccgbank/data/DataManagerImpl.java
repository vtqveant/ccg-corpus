package ru.eventflow.ccgbank.data;

import ru.eventflow.ccgbank.model.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class DataManagerImpl implements DataManager {

    private static final EntityManager enitityManager = Persistence.createEntityManagerFactory("h2-openjpa").createEntityManager();

    @Override
    public List<Document> getAllDocuments() {
        return enitityManager.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();
    }

    @Override
    public void setRelevant(Document document) {
        // TODO
        System.out.println(document.getId() + " set relevant");
    }

    @Override
    public void setNonrelevant(Document document) {
        // TODO
    }

    public static EntityManager getEnitityManager() {
        return enitityManager;
    }
}

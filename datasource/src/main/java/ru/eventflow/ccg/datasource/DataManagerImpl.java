package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class DataManagerImpl implements DataManager {

    private final EntityManager enitityManager;

    public DataManagerImpl(String persistenceUnitName) {
        enitityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
    }

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
}

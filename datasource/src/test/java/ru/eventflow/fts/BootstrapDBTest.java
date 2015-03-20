package ru.eventflow.fts;

import org.junit.Test;
import ru.eventflow.fts.datasource.Assessment;
import ru.eventflow.fts.datasource.Document;
import ru.eventflow.fts.datasource.Query;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class BootstrapDBTest {

    @Test
    public void testBootstrap() {
        EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();
        em.getTransaction().begin();

        Document document = new Document();
        document.setId(1);
        document.setText("test");
        document.setUrl("http://example.com?attr1=val1&attr2=val2");
        em.persist(document);

        Query query = new Query();
        query.setOrthography("test query");
        query.setDescription("This is a test query.");
        em.persist(query);

        Assessment assessment = new Assessment();
        assessment.setQuery(query);
        assessment.setDocument(document);
        assessment.setRelevant(true);
        em.persist(assessment);

        em.getTransaction().commit();
    }

}

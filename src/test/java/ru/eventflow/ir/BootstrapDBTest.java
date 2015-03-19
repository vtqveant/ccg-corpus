package ru.eventflow.ir;

import org.junit.Test;

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

        em.getTransaction().commit();
    }

}

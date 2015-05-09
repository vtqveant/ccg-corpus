package ru.eventflow.ccg.datasource;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class BootstrapDBTest {

    @Test
    public void testBootstrap() {
        EntityManager em = Persistence.createEntityManagerFactory(DataSource.DEFAULT).createEntityManager();
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

}

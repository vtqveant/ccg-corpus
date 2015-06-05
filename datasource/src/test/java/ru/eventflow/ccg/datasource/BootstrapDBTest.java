package ru.eventflow.ccg.datasource;

import org.junit.Test;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Map;

public class BootstrapDBTest {

    @Test
    public void testBootstrap() {
        EntityManager em = Persistence.createEntityManagerFactory(DataSource.DEFAULT).createEntityManager();
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    @Test
    public void testDictionaryQueries() {
        DataManagerImpl dataManager = new DataManagerImpl(DataSource.DEFAULT);
        Map<Form, List<Grammeme>> results = dataManager.getGrammemes("были");

        for (Map.Entry<Form, List<Grammeme>> entry : results.entrySet()) {
            Form f = entry.getKey();
            System.out.println(f.getOrthography() + " (" + f.getLexeme().getLemma().getOrthography() + ")");
            for (Grammeme g : entry.getValue()) {
                System.out.println('\t' + g.getName() + " - " + g.getDescription());
            }
        }
    }

    @Test
    public void testFormAutocomplete() {
        DataManagerImpl dataManager = new DataManagerImpl(DataSource.DEFAULT);
        List<String> results = dataManager.getOrthographies("ног");
        for (String orthography : results) {
            System.out.println(orthography);
        }
    }

}

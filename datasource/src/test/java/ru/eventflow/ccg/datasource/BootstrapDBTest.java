package ru.eventflow.ccg.datasource;

import org.junit.Test;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.syntax.Category;

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
        Map<Form, List<String>> results = dataManager.getGrammemes("были");

        for (Map.Entry<Form, List<String>> entry : results.entrySet()) {
            Form f = entry.getKey();
            System.out.println(f.getOrthography() + " (" + f.getLexeme().getLemma().getOrthography() + ")");
            for (String g : entry.getValue()) {
                System.out.println('\t' + g);
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

//    @Test
    public void testGetOccurences() {
        DataManagerImpl dataManager = new DataManagerImpl(DataSource.DEFAULT);
        Form form = dataManager.getFormById(1562247); // злословия

        List<Sentence> results = dataManager.getSentencesByFormOccurence(form);
        for (Sentence sentence : results) {
            System.out.println(sentence);
        }

    }

}

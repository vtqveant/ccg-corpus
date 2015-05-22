package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.DataSource;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.*;

public class JPADataBridge implements DataBridge {

    private static final EntityManager entityManager = Persistence.createEntityManagerFactory(DataSource.DEFAULT).createEntityManager();
    public static final int BUFFER_SIZE = 10000;

    private static int counter = 0;

    private Map<String, Grammeme> grammemes = new HashMap<>();
    private List<Lexeme> lexemes = new ArrayList<>();

    public void addGrammeme(Grammeme grammeme) {
        grammemes.put(grammeme.getName(), grammeme);

        // no clever stuff, the number of grammemes is very small
        entityManager.getTransaction().begin();
        entityManager.persist(grammeme);
        entityManager.getTransaction().commit();
    }

    public void addLexeme(Lexeme lexeme) {
        lexemes.add(lexeme);

        // persist
        counter++;
        if (counter % BUFFER_SIZE == 0) {
            entityManager.getTransaction().begin();
            for (Lexeme l : lexemes) {
                entityManager.persist(l);
            }
            entityManager.getTransaction().commit();
            System.out.println(counter + " lexemes added");
            lexemes.clear();
        }
    }

    public Grammeme getGrammeme(String name) {
        return grammemes.get(name);
    }

    @Override
    public List<Grammeme> getGrammemes() {
        List<Grammeme> l = new ArrayList<>();
        l.addAll(grammemes.values());
        return l;
    }

    @Override
    public List<Lexeme> getLexemes() {
        return lexemes;
    }

}

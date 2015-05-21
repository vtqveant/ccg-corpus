package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.DataSource;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class DataBridge {

    private static final EntityManager entityManager = Persistence.createEntityManagerFactory(DataSource.DEFAULT).createEntityManager();
    public static final int BUFFER_SIZE = 1000;

    private static int counter = 0;

    private List<Grammeme> grammemes = new ArrayList<>();
    private List<Lexeme> lexemes = new ArrayList<>();

    public void addGrammeme(Grammeme grammeme) {
        grammemes.add(grammeme);

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
                for (Form f : l.getForms()) {
                    f.setLexeme(l);
                }
                entityManager.persist(l);
            }
            entityManager.getTransaction().commit();
            System.out.println(counter + " lexemes added");
            lexemes.clear();
        }
    }

    public List<Grammeme> getGrammemes() {
        return grammemes;
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

}

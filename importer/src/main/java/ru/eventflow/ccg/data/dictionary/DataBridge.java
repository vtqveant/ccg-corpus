package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;

import java.util.List;

public interface DataBridge {
    void addGrammeme(Grammeme grammeme);

    void addLexeme(Lexeme lexeme);

    List<Grammeme> getGrammemes();

    List<Lexeme> getLexemes();

    Grammeme getGrammeme(String name);
}

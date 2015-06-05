package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Text;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import java.util.List;
import java.util.Map;

public interface DataManager {

    List<Text> getAllTexts();

    Map<Form, List<Grammeme>> getGrammemes(String form);

    List<String> getOrthographies(String prefix);
}

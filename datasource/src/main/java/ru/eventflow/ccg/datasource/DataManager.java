package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Text;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.util.List;
import java.util.Map;

public interface DataManager {

    List<Text> getAllTexts();

    Map<Form, List<String>> getGrammemes(String form);

    List<String> getOrthographies(String prefix);

    Sentence getSentenceById(int id);

    List<Sentence> getSentencesByFormId(int formId);

    List<Sentence> getSentencesByFormOccurence(Form form);
}

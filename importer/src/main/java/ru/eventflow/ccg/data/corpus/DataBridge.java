package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import java.util.List;

public interface DataBridge {
    void addText(Text text);

    Text getTextById(Integer id);

    void setRevision(String revision);

    void setVersion(String version);

    Form resolveForm(String formOrthography, String lexemeId, List<String> grammemes);
}

package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.datasource.model.corpus.Text;

import java.util.List;

public interface DataBridge {
    void addText(Text text);

    void setRevision(String revision);

    void setVersion(String version);

    int resolve(int lexemeId, List<String> grammemes);

    int addForm(String orthography, List<String> grammemes);
}

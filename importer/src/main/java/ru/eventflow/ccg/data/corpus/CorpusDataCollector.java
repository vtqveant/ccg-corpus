package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.datasource.model.disambig.Text;

import java.util.ArrayList;
import java.util.List;

public class CorpusDataCollector {

    String version;
    String revision;

    List<Text> texts = new ArrayList<>();

    public void addText(Text t) {
        if (texts.size() == 100) return;
        texts.add(t);
    }

}
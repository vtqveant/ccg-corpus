package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.data.xml.annot.Text;

import java.util.ArrayList;
import java.util.List;

public class CorpusDataCollector {

    String version;
    String revision;

    List<Text> texts = new ArrayList<>();

    public void addText(Text t) {
        texts.add(t);
    }


}

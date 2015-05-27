package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.disambig.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CorpusDataCollector {

    String version;
    String revision;

    Map<Integer, Text> texts = new HashMap<>();

    Map<Integer, Grammeme> grammemes = new HashMap<>();

    public CorpusDataCollector() {

    }

    public void addText(Text text) {
        texts.put(text.getId(), text);
    }

    public List<Text> getTexts() {
        return toList(texts);
    }

    public Text getText(String id) {
        return texts.get(Integer.valueOf(id));
    }

    private static <V> List<V> toList(Map<?, V> map) {
        List<V> l = new ArrayList<>();
        l.addAll(map.values());
        return l;
    }
}
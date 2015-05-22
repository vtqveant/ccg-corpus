package ru.eventflow.ccg.data;

import ru.eventflow.ccg.data.dictionary.DictionaryParser;
import ru.eventflow.ccg.data.dictionary.JPADataBridge;

import java.io.InputStream;
import java.net.URL;

public class DictionaryLoader {

    public static void main(String[] args) throws Exception {
        DictionaryParser parser = new DictionaryParser(new JPADataBridge());
        InputStream in = new URL("file:///home/transcend/code/NLU-RG/ccg-corpus/data/resources/dict.opcorpora.xml").openStream();
        parser.process(in);
    }
}

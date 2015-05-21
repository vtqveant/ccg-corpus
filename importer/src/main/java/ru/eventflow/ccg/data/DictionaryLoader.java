package ru.eventflow.ccg.data;

import ru.eventflow.ccg.data.dictionary.DictionaryParser;

import java.io.InputStream;
import java.net.URL;

public class DictionaryLoader {
    public static void main(String[] args) throws Exception {
        DictionaryParser parser = new DictionaryParser();

        InputStream in = new URL("file:///home/transcend/code/NLU-RG/ccg-corpus/data/resources/dict.opcorpora.xml").openStream();
        parser.process(in);

        System.out.println(parser.getGrammemes().size());
        System.out.println(parser.getLexemes().size());
    }
}

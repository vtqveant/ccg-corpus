package ru.eventflow.ccg.data.corpus;

import java.io.InputStream;
import java.net.URL;

public class CorpusLoader {

    public static final String CONNECTION_URL = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
    public static final String FILENAME = "/home/transcend/code/NLU/ccg-corpus/data/resources/annot.opcorpora.xml";

    public static void main(String[] args) throws Exception {
        DataBridge bridge = new SQLDataBridge(CONNECTION_URL);
        CorpusParser parser = new CorpusParser(bridge);
        InputStream in = new URL("file://" + FILENAME).openStream();
        parser.process(in);
    }
}

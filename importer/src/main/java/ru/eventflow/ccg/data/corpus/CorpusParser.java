package ru.eventflow.ccg.data.corpus;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CorpusParser {

    private CorpusDataCollector collector = new CorpusDataCollector();

    public CorpusParser() {
    }

    public void process(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        CorpusHandler handler = new CorpusHandler(xmlReader, collector);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(in));
    }

    public static void main(String[] args) throws Exception {
        CorpusParser parser = new CorpusParser();
        InputStream in = new URL("file:///home/transcend/code/NLU-RG/ccg-corpus/importer/src/test/resources/opcorpora/no_ambig.xml").openStream();
        parser.process(in);

        System.out.println();
    }
}

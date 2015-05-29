package ru.eventflow.ccg.data;

import org.junit.Test;
import org.xml.sax.SAXException;
import ru.eventflow.ccg.data.corpus.CorpusParser;
import ru.eventflow.ccg.data.corpus.DataBridge;
import ru.eventflow.ccg.data.dictionary.DictionaryDataCollector;
import ru.eventflow.ccg.data.dictionary.DictionaryParser;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;
import ru.eventflow.ccg.datasource.model.dictionary.LinkType;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoaderTest {

    @Test
    public void testDictionaryParser() throws IOException, SAXException, ParserConfigurationException {
        DictionaryDataCollector collector = new DictionaryDataCollector();
        DictionaryParser parser = new DictionaryParser(collector);

        InputStream in = DictionaryParser.class.getResourceAsStream("/opcorpora/dict.xml");
        parser.process(in);

        List<Grammeme> grammemes = collector.getGrammemes();
        assertNotNull(grammemes);
        assertEquals(13, grammemes.size());

        List<Lexeme> lexemes = collector.getLexemes();
        assertNotNull(lexemes);
        assertEquals(10, lexemes.size());

        List<LinkType> types = collector.getLinkTypes();
        assertNotNull(lexemes);
        assertEquals(23, types.size());
    }

    @Test
    public void testCorpusParser() throws IOException, SAXException, ParserConfigurationException {
        DataBridge bridge = new MockDataBridge();
        CorpusParser parser = new CorpusParser(bridge);
        parser.process(getClass().getResourceAsStream("/opcorpora/ambig.xml"));

        // TODO implement asserts
    }

    private class MockDataBridge implements DataBridge {
        @Override
        public void addText(Text text) {
            // TODO
        }

        @Override
        public Text getTextById(Integer id) {
            return null;
        }

        @Override
        public void setRevision(String revision) {
        }

        @Override
        public void setVersion(String version) {
        }

        @Override
        public Form resolveForm(String formOrthography, String lexemeId, List<String> grammemes) {
            return null;
        }
    }

}

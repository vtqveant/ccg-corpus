package ru.eventflow.ccg.data;

import org.junit.Test;
import org.xml.sax.SAXException;
import ru.eventflow.ccg.data.corpus.CorpusParser;
import ru.eventflow.ccg.data.corpus.DataBridge;
import ru.eventflow.ccg.data.dictionary.DictionaryDataCollector;
import ru.eventflow.ccg.data.dictionary.DictionaryParser;
import ru.eventflow.ccg.datasource.model.corpus.Paragraph;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Text;
import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;
import ru.eventflow.ccg.datasource.model.dictionary.LinkType;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        MockDataBridge bridge = new MockDataBridge();
        CorpusParser parser = new CorpusParser(bridge);
        parser.process(getClass().getResourceAsStream("/opcorpora/ambig.xml"));

        assertEquals(2, bridge.getTexts().size());

        Text t = bridge.getTexts().get(1);
        assertNotNull(t);
        assertTrue(t.getParagraphs().size() > 1);

        Paragraph p = t.getParagraphs().get(1);
        assertNotNull(p);
        assertTrue(p.getSentences().size() > 0);

        Sentence s = p.getSentences().get(0);
        assertNotNull(s);
        assertTrue(s.getTokens().size() > 12);

        Token token = s.getTokens().get(12);
        assertEquals(3, token.getVariants().size());
    }

    private class MockDataBridge implements DataBridge {
        List<Text> texts = new ArrayList<>();

        @Override
        public void addText(Text text) {
            texts.add(text);
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

        public List<Text> getTexts() {
            return texts;
        }
    }

}

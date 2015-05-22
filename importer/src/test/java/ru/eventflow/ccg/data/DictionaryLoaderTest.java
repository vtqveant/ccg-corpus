package ru.eventflow.ccg.data;

import org.junit.Test;
import org.xml.sax.SAXException;
import ru.eventflow.ccg.data.dictionary.DataBridge;
import ru.eventflow.ccg.data.dictionary.DictionaryParser;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DictionaryLoaderTest {

    @Test
    public void testParser() throws IOException, SAXException, ParserConfigurationException {
        DictionaryParser parser = new DictionaryParser(new MemoryDataBridge());

        InputStream in = DictionaryParser.class.getResourceAsStream("/opcorpora/dict.xml");
        parser.process(in);

        List<Grammeme> grammemes = parser.getGrammemes();
        assertNotNull(grammemes);
        assertEquals(13, grammemes.size());

        List<Lexeme> lexemes = parser.getLexemes();
        assertNotNull(lexemes);
        assertEquals(10, lexemes.size());
    }

    private class MemoryDataBridge implements DataBridge {

        List<Grammeme> grammemes = new ArrayList<>();
        List<Lexeme> lexemes = new ArrayList<>();

        @Override
        public void addGrammeme(Grammeme grammeme) {
            grammemes.add(grammeme);
        }

        @Override
        public void addLexeme(Lexeme lexeme) {
            lexemes.add(lexeme);
        }

        @Override
        public List<Grammeme> getGrammemes() {
            return grammemes;
        }

        @Override
        public List<Lexeme> getLexemes() {
            return lexemes;
        }

        @Override
        public Grammeme getGrammeme(String name) {
            return null;
        }
    }
}

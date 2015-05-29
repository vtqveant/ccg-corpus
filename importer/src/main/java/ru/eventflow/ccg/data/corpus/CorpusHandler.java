package ru.eventflow.ccg.data.corpus;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.eventflow.ccg.data.BaseNestedHandler;
import ru.eventflow.ccg.datasource.model.corpus.*;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.util.ArrayList;
import java.util.List;

public class CorpusHandler extends DefaultHandler {

    private XMLReader reader;
    private DataBridge bridge;

    public CorpusHandler(XMLReader reader, DataBridge bridge) {
        this.reader = reader;
        this.bridge = bridge;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        switch (name) {
            case "annotation":
                bridge.setRevision(attributes.getValue("revision"));
                bridge.setVersion(attributes.getValue("version"));
                break;
            case "text":
                TextHandler textHandler = new TextHandler(reader, this);
                textHandler.text.setId(Integer.valueOf(attributes.getValue("id")));
                textHandler.text.setParent(bridge.getTextById(Integer.valueOf(attributes.getValue("parent"))));
                textHandler.text.setName(attributes.getValue("name"));
                reader.setContentHandler(textHandler);
                break;
        }
    }

    class TextHandler extends BaseNestedHandler {
        Text text = new Text();

        protected TextHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            if (name.equals("paragraph")) {
                ParagraphHandler paragraphHandler = new ParagraphHandler(reader, this);
                paragraphHandler.paragraph.setId(Integer.valueOf(attributes.getValue("id")));
                paragraphHandler.paragraph.setText(text);
                reader.setContentHandler(paragraphHandler);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws org.xml.sax.SAXException {
            switch (name) {
                case "tag":
                    Tag tag = new Tag();
                    tag.setValue(content.toString());
                    tag.setText(text);
                    text.addTag(tag);
                    break;
                case "text":
                    bridge.addText(text);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class ParagraphHandler extends BaseNestedHandler {
        Paragraph paragraph = new Paragraph();

        public ParagraphHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            if (name.equals("sentence")) {
                SentenceHandler sentenceHandler = new SentenceHandler(reader, this);
                sentenceHandler.sentence.setId(Integer.valueOf(attributes.getValue("id")));
                sentenceHandler.sentence.setParagraph(paragraph);
                reader.setContentHandler(sentenceHandler);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (name.equals("paragraph")) {
                ((TextHandler) parent).text.addParagraph(paragraph);
                reader.setContentHandler(parent);
            }
        }
    }

    class SentenceHandler extends BaseNestedHandler {
        Sentence sentence = new Sentence();

        public SentenceHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            if (name.equals("token")) {
                TokenHandler tokenHandler = new TokenHandler(reader, this);
                tokenHandler.token.setId(Integer.valueOf(attributes.getValue("id")));
                tokenHandler.token.setOrthography(attributes.getValue("text"));
                tokenHandler.token.setSentence(sentence);
                reader.setContentHandler(tokenHandler);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "source":
                    sentence.setSource(content.toString());
                    break;
                case "sentence":
                    ((ParagraphHandler) parent).paragraph.addSentence(sentence);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class TokenHandler extends BaseNestedHandler {
        Token token = new Token();
        String orthography;

        public TokenHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "tfr":
                    token.setRevision(Integer.valueOf(attributes.getValue("rev_id")));
                    orthography = attributes.getValue("t");
                    VariantHandler variantHandler = new VariantHandler(reader, this);
                    reader.setContentHandler(variantHandler);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (name.equals("token")) {
                ((SentenceHandler) parent).sentence.addToken(token);
                reader.setContentHandler(parent); // switch back to parent handler
            }
        }
    }

    class VariantHandler extends BaseNestedHandler {
        private Variant variant;
        private List<String> grammemes = new ArrayList<>();
        private String lexemeId;

        public VariantHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "l":
                    // lemma_id = 0 is Out of Vocabulary
                    // TODO for OOV we need an explicit orthography
                    lexemeId = attributes.getValue("id");
                    variant = new Variant();
                    break;
                case "g":
                    grammemes.add(attributes.getValue("v")); // XPath: //sentence/tokens/token/tfr/v/l/g/@v
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "l":
                    TokenHandler tokenHandler = (TokenHandler) parent;
                    Form form = bridge.resolveForm(tokenHandler.orthography, lexemeId, grammemes);
                    variant.setForm(form);
                    variant.setToken(tokenHandler.token);
                    tokenHandler.token.addVariant(variant);
                    break;
                case "tfr":
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }
}

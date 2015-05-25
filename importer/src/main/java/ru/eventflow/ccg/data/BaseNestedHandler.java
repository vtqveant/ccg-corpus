package ru.eventflow.ccg.data;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public abstract class BaseNestedHandler extends DefaultHandler {

    public XMLReader reader;
    public ContentHandler parent;
    public StringBuilder content = new StringBuilder();

    public BaseNestedHandler(XMLReader reader, ContentHandler parent) {
        this.reader = reader;
        this.parent = parent;
    }

    /**
     * characters can be called multiple times per element so aggregate the content in a StringBuilder
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        content.append(ch, start, length);
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        content.setLength(0);
    }

    abstract public void endElement(String uri, String localName, String name) throws SAXException;
}
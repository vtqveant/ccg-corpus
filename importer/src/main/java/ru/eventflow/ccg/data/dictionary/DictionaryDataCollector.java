package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;
import ru.eventflow.ccg.datasource.model.dictionary.Link;
import ru.eventflow.ccg.datasource.model.dictionary.LinkType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryDataCollector {

    private Map<String, Grammeme> grammemes = new HashMap<>();
    private Map<Integer, Lexeme> lexemes = new HashMap<>();
    private Map<Integer, LinkType> linkTypes = new HashMap<>();
    private Map<Integer, Link> links = new HashMap<>();

    public DictionaryDataCollector() {
    }

    public void addGrammeme(Grammeme grammeme) {
        grammemes.put(grammeme.getName(), grammeme);
    }

    public void addLexeme(Lexeme lexeme) {
        lexemes.put(lexeme.getId(), lexeme);
    }

    public void addLinkType(LinkType type) {
        linkTypes.put(type.getId(), type);
    }

    public void addLink(Link link) {
        links.put(link.getId(), link);
    }

    public Grammeme getGrammeme(String name) {
        return grammemes.get(name);
    }

    public Lexeme getLexeme(int id) {
        return lexemes.get(id);
    }

    public LinkType getLinkType(int id) {
        return linkTypes.get(id);
    }

    public List<LinkType> getLinkTypes() {
        return toList(linkTypes);
    }

    public List<Grammeme> getGrammemes() {
        return toList(grammemes);
    }

    public List<Lexeme> getLexemes() {
        return toList(lexemes);
    }

    public List<Link> getLinks() {
        return toList(links);
    }

    private static <V> List<V> toList(Map<?, V> map) {
        List<V> l = new ArrayList<>();
        l.addAll(map.values());
        return l;
    }
}

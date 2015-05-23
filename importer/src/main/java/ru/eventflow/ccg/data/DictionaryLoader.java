package ru.eventflow.ccg.data;

import ru.eventflow.ccg.data.dictionary.DictionaryDataCollector;
import ru.eventflow.ccg.data.dictionary.DictionaryParser;
import ru.eventflow.ccg.datasource.model.dictionary.*;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dirty, unsafe and fast
 */
public class DictionaryLoader {

    public static final String URL = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
    public static final String RESOURSE_LOCATION = "file:///home/transcend/code/NLU-RG/ccg-corpus/data/resources/dict.opcorpora.xml";

    public static final int LIMIT = 10000;

    public DictionaryLoader() {
    }

    public void load(final String url, final String resourceLocation) throws Exception {
        DictionaryDataCollector collector = new DictionaryDataCollector();
        DictionaryParser parser = new DictionaryParser(collector);
        InputStream in = new URL(resourceLocation).openStream();
        parser.process(in);

        System.out.println("Grammemes: " + collector.getGrammemes().size());
        System.out.println("Lexemes: " + collector.getLexemes().size());
        System.out.println("LinkTypes: " + collector.getLinkTypes().size());
        System.out.println("Links: " + collector.getLinks().size());

        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);

        // grammemes
        PreparedStatement grammemeSt = conn.prepareStatement("INSERT INTO dictionary.grammeme(name, alias, description, parent_id) VALUES (?, ?, ?, ?)");
        for (Grammeme grammeme : collector.getGrammemes()) {
            grammemeSt.setString(1, grammeme.getName());
            grammemeSt.setString(2, grammeme.getAlias());
            grammemeSt.setString(3, grammeme.getDescription());
            String parentId = grammeme.getParent() == null ? null : grammeme.getParent().getName();
            grammemeSt.setString(4, parentId);
            grammemeSt.executeUpdate();
        }
        conn.commit();
        grammemeSt.close();

        // link types
        PreparedStatement linkTypeSt = conn.prepareStatement("INSERT INTO dictionary.link_type(id, type) VALUES (?, ?)");
        for (LinkType linkType : collector.getLinkTypes()) {
            linkTypeSt.setInt(1, linkType.getId());
            linkTypeSt.setString(2, linkType.getType());
            linkTypeSt.executeUpdate();
        }
        conn.commit();
        linkTypeSt.close();
        System.out.println("grammemes and link types done");

        // links
        PreparedStatement linksSt = conn.prepareStatement("INSERT INTO dictionary.link(id, from_id, to_id, type_id) VALUES (?, ?, ?, ?)");
        List<Link> linksBuffer = new ArrayList<>(LIMIT);
        int i = 0;
        for (Link link : collector.getLinks()) {
            i++;
            linksBuffer.add(link);
            if (i % LIMIT == 0) {
                persistLinks(linksSt, linksBuffer);
                conn.commit();
                linksBuffer.clear();
                System.out.println(i);
            }
        }
        persistLinks(linksSt, linksBuffer); // persist what's left
        conn.commit();
        linksSt.close();
        System.out.println("links done");

        // lexemes
        PreparedStatement formSt = conn.prepareStatement("INSERT INTO dictionary.form(lemma, orthography, lexeme_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement formGrammemeSt = conn.prepareStatement("INSERT INTO dictionary.form_to_grammeme(form_id, grammeme_id) VALUES (?, ?)");
        PreparedStatement lexemeSt = conn.prepareStatement("INSERT INTO dictionary.lexeme(id, rev, lemma_id) VALUES (?, ?, ?)");
        List<Lexeme> lexemesBuffer = new ArrayList<>(LIMIT);
        int j = 0;
        for (Lexeme lexeme : collector.getLexemes()) {
            j++;
            lexemesBuffer.add(lexeme);
            if (j % LIMIT == 0) {
                persistLexemes(lexemeSt, formSt, formGrammemeSt, lexemesBuffer);
                conn.commit();
                lexemesBuffer.clear();
                System.out.println(j);
            }
        }
        persistLexemes(lexemeSt, formSt, formGrammemeSt, lexemesBuffer); // persist what's left
        conn.commit();
        formSt.close();
        formGrammemeSt.close();
        lexemeSt.close();
        System.out.println("lexemes done");

        conn.close();
    }

    private void persistLexemes(PreparedStatement lexemeSt, PreparedStatement formSt, PreparedStatement formGrammemeSt, List<Lexeme> lexemes) throws SQLException {
        for (Lexeme l : lexemes) {
            for (Form form : l.getForms()) {
                persistForm(formSt, formGrammemeSt, form);
            }
            int lemmaId = persistForm(formSt, formGrammemeSt, l.getLemma());
            lexemeSt.setInt(1, l.getId());
            lexemeSt.setInt(2, l.getRev());
            lexemeSt.setInt(3, lemmaId);
            lexemeSt.executeUpdate();
        }
    }

    /**
     * forms are the only objects that do not have an id in the OpenCorpora export xml
     */
    private int persistForm(PreparedStatement formSt, PreparedStatement formGrammemeSt, Form form) throws SQLException {
        formSt.setBoolean(1, form.isLemma());
        formSt.setString(2, form.getOrthography());
        formSt.setInt(3, form.getLexeme().getId());
        formSt.executeUpdate();
        ResultSet rs = formSt.getGeneratedKeys();
        int id = -1;
        if (rs.next()) {
            id = rs.getInt(1);
        }

        for (Grammeme grammeme : form.getGrammemes()) {
            formGrammemeSt.setInt(1, id);
            formGrammemeSt.setString(2, grammeme.getName());
            formGrammemeSt.executeUpdate();
        }
        return id;
    }

    private void persistLinks(PreparedStatement linkSt, List<Link> links) throws SQLException {
        for (Link link : links) {
            linkSt.setInt(1, link.getId());
            linkSt.setInt(2, link.getFrom().getId());
            linkSt.setInt(3, link.getTo().getId());
            linkSt.setInt(4, link.getType().getId());
            linkSt.executeUpdate();
        }
    }

    public static void main(String[] args) throws Exception {
        DictionaryLoader loader = new DictionaryLoader();
        loader.load(URL, RESOURSE_LOCATION);
    }

}

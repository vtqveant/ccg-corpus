package ru.eventflow.ccg.data;

import ru.eventflow.ccg.data.dictionary.DataCollector;
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

    public static final int LIMIT = 5000;

    public static void main(String[] args) throws Exception {
        DataCollector collector = new DataCollector();
        DictionaryParser parser = new DictionaryParser(collector);
        InputStream in = new URL("file:///home/transcend/code/NLU-RG/ccg-corpus/data/resources/dict.opcorpora.xml").openStream();
        parser.process(in);

        System.out.println("Grammemes: " + collector.getGrammemes().size());
        System.out.println("Lexemes: " + collector.getLexemes().size());
        System.out.println("LinkTypes: " + collector.getLinkTypes().size());
        System.out.println("Links: " + collector.getLinks().size());

        String url = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);

        for (Grammeme grammeme : collector.getGrammemes()) {
            insertGrammeme(conn, grammeme);
        }
        for (LinkType linkType : collector.getLinkTypes()) {
            insertLinkType(conn, linkType);
        }
        conn.commit();
        System.out.println("grammemes and link types done");

        List<Link> linksBuffer = new ArrayList<>(LIMIT);
        int i = 0;
        for (Link link : collector.getLinks()) {
            i++;
            linksBuffer.add(link);
            if (i % LIMIT == 0) {
                persistLinks(conn, linksBuffer);
                conn.commit();
                linksBuffer.clear();
                System.out.println(i);
            }
        }
        persistLinks(conn, linksBuffer); // persist what's left
        conn.commit();
        System.out.println("links done");

        List<Lexeme> lexemesBuffer = new ArrayList<>(LIMIT);
        int j = 0;
        for (Lexeme lexeme : collector.getLexemes()) {
            j++;
            lexemesBuffer.add(lexeme);
            if (j % LIMIT == 0) {
                persistLexemes(conn, lexemesBuffer);
                conn.commit();
                lexemesBuffer.clear();
                System.out.println(j);
            }
        }
        persistLexemes(conn, lexemesBuffer); // persist what's left
        conn.commit();
        System.out.println("lexemes done");
    }

    private static void persistLexemes(Connection conn, List<Lexeme> lexemes) throws SQLException {
        for (Lexeme l : lexemes) {
            for (Form form : l.getForms()) {
                int formId = insertForm(conn, form);
                for (Grammeme grammeme : form.getGrammemes()) {
                    insertFormToGrammeme(conn, formId, grammeme.getName());
                }
            }
            int lemmaId = insertForm(conn, l.getLemma());
            for (Grammeme grammeme : l.getLemma().getGrammemes()) {
                insertFormToGrammeme(conn, lemmaId, grammeme.getName());
            }
            l.getLemma().setId(lemmaId);
            insertLexeme(conn, l);
        }
    }

    private static void persistLinks(Connection conn, List<Link> links) throws SQLException {
        for (Link link : links) {
            PreparedStatement st = conn.prepareStatement("INSERT INTO dictionary.link(id, from_id, to_id, type_id) VALUES (?, ?, ?, ?)");
            st.setInt(1, link.getId());
            st.setInt(2, link.getFrom().getId());
            st.setInt(3, link.getTo().getId());
            st.setInt(4, link.getType().getId());
            st.executeUpdate();
            st.close();
        }
    }

    /**
     * forms are the only objects that do not have an id in the OpenCorpora export xml
     */
    private static int insertForm(Connection conn, Form form) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO dictionary.form(lemma, orthography, lexeme_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        st.setBoolean(1, form.isLemma());
        st.setString(2, form.getOrthography());
        st.setInt(3, form.getLexeme().getId());
        st.executeUpdate();
        ResultSet rs = st.getGeneratedKeys();
        int key = -1;
        if (rs.next()) {
            key = rs.getInt(1);
        }
        st.close();
        return key;
    }

    private static void insertFormToGrammeme(Connection conn, int formId, String grammemeName) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO dictionary.form_to_grammeme(form_id, grammeme_id) VALUES (?, ?)");
        st.setInt(1, formId);
        st.setString(2, grammemeName);
        st.executeUpdate();
        st.close();
    }

    private static void insertLexeme(Connection conn, Lexeme lexeme) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO dictionary.lexeme(id, rev, lemma_id) VALUES (?, ?, ?)");
        st.setInt(1, lexeme.getId());
        st.setInt(2, lexeme.getRev());
        st.setInt(3, lexeme.getLemma().getId());
        st.executeUpdate();
        st.close();
    }

    private static void insertGrammeme(Connection conn, Grammeme grammeme) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO dictionary.grammeme(name, alias, description, parent_id) VALUES (?, ?, ?, ?)");
        st.setString(1, grammeme.getName());
        st.setString(2, grammeme.getAlias());
        st.setString(3, grammeme.getDescription());
        if (grammeme.getParent() != null) {
            st.setString(4, grammeme.getParent().getName());
        } else {
            st.setString(4, null);
        }
        st.executeUpdate();
        st.close();
    }

    private static void insertLinkType(Connection conn, LinkType linkType) throws SQLException {
        PreparedStatement st = conn.prepareStatement("INSERT INTO dictionary.link_type(id, type) VALUES (?, ?)");
        st.setInt(1, linkType.getId());
        st.setString(2, linkType.getType());
        st.executeUpdate();
        st.close();
    }

}

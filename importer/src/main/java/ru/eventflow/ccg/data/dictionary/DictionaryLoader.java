package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.model.dictionary.*;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;

/**
 * Dirty, unsafe and fast. Requires 1.5G of heap to run.
 */
public class DictionaryLoader {

    public static final String URL = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
//    public static final String RESOURSE_LOCATION = "file:///home/transcend/code/NLU-RG/ccg-corpus/data/resources/dict.opcorpora.xml";
    public static final String RESOURSE_LOCATION = "file:///C:\\KOSTA\\code\\ccg-corpus\\data\\resources\\dict.opcorpora.xml";

    public static final int CHUNK_SIZE = 5000;

    public DictionaryLoader() {
    }

    public void load(final String url, final String resourceLocation) throws Exception {
        DictionaryDataCollector collector = new DictionaryDataCollector();
        DictionaryParser parser = new DictionaryParser(collector);
        InputStream in = new URL("file://" + resourceLocation).openStream();
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
        grammemeSt.close();
        conn.commit();

        // link types
        PreparedStatement linkTypeSt = conn.prepareStatement("INSERT INTO dictionary.link_type(id, type) VALUES (?, ?)");
        for (LinkType linkType : collector.getLinkTypes()) {
            linkTypeSt.setInt(1, linkType.getId());
            linkTypeSt.setString(2, linkType.getType());
            linkTypeSt.executeUpdate();
        }
        linkTypeSt.close();
        conn.commit();
        System.out.println("grammemes and link types done");

        // links
        PreparedStatement linkSt = conn.prepareStatement("INSERT INTO dictionary.link(id, from_id, to_id, type_id) VALUES (?, ?, ?, ?)");
        int i = 0;
        for (Link link : collector.getLinks()) {
            linkSt.setInt(1, link.getId());
            linkSt.setInt(2, link.getFrom().getId());
            linkSt.setInt(3, link.getTo().getId());
            linkSt.setInt(4, link.getType().getId());
            linkSt.executeUpdate();

            if (++i % CHUNK_SIZE == 0) {
                conn.commit();
                System.out.println(i);
            }
        }
        linkSt.close();
        conn.commit();
        System.out.println("links done");

        // lexemes
        PreparedStatement formSt = conn.prepareStatement("INSERT INTO dictionary.form(lemma, orthography, lexeme_id) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement formGrammemeSt = conn.prepareStatement("INSERT INTO dictionary.form_to_grammeme(form_id, grammeme_id) VALUES (?, ?)");
        PreparedStatement lexemeSt = conn.prepareStatement("INSERT INTO dictionary.lexeme(id, rev, lemma_id) VALUES (?, ?, ?)");
        int j = 0;
        for (Lexeme lexeme : collector.getLexemes()) {
            // forms
            for (Form form : lexeme.getForms()) {
                persistForm(formSt, formGrammemeSt, form);
            }

            // lemma is a special form
            int lemmaId = persistForm(formSt, formGrammemeSt, lexeme.getLemma());

            // lexeme
            lexemeSt.setInt(1, lexeme.getId());
            lexemeSt.setInt(2, lexeme.getRev());
            lexemeSt.setInt(3, lemmaId);
            lexemeSt.executeUpdate();

            if (++j % CHUNK_SIZE == 0) {
                conn.commit();
                System.out.println(j);
            }
        }
        formSt.close();
        formGrammemeSt.close();
        lexemeSt.close();
        conn.commit();
        System.out.println("lexemes done");

        conn.close();
    }

    /**
     * forms are the only objects that do not have an id in the OpenCorpora export xml, so we have to keep track of owr own one
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

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args.length % 2 != 0 || !args[0].equals("--resource")) {
            System.out.println("Usage:");
            System.out.println("\t--resource\tOpenCorpora dictionary dump file (XML)");
            System.exit(-1);
        }
        String resourceLocation = args[1];

        DictionaryLoader loader = new DictionaryLoader();
        loader.load(URL, resourceLocation);
    }

}

package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.data.ExportableBitSet;
import ru.eventflow.ccg.datasource.model.dictionary.*;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Dirty, unsafe and fast. Requires 1.5G of heap to run.
 */
public class DictionaryLoader {

    public static final String URL = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";
    public static final String RESOURSE_LOCATION = "file:///home/transcend/code/NLU-RG/ccg-corpus/data/resources/dict.opcorpora.xml";
//    public static final String RESOURSE_LOCATION = "file:///C:\\KOSTA\\code\\ccg-corpus\\data\\resources\\dict.opcorpora.xml";

    public static final int CHUNK_SIZE = 5000;

    private static final int BITSET_SIZE = 113; // number of grammemes

    private final Map<String, BitSet> grammemeFlags = new HashMap<>();      // grammeme name -> single grammeme bitset
    private PreparedStatement grammemesByFormIdStatement;


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

        System.out.println("Updating flags");
        updateGrammemeFlags(conn);

        conn.close();
    }

    private void updateGrammemeFlags(Connection conn) throws SQLException {
        // prepare grammeme names for bitsets
        PreparedStatement updateGrammemeFlags = conn.prepareStatement("UPDATE dictionary.grammeme SET flags = ? " +
                "WHERE name = ?");
        PreparedStatement grammemesStatement = conn.prepareStatement("SELECT \"name\" " +
                "FROM dictionary.grammeme");
        ResultSet grRs = grammemesStatement.executeQuery();
        int i = 0;
        while (grRs.next()) {
            ExportableBitSet flag = new ExportableBitSet(BITSET_SIZE);
            flag.set(i);
            i++;

            updateGrammemeFlags.setBytes(1, flag.toByteArray());
            updateGrammemeFlags.setString(2, grRs.getString("name"));
            updateGrammemeFlags.execute();

            // cache
            grammemeFlags.put(grRs.getString("name"), flag);
        }

        System.out.println("Grammemes ready");

        // prepare forms to lemmata mapping
        final Map<Integer, Integer> lemmata = new HashMap<>(); // form id -> lemma id
        PreparedStatement formsLexemesStatement = conn.prepareStatement("SELECT f.id, l.lemma_id " +
                "FROM dictionary.form f, dictionary.lexeme l " +
                "WHERE (NOT f.lemma) AND f.lexeme_id = l.id");
        ResultSet rs2 = formsLexemesStatement.executeQuery();
        while (rs2.next()) {
            lemmata.put(rs2.getInt("id"), rs2.getInt("lemma_id"));
        }

        System.out.println("forms -> lemmata ready");


        // TODO join in the memory (currently it takes approx. 20 minutes)
        PreparedStatement updateFormFlags = conn.prepareStatement("UPDATE dictionary.form SET flags = ? WHERE id= ?");
        // prepare get grammemes by form id
        grammemesByFormIdStatement = conn.prepareStatement("SELECT fg.grammeme_id AS name " +
                "FROM dictionary.form f, dictionary.form_to_grammeme fg " +
                "WHERE f.id = fg.form_id AND f.id = ?");

        // compute grammeme bitsets for each form id
        int j = 0;
        for (int formId : lemmata.keySet()) {
            ExportableBitSet flags = getGrammemesBitSet(formId);
            flags.or(getGrammemesBitSet(lemmata.get(formId)));
            // forms.put(formId, flags);
            updateFormFlags.setBytes(1, flags.toByteArray());
            updateFormFlags.setInt(2, formId);
            updateFormFlags.execute();

            if (j % 10000 == 0) {
                System.out.println(j + " bitsets done");
            }
            j++;
        }

        System.out.println("flags done.");
    }

    private ExportableBitSet getGrammemesBitSet(int formId) throws SQLException {
        ExportableBitSet flags = new ExportableBitSet(BITSET_SIZE);
        grammemesByFormIdStatement.setInt(1, formId);
        ResultSet rs1 = grammemesByFormIdStatement.executeQuery();
        while (rs1.next()) {
            String grammemeName = rs1.getString("name");
            flags.or(grammemeFlags.get(grammemeName));
        }
        return flags;
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

package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.model.dictionary.*;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dirty, unsafe and fast. Requires 1.5G of heap to run.
 */
public class DictionaryLoader {

    public static final String URL = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";

    public static final int CHUNK_SIZE = 5000;

    private final Map<String, BitSet> grammemeFlags = new HashMap<>(); // grammeme name -> single grammeme bitset


    public DictionaryLoader() {
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
        PreparedStatement grammemeSt = conn.prepareStatement("INSERT INTO dictionary.grammeme(name, alias, description, parent_id, flags) VALUES (?, ?, ?, ?, ?)");
        int i = 0;

        // grammemes missing in the dictionary
        List<Grammeme> grammemes = collector.getGrammemes();
        grammemes.add(buildGrammeme("UNKN", "неизв.", "отсутствует в словаре"));
        grammemes.add(buildGrammeme("PNCT", "пункт.", "пунктуация" ));
        grammemes.add(buildGrammeme("NUMB", "цифры" , "арабские цифры"));
        grammemes.add(buildGrammeme("ROMN", "римск.", "римские цифры"));
        grammemes.add(buildGrammeme("LATN", "лат.", "латинский"));

        int bitsetSize = grammemes.size();
        for (Grammeme grammeme : grammemes) {
            grammemeSt.setString(1, grammeme.getName());
            grammemeSt.setString(2, grammeme.getAlias());
            grammemeSt.setString(3, grammeme.getDescription());
            String parentId = grammeme.getParent() == null ? null : grammeme.getParent().getName();
            grammemeSt.setString(4, parentId);

            BitSet flag = new BitSet(bitsetSize);
            flag.set(i);
            i++;
            grammemeSt.setBytes(5, flag.toByteArray());

            grammemeSt.executeUpdate();

            // cache
            grammemeFlags.put(grammeme.getName(), flag);
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
        i = 0;
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
        PreparedStatement formSt = conn.prepareStatement("INSERT INTO dictionary.form(lemma, orthography, lexeme_id, flags) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

    private static Grammeme buildGrammeme(String name, String alias, String description) {
        Grammeme g = new Grammeme();
        g.setName(name);
        g.setAlias(alias);
        g.setDescription(description);
        return g;
    }

    /**
     * forms are the only objects that do not have an id in the OpenCorpora export xml, so we have to keep track of owr own one
     */
    private int persistForm(PreparedStatement formSt, PreparedStatement formGrammemeSt, Form form) throws SQLException {
        formSt.setBoolean(1, form.isLemma());
        formSt.setString(2, form.getOrthography());
        formSt.setInt(3, form.getLexeme().getId());

        BitSet flags = new BitSet();
        for (Grammeme grammeme : form.getGrammemes()) {
            flags.or(grammemeFlags.get(grammeme.getName()));
        }
        for (Grammeme grammeme : form.getLexeme().getLemma().getGrammemes()) {
            flags.or(grammemeFlags.get(grammeme.getName()));
        }
        formSt.setBytes(4, flags.toByteArray());

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

}

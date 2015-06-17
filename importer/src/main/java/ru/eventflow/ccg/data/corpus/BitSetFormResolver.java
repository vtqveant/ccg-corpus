package ru.eventflow.ccg.data.corpus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitSetFormResolver {

    private static final int BITSET_SIZE = 113; // number of grammemes

    private final Map<Integer, BitSet> forms = new HashMap<>();    // form id -> grammemes bitset
    private final Map<String, BitSet> bits = new HashMap<>();      // grammeme name -> single grammeme bitset

    private PreparedStatement grammemesByFormIdStatement;
    private PreparedStatement formDetailsStatement;

    public BitSetFormResolver(Connection conn) {
        try {
            // prepare grammeme names for bitsets
            PreparedStatement grammemesStatement = conn.prepareStatement("SELECT \"name\" FROM dictionary.grammeme");
            ResultSet grRs = grammemesStatement.executeQuery();
            int i = 0;
            while (grRs.next()) {
                BitSet flag = new BitSet(BITSET_SIZE);
                flag.set(i);
                bits.put(grRs.getString("name"), flag);
                i++;
            }

            System.out.println("Grammemes ready");

            // prepare forms to lemmata mapping
            final Map<Integer, Integer> lemmata = new HashMap<>(); // form id -> lemma id
            PreparedStatement formsLexemesStatement = conn.prepareStatement("SELECT f.id, l.lemma_id FROM dictionary.form f, dictionary.lexeme l " +
                    "WHERE (NOT f.lemma) AND f.lexeme_id = l.id");
            ResultSet rs2 = formsLexemesStatement.executeQuery();
            while (rs2.next()) {
                lemmata.put(rs2.getInt("id"), rs2.getInt("lemma_id"));
            }

            System.out.println("forms -> lemmata ready");

            // TODO join in the memory (currently it takes approx. 20 minutes)
            // prepare get grammemes by form id
            grammemesByFormIdStatement = conn.prepareStatement("SELECT g.name AS name FROM dictionary.form f, dictionary.grammeme g, dictionary.form_to_grammeme fg " +
                    "WHERE f.id = fg.form_id AND g.name = fg.grammeme_id AND f.id = ?");

            // compute grammeme bitsets for each form id
            int j = 0;
            for (int formId : lemmata.keySet()) {
                BitSet flags = getGrammemesBitSet(formId);
                flags.or(getGrammemesBitSet(lemmata.get(formId)));
                forms.put(formId, flags);

                if (j % 10000 == 0) {
                    System.out.println(j + " bitsets done");
                }
                j++;
            }

            // prepare get form details by orthography and lexeme id
            formDetailsStatement = conn.prepareStatement("SELECT id, orthography, lexeme_id FROM dictionary.form f WHERE NOT f.lemma AND orthography = ? AND lexeme_id = ?");

            System.out.println("BitSetFormResolver ready.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // TODO make a test
    public static void test() throws SQLException {
        /**
         BitSetFormResolver resolver = new BitSetFormResolver(CONNECTION_URL);

         List<String> grammemes = new ArrayList<>();
         grammemes.add("nomn");
         grammemes.add("sing");
         grammemes.add("NOUN");
         grammemes.add("anim");
         grammemes.add("masc");
         Form form = resolver.resolve("ёж", 1, grammemes);

         System.out.println(form);
         */
    }

    private BitSet getGrammemesBitSet(int formId) throws SQLException {
        BitSet flags = new BitSet(BITSET_SIZE);
        grammemesByFormIdStatement.setInt(1, formId);
        ResultSet rs1 = grammemesByFormIdStatement.executeQuery();
        while (rs1.next()) {
            String grammemeName = rs1.getString("name");
            flags.or(bits.get(grammemeName));
        }
        return flags;
    }

    /**
     * 1. select forms matching lexeme id and orthography
     * 2. filter forms to match grammemes
     */
    public int resolve(String orthography, int lexemeId, List<String> grammemes) {
        // build a bitset for comparison
        BitSet grammemesBitSet = new BitSet(BITSET_SIZE);
        for (String grammeme : grammemes) {
            grammemesBitSet.or(bits.get(grammeme));
        }

        try {
            formDetailsStatement.setString(1, orthography);
            formDetailsStatement.setInt(2, lexemeId);
            ResultSet rs4 = formDetailsStatement.executeQuery();

            while (rs4.next()) {
                int formId = rs4.getInt("id");
                BitSet flags = forms.get(formId);
                if (flags != null && flags.equals(grammemesBitSet)) {
                    return formId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

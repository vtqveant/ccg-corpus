package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.data.ExportableBitSet;

import java.sql.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitSetFormResolver {

    private static final int BITSET_SIZE = 113; // number of grammemes
    private final Map<String, BitSet> grammemeFlags = new HashMap<>();      // grammeme name -> single grammeme bitset

    private Connection conn;

    public BitSetFormResolver(Connection conn) throws SQLException {
        this.conn = conn;

        PreparedStatement st = conn.prepareStatement("SELECT name, flags FROM dictionary.grammeme");
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            ExportableBitSet bs = new ExportableBitSet(rs.getBytes("flags"));
            grammemeFlags.put(rs.getString("name"), bs);
        }
    }

    /**
     * 1. select forms matching lexeme id and orthography
     * 2. filter forms to match grammemes
     */
    public int resolve(String orthography, int lexemeId, List<String> grammemes) {
        // build a bitset for comparison
        BitSet grammemesBitSet = new BitSet(BITSET_SIZE);
        for (String grammeme : grammemes) {
            BitSet flag = grammemeFlags.get(grammeme);
            if (flag != null) {
                grammemesBitSet.or(flag);
            }
        }

        try {
            PreparedStatement st = conn.prepareStatement("SELECT flags FROM dictionary.form WHERE id = ?");

            // prepare get form details by orthography and lexeme id
            PreparedStatement formDetailsStatement = conn.prepareStatement("SELECT id, orthography, lexeme_id " +
                    "FROM dictionary.form f " +
                    "WHERE NOT f.lemma AND orthography = ? AND lexeme_id = ?");

            formDetailsStatement.setString(1, orthography);
            formDetailsStatement.setInt(2, lexemeId);
            ResultSet rs4 = formDetailsStatement.executeQuery();

            while (rs4.next()) {
                int formId = rs4.getInt("id");
                st.setInt(1, formId);
                ResultSet rs = st.executeQuery();
                rs.next();
                byte[] bytes = rs.getBytes("flags");
                ExportableBitSet flags = new ExportableBitSet(bytes);

                if (flags.equals(grammemesBitSet)) {
                    return formId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}

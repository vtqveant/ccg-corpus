package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.data.ExportableBitSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitSetFormResolver {

    private final Map<String, BitSet> grammemeFlags = new HashMap<>();  // grammeme name -> single grammeme bitset

    private PreparedStatement formDetailsStatement;
    private PreparedStatement formFlagsStatement;

    public BitSetFormResolver(Connection conn) throws SQLException {

        PreparedStatement st = conn.prepareStatement("SELECT name, flags FROM dictionary.grammeme");
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            ExportableBitSet bs = new ExportableBitSet(rs.getBytes("flags"));
            grammemeFlags.put(rs.getString("name"), bs);
        }

        formDetailsStatement = conn.prepareStatement("SELECT id, lexeme_id FROM dictionary.form f WHERE lexeme_id = ?");
        formFlagsStatement = conn.prepareStatement("SELECT flags FROM dictionary.form WHERE id = ?");
    }

    /**
     * 1. select forms matching lexeme id and orthography
     * 2. filter forms to match grammemes
     */
    public int resolve(int lexemeId, List<String> grammemes) {
        // build a bitset for comparison
        BitSet grammemesBitSet = new BitSet();
        for (String grammeme : grammemes) {
            BitSet flag = grammemeFlags.get(grammeme);
            if (flag == null) return -1; // unknown grammeme TODO fix OOV gramemes
            grammemesBitSet.or(flag);
        }

        try {
            formDetailsStatement.setInt(1, lexemeId);
            ResultSet rs4 = formDetailsStatement.executeQuery();

            while (rs4.next()) {
                int formId = rs4.getInt("id");
                formFlagsStatement.setInt(1, formId);
                ResultSet rs = formFlagsStatement.executeQuery();
                rs.next();
                byte[] bytes = rs.getBytes("flags");
                BitSet flags = new ExportableBitSet(bytes);

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

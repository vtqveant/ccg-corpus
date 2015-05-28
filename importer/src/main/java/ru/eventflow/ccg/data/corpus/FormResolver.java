package ru.eventflow.ccg.data.corpus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormResolver {

    public static final String URL = "jdbc:postgresql://localhost/corpus?user=corpus&password=corpus";

    String lexemeId;
    String lexeme;
    String orthography;
    List<String> grammemes = new ArrayList<>();

    private static Connection conn;

    public FormResolver() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(URL);
                conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int resolve() {
        int formId = 0;
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("select form.id as form_id, g.name as grammeme ");
            sb.append("from dictionary.form lemma, dictionary.form form, dictionary.lexeme lexeme, ");
            sb.append("dictionary.form_to_grammeme fg, dictionary.grammeme g ");
            sb.append("where g.name = fg.grammeme_id and lemma.lexeme_id = lexeme.id and ");
            sb.append("form.lexeme_id = lexeme.id and (fg.form_id = lemma.id or fg.form_id = form.id) ");
            sb.append("and lemma.id = lexeme.lemma_id and lexeme.id = ");
            sb.append(lexemeId);
            sb.append(" and form.lemma = false ");
            sb.append("and form.orthography = '");
            sb.append(orthography.replace("\'", "\'\'"));
            sb.append("' and ");
            sb.append("g.name in (");

            for (String g : grammemes) {
                sb.append('\'');
                sb.append(g.replace("\'", "\'\'"));
                sb.append('\'');
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") order by form_id");

            String q = sb.toString();
//            System.out.println(q);

            PreparedStatement statement = conn.prepareStatement(q);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) { // guaranteed to be a single result
                formId = rs.getInt("form_id");
//                System.out.println(formId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // reset
        grammemes.clear();
        orthography = null;
        lexemeId = null;
        lexeme = null;

        return formId;
    }


}

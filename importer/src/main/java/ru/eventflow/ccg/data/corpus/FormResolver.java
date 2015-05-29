package ru.eventflow.ccg.data.corpus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO add missing grammemes to the dictionary tagset (and mark)
 */
public class FormResolver {

    String lexemeId;
    String orthography;
    List<String> grammemes = new ArrayList<>();

    private Connection conn;

    public FormResolver(Connection conn) {
        this.conn = conn;
    }

    int resolve() {
        int formId = 0;
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT form.id AS form_id ");
            sb.append("FROM dictionary.form lemma, dictionary.form form, dictionary.lexeme lexeme, ");
            sb.append("dictionary.form_to_grammeme fg, dictionary.grammeme g ");
            sb.append("WHERE g.name = fg.grammeme_id AND lemma.lexeme_id = lexeme.id AND ");
            sb.append("form.lexeme_id = lexeme.id AND (fg.form_id = lemma.id OR fg.form_id = form.id) ");
            sb.append("AND lemma.id = lexeme.lemma_id AND lexeme.id = ");
            sb.append(lexemeId);
            sb.append(" AND form.lemma = false AND ");
            sb.append("form.orthography = '");
            sb.append(orthography.replace("\'", "\'\'"));
            sb.append("' AND ");
            sb.append("g.name IN (");

            for (String g : grammemes) {
                sb.append('\'');
                sb.append(g.replace("\'", "\'\'"));
                sb.append('\'');
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") group by form.id having count(g.name) = ");
            sb.append(grammemes.size());

            String q = sb.toString();
//            System.out.println(q);

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(q);
            while (rs.next()) {
                formId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // reset
        grammemes.clear();

        return formId;
    }


}

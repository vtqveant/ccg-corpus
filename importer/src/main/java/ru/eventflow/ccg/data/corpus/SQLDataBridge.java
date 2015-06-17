package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.data.ExportableBitSet;
import ru.eventflow.ccg.datasource.model.corpus.*;

import java.sql.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLDataBridge implements DataBridge {

    private final Map<String, BitSet> grammemeFlags = new HashMap<>(); // grammeme name -> single grammeme bitset
    private String version;
    private String revision;
    private Connection conn;
    private PreparedStatement stVariant;
    private PreparedStatement stToken;
    private PreparedStatement stSentence;
    private PreparedStatement stParagraph;
    private PreparedStatement stText;
    private PreparedStatement stTag;
    private PreparedStatement stFormDetails;
    private PreparedStatement stFormFlags;
    private int counter = 0;

    public SQLDataBridge(String connectionUrl) {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(connectionUrl);
                conn.setAutoCommit(false);

                stVariant = conn.prepareStatement("INSERT INTO corpus.variant (token_id, form_id) VALUES (?, ?)");
                stToken = conn.prepareStatement("INSERT INTO corpus.token (id, orthography, revision, sentence_id, pos) VALUES (?, ?, ?, ?, ?)");
                stSentence = conn.prepareStatement("INSERT INTO corpus.sentence (id, source, paragraph_id, pos) VALUES (?, ?, ?, ?)");
                stParagraph = conn.prepareStatement("INSERT INTO corpus.paragraph (id, text_id, pos) VALUES (?, ?, ?)");
                stText = conn.prepareStatement("INSERT INTO corpus.text (id, name, parent_id) VALUES (?, ?, ?)");
                stTag = conn.prepareStatement("INSERT INTO corpus.tag (source, text_id) VALUES (?, ?)");

                // form resolver
                PreparedStatement st = conn.prepareStatement("SELECT name, flags FROM dictionary.grammeme");
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    ExportableBitSet bs = new ExportableBitSet(rs.getBytes("flags"));
                    grammemeFlags.put(rs.getString("name"), bs);
                }

                stFormDetails = conn.prepareStatement("SELECT id, lexeme_id FROM dictionary.form f WHERE lexeme_id = ?");
                stFormFlags = conn.prepareStatement("SELECT flags FROM dictionary.form WHERE id = ?");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void addText(Text text) {
        try {
            for (Paragraph paragraph : text.getParagraphs()) {
                for (Sentence sentence : paragraph.getSentences()) {
                    for (Token token : sentence.getTokens()) {
                        for (Variant variant : token.getVariants()) {
                            stVariant.setInt(1, variant.getToken().getId());
                            stVariant.setInt(2, variant.getForm().getId());
                            stVariant.execute();
                        }
                        stToken.setInt(1, token.getId());
                        stToken.setString(2, token.getOrthography());
                        stToken.setInt(3, token.getRevision());
                        stToken.setInt(4, token.getSentence().getId());
                        stToken.setInt(5, token.getPosition());
                        stToken.execute();
                    }
                    stSentence.setInt(1, sentence.getId());
                    stSentence.setString(2, sentence.getSource());
                    stSentence.setInt(3, sentence.getParagraph().getId());
                    stSentence.setInt(4, sentence.getPosition());
                    stSentence.execute();
                }
                stParagraph.setInt(1, paragraph.getId());
                stParagraph.setInt(2, paragraph.getText().getId());
                stParagraph.setInt(3, paragraph.getPosition());
                stParagraph.execute();
            }
            stText.setInt(1, text.getId());
            stText.setString(2, text.getName());
            if (text.getParent() != null) {
                stText.setInt(3, text.getParent().getId());
            } else {
                stText.setNull(3, Types.INTEGER);
            }
            stText.execute();

            for (Tag tag : text.getTags()) {
                stTag.setString(1, tag.getValue());
                stTag.setInt(2, tag.getText().getId());
                stTag.execute();
            }

            conn.commit();

            if (++counter % 100 == 0) {
                System.out.println(counter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void setRevision(String revision) {
        this.revision = revision;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * links tokens in the dump to dictionary entries based on grammatical information
     */
    @Override
    public int resolve(int lexemeId, List<String> grammemes) {
        try {
            stFormDetails.setInt(1, lexemeId);
            ResultSet rs4 = stFormDetails.executeQuery();

            while (rs4.next()) {
                int formId = rs4.getInt("id");
                stFormFlags.setInt(1, formId);
                ResultSet rs = stFormFlags.executeQuery();
                rs.next();
                byte[] bytes = rs.getBytes("flags");
                BitSet flags = new ExportableBitSet(bytes);

                BitSet formFlags = buildFlags(grammemes);
                if (flags.equals(formFlags)) {
                    return formId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private ExportableBitSet buildFlags(List<String> grammemes) {
        ExportableBitSet formFlags = new ExportableBitSet();
        for (String grammeme : grammemes) {
            BitSet flag = grammemeFlags.get(grammeme);
            if (flag == null) {  // fix OOV gramemes
                int size = grammemeFlags.size();
                flag = new BitSet();
                flag.set(size);
                grammemeFlags.put(grammeme, flag);
            }
            formFlags.or(flag);
        }
        return formFlags;
    }

    @Override
    public int addForm(String orthography, List<String> grammemes) {
        try {
            PreparedStatement stForm = conn.prepareStatement("INSERT INTO dictionary.form (lemma, orthography, lexeme_id, flags) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stLexeme = conn.prepareStatement("INSERT INTO dictionary.lexeme (id, rev, lemma_id) VALUES (?, ?, ?)");
            PreparedStatement stFormGrammeme = conn.prepareStatement("INSERT INTO dictionary.form_to_grammeme (form_id, grammeme_id) VALUES (?, ?)");

            // id in 'dictionary.lexeme' is manual
            PreparedStatement stNextLexemeId = conn.prepareStatement("SELECT max(id) FROM dictionary.lexeme");
            ResultSet rs = stNextLexemeId.executeQuery();
            rs.next();
            int lexemeId = rs.getInt(1) + 1;

            // add lemma
            stForm.setBoolean(1, true);
            stForm.setString(2, orthography);
            stForm.setInt(3, lexemeId);
            stForm.setBytes(4, buildFlags(grammemes).toByteArray());
            stForm.executeUpdate();
            ResultSet generatedKeys = stForm.getGeneratedKeys();
            generatedKeys.next();
            int lemmaId = generatedKeys.getInt(1);

            // add regular form
            stForm.setBoolean(1, false);
            stForm.setString(2, orthography);
            stForm.setInt(3, lexemeId);
            stForm.setBytes(4, new byte[]{});
            stForm.executeUpdate();
            ResultSet generatedKeys2 = stForm.getGeneratedKeys();
            generatedKeys2.next();
            int formId = generatedKeys2.getInt(1);

            // add lexeme
            stLexeme.setInt(1, lexemeId);
            stLexeme.setInt(2, -1);
            stLexeme.setInt(3, lemmaId);
            stLexeme.executeUpdate();

            // add grammemes
            for (String grammeme : grammemes) {
                stFormGrammeme.setInt(1, lemmaId);
                stFormGrammeme.setString(2, grammeme);
                stFormGrammeme.executeUpdate();
            }

//            conn.commit();
            stForm.close();
            stLexeme.close();
            stFormGrammeme.close();

            return formId;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
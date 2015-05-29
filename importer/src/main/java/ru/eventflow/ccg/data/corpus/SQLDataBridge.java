package ru.eventflow.ccg.data.corpus;

import ru.eventflow.ccg.datasource.model.corpus.*;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLDataBridge implements DataBridge {

    private Connection conn;
    private FormResolver resolver;

    String version;
    String revision;

    private Map<Integer, Text> texts = new HashMap<>();
    private PreparedStatement stVariant;
    private PreparedStatement stToken;
    private PreparedStatement stSentence;
    private PreparedStatement stParagraph;
    private PreparedStatement stText;
    private PreparedStatement stTag;

    public SQLDataBridge(String connectionUrl) {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(connectionUrl);
                conn.setAutoCommit(false);

                stVariant = conn.prepareStatement("INSERT INTO corpus.variant (token_id, form_id) VALUES (?, ?)");
                stToken = conn.prepareStatement("INSERT INTO corpus.token (id, orthography, sentence_id) VALUES (?, ?, ?)");
                stSentence = conn.prepareStatement("INSERT INTO corpus.sentence (id, source, paragraph_id) VALUES (?, ?, ?)");
                stParagraph = conn.prepareStatement("INSERT INTO corpus.paragraph (id, text_id) VALUES (?, ?)");
                stText = conn.prepareStatement("INSERT INTO corpus.text (id, name, parent_id) VALUES (?, ?, ?)");
                stTag = conn.prepareStatement("INSERT INTO corpus.tag (source, text_id) VALUES (?, ?)");

                resolver = new FormResolver(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void addText(Text text) {
        texts.put(text.getId(), text); // this is for parent lookups

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
                        stToken.setInt(3, token.getSentence().getId());
                        stToken.execute();
                    }
                    stSentence.setInt(1, sentence.getId());
                    stSentence.setString(2, sentence.getSource());
                    stSentence.setInt(3, sentence.getParagraph().getId());
                    stSentence.execute();
                }
                stParagraph.setInt(1, paragraph.getId());
                stParagraph.setInt(2, paragraph.getText().getId());
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public Text getTextById(Integer id) {
        return texts.get(id);
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
     * A dummy Form instance with an id is enough for inserts
     *
     * @param formOrthography
     * @param lexemeId
     * @param grammemes
     * @return
     */
    @Override
    public Form resolveForm(String formOrthography, String lexemeId, List<String> grammemes) {
        resolver.orthography = formOrthography;
        resolver.lexemeId = lexemeId;
        resolver.grammemes = grammemes;
        int id = resolver.resolve();
        Form dummy = new Form();
        dummy.setId(id);
        return dummy;
    }
}
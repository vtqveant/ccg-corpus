package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;

public class AnnotationView extends JPanel {

    private final JPanel glossesPanel;
    private final JTextArea goalsTextArea;
    private final JTextPane textPane;

    AbstractDocument doc;

    public AnnotationView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());

        // glosses at the top
        glossesPanel = new JPanel();
        glossesPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        glossesPanel.setBackground(Color.WHITE);

        // TODO make smart horizontal scrollbar
        JScrollPane scrollPane = new JScrollPane(glossesPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.PAGE_START);

        // editor
        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textPane.setMargin(new Insets(5, 5, 5, 5));
        StyledDocument styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDoc;
            // doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument!");
            System.exit(-1);
        }

        JScrollPane sp1 = new JScrollPane(textPane);
        sp1.setSize(new Dimension(600, -1));
        sp1.setBorder(BorderFactory.createEmptyBorder());

        // goals
        goalsTextArea = new JTextArea(1, 30);
        goalsTextArea.setEditable(false);
        goalsTextArea.setFocusable(false);
        goalsTextArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        goalsTextArea.setBackground(new Color(243, 243, 243));
        goalsTextArea.setText("1 goal");

        JScrollPane sp2 = new JScrollPane(goalsTextArea);
        sp2.setBorder(BorderFactory.createEmptyBorder());
        sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setOneTouchExpandable(false);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setLeftComponent(sp1);
        splitPane.setRightComponent(sp2);
        add(splitPane, BorderLayout.CENTER);
    }

    public void addGlosses(List<String> words, List<String> glosses) {
        assert words.size() == glosses.size();
        int size = words.size();
        for (int i = 0; i < size; i++) {
            glossesPanel.add(new TokenPanel(words.get(i), glosses.get(i)), 0);
        }
    }

    public JTextArea getGoalsTextArea() {
        return goalsTextArea;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    private class TokenPanel extends JPanel {
        public TokenPanel(String token, String gloss) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            setBackground(Color.WHITE);

            JLabel tokenLabel = new JLabel(token);
            tokenLabel.setHorizontalAlignment(SwingConstants.LEFT);
            tokenLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            add(tokenLabel);

            if (gloss == null || gloss.length() == 0) gloss = " ";
            JLabel glossLabel = new JLabel(gloss);
            glossLabel.setHorizontalAlignment(SwingConstants.LEFT);
            glossLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
            add(glossLabel);
        }
    }
}

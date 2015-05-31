package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AnnotationView extends JPanel {

    private final JPanel glossesPanel;

    public AnnotationView() {
        setLayout(new BorderLayout());

        glossesPanel = new JPanel();
        glossesPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        glossesPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(glossesPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.PAGE_END);
    }

    public void addGlosses(List<String> words, List<String> glosses) {
        assert words.size() == glosses.size();
        int size = words.size();
        for (int i = 0; i < size; i++) {
            glossesPanel.add(new TokenPanel(words.get(i), glosses.get(i)), 0);
        }
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
            glossLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 11));
            add(glossLabel);
        }
    }

}

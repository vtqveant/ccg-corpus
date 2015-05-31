package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AnnotationView extends JPanel {

    private final JPanel topPanel;

    public AnnotationView() {
        setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));

        JScrollPane scrollPane = new JScrollPane(topPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.PAGE_END);
    }

    public void addGlosses(List<String> words, List<String> glosses) {
        assert words.size() == glosses.size();
        int size = words.size();
        for (int i = 0; i < size; i++) {
            topPanel.add(new TokenPanel(words.get(i), glosses.get(i)), 0);
        }
        topPanel.validate();
    }

    private class TokenPanel extends JPanel {
        public TokenPanel(String token, String gloss) {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 3));

            JLabel tokenLabel = new JLabel(token);
            tokenLabel.setHorizontalAlignment(SwingConstants.LEFT);
            tokenLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            add(tokenLabel);

            JLabel glossLabel = new JLabel(gloss);
            glossLabel.setHorizontalAlignment(SwingConstants.LEFT);
            glossLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            add(glossLabel);
        }
    }

}

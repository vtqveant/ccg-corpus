package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;

public class GlossesView extends JPanel {

    private static final Color GLOSS_COLOR = new Color(100, 40, 30);

    public GlossesView() {
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        setBackground(Color.WHITE);
    }

    public void addGlosses(java.util.List<String> words, java.util.List<String> glosses) {
        assert words.size() == glosses.size();
        int size = words.size();
        for (int i = 0; i < size; i++) {
            add(new TokenPanel(words.get(i), glosses.get(i)), 0);
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
            tokenLabel.setFont(Defaults.NORMAL_FONT);
            tokenLabel.setForeground(GLOSS_COLOR);
            add(tokenLabel);

            if (gloss == null || gloss.length() == 0) gloss = " ";
            JLabel glossLabel = new JLabel(gloss);
            glossLabel.setHorizontalAlignment(SwingConstants.LEFT);
            glossLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 10));
            glossLabel.setForeground(GLOSS_COLOR);
            add(glossLabel);
        }
    }

}

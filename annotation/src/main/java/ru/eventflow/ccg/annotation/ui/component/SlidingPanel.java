package ru.eventflow.ccg.annotation.ui.component;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;

public abstract class SlidingPanel extends JPanel {

    protected JPanel headingPanel = new JPanel();

    public SlidingPanel() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(Defaults.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);

        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        add(headingPanel, BorderLayout.PAGE_START);
    }

    abstract public ImageIcon getIcon();

    abstract public String getTitle();
}

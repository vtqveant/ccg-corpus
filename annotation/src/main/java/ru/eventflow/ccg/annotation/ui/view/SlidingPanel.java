package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class SlidingPanel extends JPanel {

    public SlidingPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(Defaults.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());

        for (JToggleButton button : getButtons()) {
            headingPanel.add(button);
            headingPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        }

        add(headingPanel, BorderLayout.PAGE_START);
    }

    /**
     * This is called in the constructor
     */
    abstract public ImageIcon getIcon();

    /**
     * This is called in the constructor
     */
    abstract public String getTitle();

    /**
     * This is called in the constructor
     */
    abstract public List<JToggleButton> getButtons();

}

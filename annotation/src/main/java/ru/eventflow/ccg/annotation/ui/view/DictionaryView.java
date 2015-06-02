package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;

public class DictionaryView extends JPanel {

    public DictionaryView() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel("Dictionary");
        titleLabel.setFont(Defaults.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);

        JToggleButton clipboardBtn = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif")));
        clipboardBtn.setToolTipText("Toggle View");
        clipboardBtn.setFocusable(false);

        final JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        headingPanel.add(clipboardBtn);
        add(headingPanel, BorderLayout.PAGE_START);
    }
}

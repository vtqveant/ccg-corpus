package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class DictionaryView extends JPanel {

    public DictionaryView() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel("Dictionary");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
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

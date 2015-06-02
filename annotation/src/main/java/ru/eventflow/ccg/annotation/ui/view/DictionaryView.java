package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.component.SlidingPanel;

import javax.swing.*;

public class DictionaryView extends SlidingPanel {

    public static final String TITLE = "Dictionary";

    public DictionaryView() {
        super();

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif"));
        JToggleButton clipboardBtn = new JToggleButton(icon);
        clipboardBtn.setToolTipText("Toggle View");
        clipboardBtn.setFocusable(false);
        headingPanel.add(clipboardBtn);
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(ClassLoader.getSystemResource("images/lookup.png"));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}

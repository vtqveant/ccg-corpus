package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryView extends SlidingPanel {

    public DictionaryView() {
        super();

        JLabel label = new JLabel("dummy");
        add(label, BorderLayout.CENTER);
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(ClassLoader.getSystemResource("images/lookup.png"));
    }

    @Override
    public String getTitle() {
        return "Dictionary";
    }

    @Override
    public List<JToggleButton> getButtons() {
        List<JToggleButton> buttons = new ArrayList<>();

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif"));
        JToggleButton clipboardBtn = new JToggleButton(icon);
        clipboardBtn.setToolTipText("Toggle View");
        clipboardBtn.setFocusable(false);
        buttons.add(clipboardBtn);

        return buttons;
    }
}

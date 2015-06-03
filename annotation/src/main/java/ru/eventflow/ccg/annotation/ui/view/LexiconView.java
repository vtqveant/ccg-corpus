package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LexiconView extends SliderPanel {

    public LexiconView() {
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
        return "Lexicon";
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

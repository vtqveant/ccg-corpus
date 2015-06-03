package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LexiconSliderView extends SliderPanel {

    private final JSplitPane splitPane;

    public LexiconSliderView() {
        super();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setDividerLocation(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void setRightComponent(Component component) {
        splitPane.setRightComponent(component);
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
package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NavigationSliderView extends SliderPanel {

    private final JSplitPane splitPane;
    private JToggleButton annotatedBtn;
    private JToggleButton ambiguousBtn;

    public NavigationSliderView() {
        super();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        add(splitPane, BorderLayout.CENTER);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void setRightComponent(Component component) {
        splitPane.setRightComponent(component);
    }

    @Override
    public String getTitle() {
        return "Navigation";
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(ClassLoader.getSystemResource("images/corpus.png"));
    }

    @Override
    public List<AbstractButton> getButtons() {
        List<AbstractButton> buttons = new ArrayList<>();

        ImageIcon magnifyIcon = new ImageIcon(ClassLoader.getSystemResource("images/magnify.gif"));
        annotatedBtn = new JToggleButton(magnifyIcon);
        annotatedBtn.setSelected(true);
        annotatedBtn.setToolTipText("Show Annotated Sentences");
        annotatedBtn.setFocusable(false);
        buttons.add(annotatedBtn);

        ImageIcon lockIcon = new ImageIcon(ClassLoader.getSystemResource("images/lock.gif"));
        ambiguousBtn = new JToggleButton(lockIcon);
        ambiguousBtn.setSelected(true);
        ambiguousBtn.setToolTipText("Show Ambiguous Sentences");
        ambiguousBtn.setFocusable(false);
        buttons.add(ambiguousBtn);

        return buttons;
    }

    public JToggleButton getAnnotatedBtn() {
        return annotatedBtn;
    }

    public JToggleButton getAmbiguousBtn() {
        return ambiguousBtn;
    }
}

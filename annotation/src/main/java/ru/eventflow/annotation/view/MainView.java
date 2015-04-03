package ru.eventflow.annotation.view;

import ru.eventflow.annotation.Stuff;

import javax.swing.*;
import java.awt.*;

public class MainView extends JPanel {

    private JPanel topPanel;
    private final JSplitPane splitPane;

    public MainView() {
        setLayout(new BorderLayout());

        // TODO rewrite if you need a menu
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JMenuBar menuBar = Stuff.getStuff();
        topPanel.add(menuBar);
        add(topPanel, BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        add(splitPane, BorderLayout.CENTER);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void setRightComponent(Component component) {
        splitPane.setRightComponent(component);
    }
}

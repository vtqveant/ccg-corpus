package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JPanel {

    private final JSplitPane splitPane;
    private final JLabel statusLabel = new JLabel();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    public MainView() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setRightComponent(tabbedPane);
        add(splitPane, BorderLayout.CENTER);

        statusLabel.setBorder(new EmptyBorder(2, 5, 2, 2));
        statusLabel.setFont(new Font("Sans", Font.PLAIN, 11));
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void addTab(String title, Component component) {
        tabbedPane.addTab(title, component);
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }


}

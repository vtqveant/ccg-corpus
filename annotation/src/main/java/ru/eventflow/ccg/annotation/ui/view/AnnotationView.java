package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class AnnotationView extends JPanel {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public AnnotationView() {
        setLayout(new BorderLayout());
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void addTab(String title, Component component) {
        tabbedPane.addTab(title, component);
    }
}

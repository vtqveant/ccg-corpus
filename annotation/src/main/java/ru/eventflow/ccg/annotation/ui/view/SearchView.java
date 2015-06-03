package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class SearchView extends JPanel {

    public SearchView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        JLabel label = new JLabel("search");
        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, BorderLayout.CENTER);
    }
}

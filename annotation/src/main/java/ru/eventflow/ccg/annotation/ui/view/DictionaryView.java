package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class DictionaryView extends JPanel {

    public DictionaryView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        JLabel label = new JLabel("dictionary");

        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane);
    }
}

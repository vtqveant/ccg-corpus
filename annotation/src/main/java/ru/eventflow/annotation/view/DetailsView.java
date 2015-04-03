package ru.eventflow.annotation.view;

import javax.swing.*;
import java.awt.*;

public class DetailsView extends JPanel {

    private final JTextArea textArea;

    public DetailsView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 300));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }

    public void setText(String text) {
        textArea.setText(text);
    }
}

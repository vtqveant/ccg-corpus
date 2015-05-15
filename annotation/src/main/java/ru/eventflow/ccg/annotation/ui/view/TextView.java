package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class TextView extends JPanel {

    private final JTextArea textArea;

    public TextView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        textArea.setBackground(Color.WHITE);
        textArea.setMargin(new Insets(2, 2, 2, 2));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

    }

    public void setText(String text) {
        textArea.setText(text);
        textArea.setCaretPosition(0);
    }

}

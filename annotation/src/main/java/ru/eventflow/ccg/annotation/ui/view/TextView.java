package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;

public class TextView extends JPanel {

    private final JTextPane textArea;

    public TextView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 300));

        textArea = new JTextPane();
        textArea.setEditable(false);
        textArea.setParagraphAttributes(SimpleAttributeSet.EMPTY, true);
        textArea.setMargin(new Insets(2, 2, 2, 2));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }

    public void setText(String text) {
        textArea.setText(text);
        textArea.setCaretPosition(0);
    }
}

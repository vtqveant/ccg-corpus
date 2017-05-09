package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.SyntaxColoringTextPane;
import ru.eventflow.ccg.annotation.ui.component.TextLineNumber;

import javax.swing.*;
import java.awt.*;

public class FileEditorView extends JPanel {

    private final JTextPane textPane;

    public FileEditorView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());

        textPane = new SyntaxColoringTextPane();
        textPane.setCaretPosition(0);
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textPane.setMargin(new Insets(5, 5, 5, 5));
        textPane.setFont(Defaults.MONOSPACED_FONT);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setSize(new Dimension(600, -1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setRowHeaderView(new TextLineNumber(textPane)); // adds line numbers

        add(scrollPane, BorderLayout.CENTER);
    }

    public JTextPane getTextPane() {
        return textPane;
    }

}

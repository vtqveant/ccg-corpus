package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.component.SlidingPanel;

import javax.swing.*;
import java.awt.*;

public class MessagesView extends SlidingPanel {

    public static final String TITLE = "Messages";

    private final JTextArea textArea = new JTextArea();

    public MessagesView() {
        super();

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif"));
        JToggleButton clipboardBtn = new JToggleButton(icon);
        clipboardBtn.setToolTipText("Toggle View");
        clipboardBtn.setFocusable(false);
        headingPanel.add(clipboardBtn);

        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addRecord(String record) {
        textArea.append(record + '\n');
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(ClassLoader.getSystemResource("images/log.png"));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}

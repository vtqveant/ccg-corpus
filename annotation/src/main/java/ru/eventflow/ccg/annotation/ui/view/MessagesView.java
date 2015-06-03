package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MessagesView extends SlidingPanel {

    private final JTextArea textArea = new JTextArea();

    public MessagesView() {
        super();

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
        return "Messages";
    }

    @Override
    public List<JToggleButton> getButtons() {
        List<JToggleButton> buttons = new ArrayList<>();

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif"));
        JToggleButton clipboardBtn = new JToggleButton(icon);
        clipboardBtn.setToolTipText("Toggle View");
        clipboardBtn.setFocusable(false);
        buttons.add(clipboardBtn);

        return buttons;
    }
}

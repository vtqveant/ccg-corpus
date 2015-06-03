package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MessagesView extends SliderPanel {

    private final JTextArea textArea = new JTextArea();

    public MessagesView() {
        super();

        textArea.setEditable(false);
        textArea.setMargin(new Insets(2, 5, 2, 5));
        textArea.setFont(Defaults.SMALL_FONT);
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
        return null;
    }
}

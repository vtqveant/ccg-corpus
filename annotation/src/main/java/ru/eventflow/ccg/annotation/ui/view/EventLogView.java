package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.SlidingPanel;

import javax.swing.*;
import java.awt.*;

public class EventLogView extends SlidingPanel {

    public static final String TITLE = "Event Log";
    private final JTextArea textArea = new JTextArea();

    public EventLogView() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(Defaults.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);

        JToggleButton clipboardBtn = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif")));
        clipboardBtn.setFocusable(false);

        final JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        headingPanel.add(clipboardBtn);
        add(headingPanel, BorderLayout.PAGE_START);

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

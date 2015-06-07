package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class MessagesSliderView extends SliderPanel {

    private final JTextArea textArea = new JTextArea();

    public MessagesSliderView() {
        super();

        textArea.setEditable(false);
        textArea.setMargin(new Insets(2, 5, 2, 5));
        textArea.setFont(Defaults.SMALL_FONT);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setBackground(Color.WHITE);
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
    public List<AbstractButton> getButtons() {
        List<AbstractButton> buttons = new ArrayList<>();

        ImageIcon trashIcon = new ImageIcon(ClassLoader.getSystemResource("images/trash.gif"));
        JButton clearBtn = new JButton(trashIcon);
        clearBtn.setSelected(true);
        clearBtn.setToolTipText("Clear");
        clearBtn.setFocusable(false);
        clearBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        buttons.add(clearBtn);

        return buttons;
    }
}

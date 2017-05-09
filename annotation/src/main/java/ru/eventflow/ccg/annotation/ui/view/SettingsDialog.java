package ru.eventflow.ccg.annotation.ui.view;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends ModalDialog {

    public static final int LABEL_WIDTH = 120;
    public static final int LABEL_HEIGHT = 20;

    private final JTextField textField = new JTextField(25);

    @Inject
    public SettingsDialog(JFrame frame) {
        super(frame, "Settings");

        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
        upperPanel.add(buildRowBox("SPARQL Endpoint: ", textField));
    }

    private Box buildRowBox(String text, JTextField textField) {
        Box box = new Box(BoxLayout.LINE_AXIS);
        box.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, 0, MARGIN));

        JLabel label = new JLabel(text);
        Dimension dimension = new Dimension(LABEL_WIDTH, LABEL_HEIGHT);
        label.setMinimumSize(dimension);
        label.setMaximumSize(dimension);
        label.setPreferredSize(dimension);
        box.add(label);

        textField.setMaximumSize(textField.getPreferredSize());
        box.add(textField);
        return box;
    }

    public JTextField getTextField() {
        return textField;
    }

}

package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public abstract class ModalDialog extends AbstractDialog {

    protected static final int MARGIN = 20;
    private static final Dimension buttonDimension = new Dimension(65, 30);
    public final JButton okButton = new JButton("OK");
    protected final JPanel upperPanel = new JPanel();

    public ModalDialog(Frame owner, String title) {
        super(owner, title, true);

        add(upperPanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, MARGIN));

        buttonsPanel.add(Box.createHorizontalGlue());

        okButton.setPreferredSize(buttonDimension);
        okButton.setFocusPainted(false);
        buttonsPanel.add(okButton);

        buttonsPanel.add(Box.createHorizontalStrut(10));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(buttonDimension);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());
        buttonsPanel.add(cancelButton);

        add(buttonsPanel, BorderLayout.PAGE_END);
    }

    public JButton getOkButton() {
        return okButton;
    }
}

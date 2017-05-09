package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;

public class ToolbarView extends JToolBar {

    private final JButton openButton;
    private final JButton saveButton;
    private final JButton executeButton;
    private final JButton settingsButton;

    public ToolbarView() {
        setBorder(BorderFactory.createEmptyBorder(4, 2, 4, 2));
        setFloatable(false);
        setRollover(true);

        final ImageIcon openIcon = new ImageIcon(ClassLoader.getSystemResource("images/db.png"));
        openButton = new JButton(openIcon);
        openButton.setFocusPainted(false);
        openButton.setToolTipText("Open Project");
        add(openButton);

        final ImageIcon saveIcon = new ImageIcon(ClassLoader.getSystemResource("images/save.png"));
        saveButton = new JButton(saveIcon);
        saveButton.setFocusPainted(false);
        saveButton.setToolTipText("Save All");
        add(saveButton);

        add(Box.createHorizontalGlue());

        final ImageIcon executeIcon = new ImageIcon(ClassLoader.getSystemResource("images/play.png"));
        executeButton = new JButton("Generate Dataset ", executeIcon);
        executeButton.setFocusPainted(false);
        executeButton.setToolTipText("Generate Dataset");
        executeButton.setEnabled(false);
        add(executeButton);

        final ImageIcon settingsIcon = new ImageIcon(ClassLoader.getSystemResource("images/settings.png"));
        settingsButton = new JButton(settingsIcon);
        settingsButton.setFocusPainted(false);
        settingsButton.setToolTipText("Settings");
        add(settingsButton);
    }

    public JButton getOpenButton() {
        return openButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getExecuteButton() {
        return executeButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

}

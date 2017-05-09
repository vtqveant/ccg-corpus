package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MenuView extends JMenuBar {

    public static final String ITEM_FORMAT = "%-20s";
    private static final ImageIcon PLACEHOLDER_ICON = icon("placeholder.png");
    private final JMenuItem openMenuItem;
    private final JMenuItem saveMenuItem;
    private final JMenuItem settingsMenuItem;
    private final JMenuItem exitMenuItem;
    private final JMenuItem aboutMenuItem;


    public MenuView() {
        setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = new JMenu("File");
        openMenuItem = addMenuItem(fileMenu, "Open Project");
        saveMenuItem = addMenuItem(fileMenu, "Save");
        settingsMenuItem = addMenuItem(fileMenu, "Settings");
        exitMenuItem = addMenuItem(fileMenu, "Exit");
        add(fileMenu);


        settingsMenuItem.setAction(new Action() {
            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public void putValue(String key, Object value) {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void setEnabled(boolean b) {

            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JMenu editMenu = new JMenu("Edit");
        addDisabledMenuItem(editMenu, "Undo", icon("undo.png"));
        addDisabledMenuItem(editMenu, "Undo", icon("redo.png"));
        editMenu.add(new JSeparator());
        addDisabledMenuItem(editMenu, "Cut");
        addDisabledMenuItem(editMenu, "Copy");
        addDisabledMenuItem(editMenu, "Paste");
        addDisabledMenuItem(editMenu, "Delete");
        add(editMenu);

        JMenu helpMenu = new JMenu("Help");
        addDisabledMenuItem(helpMenu, "Manual");
        helpMenu.add(new JSeparator());
        aboutMenuItem = addMenuItem(helpMenu, "About");
        add(helpMenu);
    }

    private static JMenuItem addMenuItem(JMenu menu, String title, ImageIcon imageIcon, boolean enabled) {
        JMenuItem menuItem = new JMenuItem(String.format(ITEM_FORMAT, title), imageIcon);
        menuItem.setEnabled(enabled);
        menu.add(menuItem);
        return menuItem;
    }

    private static ImageIcon icon(String name) {
        return new ImageIcon(ClassLoader.getSystemResource("images/" + name));
    }

    private static JMenuItem addMenuItem(JMenu menu, String title) {
        return addMenuItem(menu, title, PLACEHOLDER_ICON, true);
    }

    private static JMenuItem addDisabledMenuItem(JMenu menu, String title) {
        return addMenuItem(menu, title, PLACEHOLDER_ICON, false);
    }

    private static JMenuItem addDisabledMenuItem(JMenu menu, String title, ImageIcon imageIcon) {
        return addMenuItem(menu, title, imageIcon, false);
    }

    public JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public JMenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public JMenuItem getSaveMenuItem() {
        return saveMenuItem;
    }

    public JMenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    public JMenuItem getSettingsMenuItem() {
        return settingsMenuItem;
    }
}

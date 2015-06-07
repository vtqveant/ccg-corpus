package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.event.Setting;

import javax.swing.*;

public class MenuView extends JMenuBar {

    private static final ImageIcon TICK_ICON = new ImageIcon(ClassLoader.getSystemResource("images/tick.gif"));
    private static final ImageIcon PLACEHOLDER_ICON = new ImageIcon(ClassLoader.getSystemResource("images/placeholder.png"));
    private final JMenuItem firstMenuItem;
    private final JMenuItem secondMenuItem;
    private final SettingMenuItem glossesMenuItem;
    private final SettingMenuItem statusBarMenuItem;
    private final JMenuItem aboutMenuItem;


    public MenuView() {
        setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = new JMenu("File");
        firstMenuItem = new JMenuItem("Settings          ", PLACEHOLDER_ICON);
        fileMenu.add(firstMenuItem);
        secondMenuItem = new JMenuItem("Exit              ", PLACEHOLDER_ICON);
        fileMenu.add(secondMenuItem);
        add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        ImageIcon undoIcon = new ImageIcon(ClassLoader.getSystemResource("images/undo.png"));
        editMenu.add(new JMenuItem("Undo              ", undoIcon));
        ImageIcon redoIcon = new ImageIcon(ClassLoader.getSystemResource("images/redo.png"));
        editMenu.add(new JMenuItem("Redo              ", redoIcon));
        editMenu.add(new JSeparator());
        editMenu.add(new JMenuItem("Cut               ", PLACEHOLDER_ICON));
        editMenu.add(new JMenuItem("Copy              ", PLACEHOLDER_ICON));
        editMenu.add(new JMenuItem("Paste             ", PLACEHOLDER_ICON));
        editMenu.add(new JMenuItem("Delete            ", PLACEHOLDER_ICON));
        add(editMenu);

        JMenu viewMenu = new JMenu("View");
        glossesMenuItem = new SettingMenuItem("Glosses           ", true, Setting.GLOSSES);
        viewMenu.add(glossesMenuItem);
        statusBarMenuItem = new SettingMenuItem("Status Bar        ", true, Setting.STATUSBAR);
        viewMenu.add(statusBarMenuItem);
        add(viewMenu);

        // like in CoqIDE
        JMenu navigationMenu = new JMenu("Navigation");
        ImageIcon forwardIcon = new ImageIcon(ClassLoader.getSystemResource("images/forward.png"));
        navigationMenu.add(new JMenuItem("Forward           ", forwardIcon));
        ImageIcon backwardIcon = new ImageIcon(ClassLoader.getSystemResource("images/backward.png"));
        navigationMenu.add(new JMenuItem("Backward          ", backwardIcon));
        navigationMenu.add(new JMenuItem("Go to             ", PLACEHOLDER_ICON));
        navigationMenu.add(new JMenuItem("Start             ", PLACEHOLDER_ICON));
        navigationMenu.add(new JMenuItem("End               ", PLACEHOLDER_ICON));
        navigationMenu.add(new JMenuItem("Interrupt         ", PLACEHOLDER_ICON));
        navigationMenu.add(new JMenuItem("Hide              ", PLACEHOLDER_ICON));
        add(navigationMenu);

        JMenu tacticsMenu = new JMenu("Tactics");
        tacticsMenu.add(new JMenuItem("simpl             ", PLACEHOLDER_ICON));
        tacticsMenu.add(new JMenuItem("reflexivity       ", PLACEHOLDER_ICON));
        tacticsMenu.add(new JMenuItem("admit             ", PLACEHOLDER_ICON));
        add(tacticsMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("Manual            ", PLACEHOLDER_ICON));
        helpMenu.add(new JSeparator());
        aboutMenuItem = new JMenuItem("About             ", PLACEHOLDER_ICON);
        helpMenu.add(aboutMenuItem);
        add(helpMenu);
    }

    public JMenuItem getFirstMenuItem() {
        return firstMenuItem;
    }

    public JMenuItem getSecondMenuItem() {
        return secondMenuItem;
    }

    public SettingMenuItem getGlossesMenuItem() {
        return glossesMenuItem;
    }

    public SettingMenuItem getStatusBarMenuItem() {
        return statusBarMenuItem;
    }

    public JMenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    /**
     * to emulate behaviour of the ugly JCheckBoxMenuItem
     */
    public class SettingMenuItem extends JMenuItem {
        private boolean checked;
        private Setting setting;

        public SettingMenuItem(String text, boolean checked, Setting setting) {
            super(text);
            this.setting = setting;
            setChecked(checked);
        }

        public Setting getSetting() {
            return setting;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
            setIcon(checked ? TICK_ICON : PLACEHOLDER_ICON);
        }
    }
}

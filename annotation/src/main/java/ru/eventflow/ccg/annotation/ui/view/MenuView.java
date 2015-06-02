package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;

public class MenuView extends JMenuBar {

    private final JMenuItem firstMenuItem;
    private final JMenuItem secondMenuItem;

    public MenuView() {
        setBorder(BorderFactory.createEmptyBorder());

        JMenu menu1 = new JMenu("File");
        firstMenuItem = new JMenuItem("Settings  ");
        menu1.add(firstMenuItem);
        secondMenuItem = new JMenuItem("Exit      ");
        menu1.add(secondMenuItem);
        add(menu1);

        JMenu menu2 = new JMenu("Edit");
        ImageIcon undoIcon = new ImageIcon(ClassLoader.getSystemResource("images/undo.png"));
        menu2.add(new JMenuItem("Undo      ", undoIcon));
        ImageIcon redoIcon = new ImageIcon(ClassLoader.getSystemResource("images/redo.png"));
        menu2.add(new JMenuItem("Redo      ", redoIcon));
        menu2.add(new JSeparator());
        menu2.add(new JMenuItem("Cut       "));
        menu2.add(new JMenuItem("Copy      "));
        menu2.add(new JMenuItem("Paste     "));
        menu2.add(new JMenuItem("Delete    "));
        add(menu2);

        // like in CoqIDE
        JMenu menu3 = new JMenu("Navigation");
        ImageIcon forwardIcon = new ImageIcon(ClassLoader.getSystemResource("images/forward.png"));
        menu3.add(new JMenuItem("Forward   ", forwardIcon));
        ImageIcon backwardIcon = new ImageIcon(ClassLoader.getSystemResource("images/backward.png"));
        menu3.add(new JMenuItem("Backward  ", backwardIcon));
        menu3.add(new JMenuItem("Go to     "));
        menu3.add(new JMenuItem("Start     "));
        menu3.add(new JMenuItem("End       "));
        menu3.add(new JMenuItem("Interrupt "));
        menu3.add(new JMenuItem("Hide      "));
        add(menu3);

        JMenu menu4 = new JMenu("Tactics");
        menu4.add(new JMenuItem("simpl      "));
        menu4.add(new JMenuItem("reflexivity"));
        menu4.add(new JMenuItem("admit      "));
        add(menu4);

        JMenu menu5 = new JMenu("Help");
        menu5.add(new JMenuItem("Manual   "));
        menu5.add(new JSeparator());
        menu5.add(new JMenuItem("About    "));
        add(menu5);
    }

    public JMenuItem getFirstMenuItem() {
        return firstMenuItem;
    }

    public JMenuItem getSecondMenuItem() {
        return secondMenuItem;
    }
}

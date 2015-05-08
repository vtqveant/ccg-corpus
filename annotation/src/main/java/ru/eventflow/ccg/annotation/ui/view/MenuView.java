package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class MenuView extends JMenuBar {

    private final JMenuItem firstMenuItem;
    private final JMenuItem secondMenuItem;

    public MenuView() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");

        firstMenuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
        firstMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        firstMenuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(firstMenuItem);

        secondMenuItem = new JMenuItem("Both text and icon", new ImageIcon(ClassLoader.getSystemResource("images/file-16.png")));
        secondMenuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(secondMenuItem);

        add(menu);
    }

    public JMenuItem getFirstMenuItem() {
        return firstMenuItem;
    }

    public JMenuItem getSecondMenuItem() {
        return secondMenuItem;
    }
}

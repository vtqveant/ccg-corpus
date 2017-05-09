package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDialog extends JDialog {

    private static final int WIDTH = 462;
    private static final int HEIGHT = 286;

    public AbstractDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        setSize(WIDTH, HEIGHT);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (WIDTH / 2);
        int y = (screenSize.height / 2) - (HEIGHT / 2);
        setLocation(x, y);

        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}

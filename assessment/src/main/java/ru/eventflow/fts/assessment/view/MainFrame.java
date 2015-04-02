package ru.eventflow.fts.assessment.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setContentPane(new MainClientForm().getRootPanel());

//        setSize(605, 660);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Eventflow Assessment Tool");
        setLocationRelativeTo(null);
        pack();
    }
}

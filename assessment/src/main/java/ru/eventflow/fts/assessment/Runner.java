package ru.eventflow.fts.assessment;

import javax.swing.*;
import java.awt.*;

public class Runner {

    public static void main(String[] args) {
        MainClientForm mainClientForm = new MainClientForm();

        JFrame frame = new JFrame("Eventflow Assessment Tool");
        frame.setPreferredSize(new Dimension(1024, 680));
        frame.setContentPane(mainClientForm.getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class DictionaryView extends JPanel {

    public DictionaryView() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("dictionary");
        add(label, BorderLayout.CENTER);
    }
}

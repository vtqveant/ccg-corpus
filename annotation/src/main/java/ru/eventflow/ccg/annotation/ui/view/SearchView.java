package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;

public class SearchView extends JPanel {

    public static final ImageIcon SEARCH_ICON = new ImageIcon(ClassLoader.getSystemResource("images/magnify.gif"));
    private final JTextField searchTextField;
    private final JButton searchBtn;

    public SearchView() {
        setLayout(new BorderLayout());

        searchTextField = new JTextField();
        searchTextField.setEditable(true);
        searchTextField.setFont(Defaults.SMALL_FONT);
        add(searchTextField, BorderLayout.CENTER);

        searchBtn = new JButton(SEARCH_ICON);
        searchBtn.setFocusable(false);
        searchBtn.setSize(16, 16);
        add(searchBtn, BorderLayout.EAST);
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public JButton getSearchBtn() {
        return searchBtn;
    }
}

package ru.eventflow.manager.view;

import ru.eventflow.manager.presenter.RootPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RootView implements View<RootPresenter> {

    private RootPresenter presenter;

    private JLabel statusLabel;
    private JTextField inputField;

    public RootView() {
        createUI();
    }

    private void createUI() {
        statusLabel = new JLabel("This updates in reponse to input: ");
        inputField = new JTextField(20);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                presenter.login(inputField.getText());
            }
        });

        Box topBox = Box.createHorizontalBox();
        topBox.add(statusLabel);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(inputField);
        topBox.add(Box.createHorizontalStrut(5));
        topBox.add(loginButton);

        JFrame frame = new JFrame("Eventflow Assessment Tool");
        frame.getContentPane().setBackground(Color.white);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1));

        frame.add(topBox);

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void setPresenter(RootPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * gets called by the presenter to update the status label.
     *
     * @param text
     */
    public void updateStatusLabel(String text) {
        statusLabel.setText(text);
    }

}

package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

public class ContainerView extends JPanel {

    private final JTabbedPane tabbedPane;
    private final JLabel placeholderLabel;

    private int tabCount = 0;

    public static final ImageIcon ICON = new ImageIcon(ClassLoader.getSystemResource("images/cross.png"));

    public ContainerView() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        placeholderLabel = new JLabel("No sentences are being annotated");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setVerticalAlignment(SwingConstants.CENTER);
        placeholderLabel.setBorder(BorderFactory.createEmptyBorder());
        placeholderLabel.setBackground(Color.LIGHT_GRAY);
        placeholderLabel.setOpaque(true);
        add(placeholderLabel, BorderLayout.CENTER);
    }

    public void addTab(String title, Component component) {
        if (tabCount == 0) {
            remove(placeholderLabel);
            add(tabbedPane, BorderLayout.CENTER);
            updateUI();
        }
        tabbedPane.addTab(title, component);
        tabbedPane.setTabComponentAt(tabCount, new TabComponent());
        tabCount++;
    }

    private class TabComponent extends JPanel {
        public TabComponent() {
            setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 0));

            // read titles from JTabbedPane
            JLabel label = new JLabel() {
                public String getText() {
                    if (tabCount > 0) return tabbedPane.getTitleAt(tabCount - 1);
                    return null;
                }
            };
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
            add(label);

            JButton button = new JButton();
            button.setIcon(ICON);
            button.setUI(new BasicButtonUI());
            button.setContentAreaFilled(false);
            button.setFocusable(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setRolloverEnabled(true);
            button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tabCount > 0) tabbedPane.remove(--tabCount);
                    if (tabCount == 0) {
                        ContainerView.this.remove(tabbedPane);
                        ContainerView.this.add(placeholderLabel, BorderLayout.CENTER);
                        ContainerView.this.updateUI();
                    }
                }
            });
            add(button);
        }
    }

}

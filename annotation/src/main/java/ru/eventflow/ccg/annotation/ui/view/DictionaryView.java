package ru.eventflow.ccg.annotation.ui.view;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.*;
import java.awt.*;

public class DictionaryView extends JPanel {

    public DictionaryView() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel("Dictionary");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        titleLabel.setForeground(Color.GRAY);

        JToggleButton clipboardBtn = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/clipboard.gif")));
        clipboardBtn.setToolTipText("Toggle View");
        clipboardBtn.setFocusable(false);

        final JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        headingPanel.add(clipboardBtn);
        add(headingPanel, BorderLayout.PAGE_START);

        // TODO refactor
        JPanel workareaPanel = new JPanel(new BorderLayout());

        final Object[] elements = new Object[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" };
        final JComboBox comboBox = new JComboBox();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AutoCompleteSupport support = AutoCompleteSupport.install(comboBox, GlazedLists.eventListOf(elements));
            }
        });
        System.out.println("Is editable - " + comboBox.isEditable() + ". Surprise!");

        workareaPanel.add(comboBox, BorderLayout.PAGE_START);

        workareaPanel.add(new JLabel(" "), BorderLayout.CENTER);



        JScrollPane scrollPane = new JScrollPane(workareaPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
    }
}

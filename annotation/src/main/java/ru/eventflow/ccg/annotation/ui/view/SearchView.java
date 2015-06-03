package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

// TODO JTreeTable howto: http://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/tech_topics/tables-trees/tables-trees.html
// part 2: http://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/tech_topics/tables_trees_2/tables_trees_2.html
public class SearchView extends JPanel {

    public SearchView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        JLabel label = new JLabel("search");
        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, BorderLayout.CENTER);
    }
}

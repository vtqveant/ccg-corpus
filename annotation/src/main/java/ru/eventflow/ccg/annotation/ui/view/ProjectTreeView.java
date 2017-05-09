package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.EmptyFolderTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class ProjectTreeView extends JPanel {

    private final JTree tree;

    public ProjectTreeView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 150));

        tree = new JTree(new DefaultTreeModel(null));
        tree.setEditable(false);
        tree.setDragEnabled(false);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setFont(Defaults.SMALL_FONT);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new EmptyFolderTreeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTree getTree() {
        return tree;
    }

}

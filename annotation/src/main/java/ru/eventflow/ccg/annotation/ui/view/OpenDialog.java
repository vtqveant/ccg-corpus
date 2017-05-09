package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.component.EmptyFolderTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class OpenDialog extends ModalDialog {

    private final JTree tree;

    public OpenDialog(Frame owner, String title) {
        super(owner, title);

        okButton.setEnabled(false);

        upperPanel.setLayout(new BorderLayout());

        tree = new JTree(new DefaultTreeModel(null));
        tree.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        tree.setEditable(false);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);
        tree.setDragEnabled(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new EmptyFolderTreeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(2, 2, MARGIN - 2, 2));

        upperPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public JTree getTree() {
        return tree;
    }
}

package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.project.*;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.DialogEventHandler;
import ru.eventflow.ccg.annotation.ui.event.FileSelectedEvent;
import ru.eventflow.ccg.annotation.ui.view.DialogType;
import ru.eventflow.ccg.annotation.ui.view.OpenDialog;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.List;

public class OpenDialogPresenter implements Presenter<JDialog>, DialogEventHandler,
        TreeWillExpandListener, TreeSelectionListener, ActionListener {

    private EventBus eventBus;
    private ProjectManager projectManager;
    private OpenDialog dialog;

    @Inject
    public OpenDialogPresenter(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.projectManager = new FileSystemProjectManager(new File(System.getProperty("user.home")));

        eventBus.addHandler(DialogEvent.TYPE, this);
    }

    @Override
    public void onEvent(DialogEvent event) {
        if (event.getDialogType() == DialogType.OPEN_PROJECT) {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(event.getSource());
            dialog = new OpenDialog(owner, "Open Project");

            // attach the root node when displaying the dialog
            ProjectElement rootElement = projectManager.getRootElement();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootElement);
            attachChildren(root);
            dialog.getTree().setModel(new DefaultTreeModel(root));
            dialog.getTree().collapsePath(new TreePath(root));

            dialog.getTree().addTreeWillExpandListener(this);
            dialog.getTree().addTreeSelectionListener(this);
            dialog.getTree().addKeyListener(new EnterKeyAdapter());
            dialog.getOkButton().addActionListener(this);

            dialog.setVisible(true);
        }
    }

    private void attachChildren(DefaultMutableTreeNode node) {
        ProjectElement element = (ProjectElement) node.getUserObject();
        List<ProjectElement> children = projectManager.getChildren(element);

        for (ProjectElement child : children) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            childNode.setUserObject(child);
            node.add(childNode);
        }
    }

    private void onProjectFolderSelected() {
        TreeSelectionModel selectionModel = dialog.getTree().getSelectionModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionModel.getSelectionPath().getLastPathComponent();
        ProjectElement element = (ProjectElement) node.getUserObject();
        File base = ((FileSystemProjectManager) projectManager).getProjectFolder();
        eventBus.fireEvent(new FileSelectedEvent(base, element, FileSelectedEvent.RequestType.PROJECT));
        dialog.dispose();
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        for (Enumeration children = node.children(); children.hasMoreElements(); ) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            attachChildren(child);
        }
    }

    // TODO this leads to re-reading from the file system, find a smarter approach
    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
        for (Enumeration children = node.children(); children.hasMoreElements(); ) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            child.removeAllChildren();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        onProjectFolderSelected();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        Object o = node.getUserObject();
        dialog.getOkButton().setEnabled(o instanceof FolderElement);
    }

    @Override
    public JDialog getView() {
        return dialog;
    }

    private class EnterKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER && dialog.getOkButton().isEnabled()) {
                onProjectFolderSelected();
            }
        }
    }

}

package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.project.*;
import ru.eventflow.ccg.annotation.project.FileSystemProjectManager;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.project.ProjectManager;
import ru.eventflow.ccg.annotation.ui.event.FileSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.FileSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.ProjectTreeView;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ProjectTreePresenter implements Presenter<ProjectTreeView>, FileSelectedEventHandler {

    private ProjectTreeView view;
    private EventBus eventBus;
    private ProjectManager projectManager;

    @Inject
    public ProjectTreePresenter(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.view = new ProjectTreeView();

        this.eventBus.addHandler(FileSelectedEvent.TYPE, this);

        // subscribe to events in the tree table
        view.getTree().addKeyListener(new EnterKeyAdapter());
        view.getTree().addMouseListener(new DoubleClickMouseAdapter());
    }

    private static void attach(ProjectManager projectManager, DefaultMutableTreeNode node, ProjectElement element) {
        node.setUserObject(element);
        for (ProjectElement child : projectManager.getChildren(element)) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
            attach(projectManager, childNode, child);
            node.add(childNode);
        }
    }

    @Override
    public ProjectTreeView getView() {
        return view;
    }

    private void select() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) view.getTree().getSelectionPath().getLastPathComponent();
        ProjectElement element = (ProjectElement) node.getUserObject();
        if (element instanceof FileElement) {
            File base = ((FileSystemProjectManager) projectManager).getProjectFolder();
            eventBus.fireEvent(new FileSelectedEvent(base, element, FileSelectedEvent.RequestType.FILE));
        }
    }

    /**
     * Load project selected elsewhere
     */
    @Override
    public void onEvent(FileSelectedEvent event) {
        if (event.getRequestType() == FileSelectedEvent.RequestType.PROJECT) {
            projectManager = new FileSystemProjectManager(new File(event.getBase(), event.getProjectElement().getPath()));
            ProjectElement projectElement = projectManager.getRootElement();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            attach(projectManager, root, projectElement);
            TreeModel model = new DefaultTreeModel(root);
            view.getTree().setModel(model);
            view.requestFocus();
        }
    }

    private class EnterKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                select();
            }
        }
    }

    private class DoubleClickMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() % 2 == 0)) {
                select();
            }
        }
    }

}

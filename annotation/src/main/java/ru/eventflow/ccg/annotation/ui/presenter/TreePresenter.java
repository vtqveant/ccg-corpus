package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.TreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Document;

import javax.inject.Inject;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreePresenter implements Presenter<TreeView> {

    private TreeView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public TreePresenter(final EventBus eventBus,
                         final DataManager dataManager) {
        this.view = new TreeView();
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        init();
    }

    private void init() {
        int count = 0;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) view.getTree().getModel().getRoot();
        for (Document document : dataManager.getAllDocuments()) {
            root.add(new DefaultMutableTreeNode(document));
            count++;
        }
        eventBus.fireEvent(new StatusUpdateEvent("fetched " + count + " entries"));

        // delegates the selected document to whom it may concern
        view.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) view.getTree().getLastSelectedPathComponent();
                if (node == null) return;
                Object o = node.getUserObject();
                if (o instanceof Document) {
                    Document document = (Document) node.getUserObject();
                    eventBus.fireEvent(new DocumentSelectedEvent(document));
                } else {
                    eventBus.fireEvent(new DocumentSelectedEvent(null));
                }
            }
        });
    }

    @Override
    public TreeView getView() {
        return view;
    }
}

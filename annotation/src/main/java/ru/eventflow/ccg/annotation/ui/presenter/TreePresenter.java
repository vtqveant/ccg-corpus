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
import java.util.HashMap;
import java.util.Map;

public class TreePresenter implements Presenter<TreeView> {

    private TreeView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public TreePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.view = new TreeView();
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        init();
    }

    private void init() {
        // init tree model
        Map<Integer, DefaultMutableTreeNode> nodes = new HashMap<Integer, DefaultMutableTreeNode>();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) view.getTree().getModel().getRoot();
        nodes.put(0, root);
        for (Document document : dataManager.getAllDocuments()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(document);
            nodes.put(document.getId(), node);
        }
        // because there's no order on documents
        for (Map.Entry<Integer, DefaultMutableTreeNode> entry : nodes.entrySet()) {
            Object o = entry.getValue().getUserObject();
            if (o instanceof Document) {
                Document current = (Document) o;
                nodes.get(current.getParentId()).add(entry.getValue());
            }
        }

        // delegates the selected document to whom it may concern
        view.getTree().getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
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

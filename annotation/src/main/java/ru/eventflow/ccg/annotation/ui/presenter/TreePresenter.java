package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.view.TreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Text;

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
        for (Text text : dataManager.getAllTexts()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(text);
            nodes.put(text.getId(), node);
        }
        // attach to parents -- because there's no order on documents
        for (Map.Entry<Integer, DefaultMutableTreeNode> entry : nodes.entrySet()) {
            Object o = entry.getValue().getUserObject();
            if (o instanceof Text) {
                Text current = (Text) o;
                if (current.getParent() != null) {
                    nodes.get(current.getParent().getId()).add(entry.getValue());
                } else {
                    nodes.get(0).add(entry.getValue());
                }
            }
        }

        // delegates the selected document to whom it may concern
        view.getTree().getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) view.getTree().getLastSelectedPathComponent();
                if (node == null) return;
                Object o = node.getUserObject();
                if (o instanceof Text) {
                    Text text = (Text) node.getUserObject();
                    eventBus.fireEvent(new TextSelectedEvent(text));
                } else {
                    eventBus.fireEvent(new TextSelectedEvent(null));
                }
            }
        });
    }

    @Override
    public TreeView getView() {
        return view;
    }
}

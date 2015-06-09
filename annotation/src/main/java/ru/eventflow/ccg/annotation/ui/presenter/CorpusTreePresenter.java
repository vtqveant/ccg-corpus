package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.model.CorpusTreeTableModel;
import ru.eventflow.ccg.annotation.ui.view.CorpusTreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import javax.inject.Inject;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.Map;

public class CorpusTreePresenter implements Presenter<CorpusTreeView>, TreeSelectionListener {

    private CorpusTreeView view;
    private EventBus eventBus;

    @Inject
    public CorpusTreePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;

        // init tree table model
        Map<Integer, DefaultMutableTreeNode> nodes = new HashMap<Integer, DefaultMutableTreeNode>();
        CorpusTreeTableModel model = new CorpusTreeTableModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
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

        view = new CorpusTreeView(model);
        view.getTreeTable().addTreeSelectionListener(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        TextSelectedEvent event;
        if (path == null) {
            event = new TextSelectedEvent(null);
        } else {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            event = new TextSelectedEvent((Text) node.getUserObject());
        }
        eventBus.fireEvent(event);
    }

    @Override
    public CorpusTreeView getView() {
        return view;
    }
}

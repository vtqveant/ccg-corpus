package ru.eventflow.ccg.annotation.ui.presenter;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.model.CorpusTreeTableModel;
import ru.eventflow.ccg.annotation.ui.view.CorpusTreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import javax.inject.Inject;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.Map;

public class CorpusTreePresenter implements Presenter<CorpusTreeView>, TreeSelectionListener {

    private CorpusTreeView view;
    private EventBus eventBus;

    @Inject
    public CorpusTreePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.view = new CorpusTreeView();

        // init tree table model
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();

        Map<Integer, DefaultMutableTreeTableNode> nodes = new HashMap<>();
        nodes.put(0, root);
        for (Text text : dataManager.getAllTexts()) {
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(text);
            nodes.put(text.getId(), node);
        }
        // attach to parents -- because there's no order on documents
        for (Map.Entry<Integer, DefaultMutableTreeTableNode> entry : nodes.entrySet()) {
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
        CorpusTreeTableModel model = (CorpusTreeTableModel) view.getTreeTable().getTreeTableModel();
        model.setRoot(root);

        view.getTreeTable().addTreeSelectionListener(this);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        TextSelectedEvent event;
        if (path == null) {
            event = new TextSelectedEvent(null);
        } else {
            DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) path.getLastPathComponent();
            event = new TextSelectedEvent((Text) node.getUserObject());
        }
        eventBus.fireEvent(event);
    }

    @Override
    public CorpusTreeView getView() {
        return view;
    }
}

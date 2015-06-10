package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.SearchEvent;
import ru.eventflow.ccg.annotation.ui.event.SearchEventHandler;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.model.LexiconEntry;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeTableModel;
import ru.eventflow.ccg.annotation.ui.view.LexiconTreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.Map;

public class LexiconTreePresenter implements Presenter<LexiconTreeView>, TreeSelectionListener, SearchEventHandler {

    private LexiconTreeView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public LexiconTreePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new LexiconTreeView();

        // wiring up the search panel
        SearchPresenter searchPresenter = new SearchPresenter(eventBus, this);
        this.view.addSearchPanel(searchPresenter.getView());
        this.eventBus.addHandler(SearchEvent.TYPE, this);

        // subscribe to events in the tree table
        view.getTreeTable().addTreeSelectionListener(this);
    }

    @Override
    public LexiconTreeView getView() {
        return view;
    }

    /**
     * rebuild tree view with new data, tree selection will change and fire on it's own
     */
    @Override
    public void onEvent(SearchEvent e) {
        if (e.getTarget() != this) return;

        String text = e.getInput();
        eventBus.fireEvent(new StatusUpdateEvent(text));

        LexiconTreeTableModel model = (LexiconTreeTableModel) view.getTreeTable().getTreeTableModel();
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        Map<Form, List<String>> grammemes = dataManager.getGrammemes(text);
        for (Map.Entry<Form, List<String>> entry : grammemes.entrySet()) {
            LexiconEntry form = new LexiconEntry(entry.getKey(), entry.getValue(), 0);
            MutableTreeTableNode node = new DefaultMutableTreeTableNode(form);
            root.add(node);
        }
        model.setRoot(root);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        if (path == null) {
            // no selection, let concordance view clear it's table
            eventBus.fireEvent(new FormSelectedEvent(null));
        } else {
            TreeTableNode node = (TreeTableNode) path.getLastPathComponent();
            LexiconEntry entry = (LexiconEntry) node.getUserObject();
            eventBus.fireEvent(new FormSelectedEvent(entry.getForm()));
        }
    }
}

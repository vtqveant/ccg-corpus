package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.SearchEvent;
import ru.eventflow.ccg.annotation.ui.event.SearchEventHandler;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeNode;
import ru.eventflow.ccg.annotation.ui.view.CategoryTreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.Map;

public class CategoryTreePresenter implements Presenter<CategoryTreeView>, TreeSelectionListener, SearchEventHandler {

    private CategoryTreeView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public CategoryTreePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new CategoryTreeView();

        // wiring up the search panel
        SearchPresenter searchPresenter = new SearchPresenter(eventBus, this);
        this.view.addSearchPanel(searchPresenter.getView());
        this.eventBus.addHandler(SearchEvent.TYPE, this);

        // subscribe to events in the tree table
        view.getTreeTable().addTreeSelectionListener(this);
    }

    @Override
    public CategoryTreeView getView() {
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

        // TODO refactor
        CategoryTreeView.LexiconTreeTableModel model = (CategoryTreeView.LexiconTreeTableModel) view.getTreeTable().getTreeTableModel();
        LexiconTreeNode root = (LexiconTreeNode) model.getRoot();
        root.getChildren().clear();

        Map<Form, List<String>> grammemes = dataManager.getGrammemes(text);
        for (Map.Entry<Form, List<String>> entry : grammemes.entrySet()) {
            LexiconTreeNode form = new LexiconTreeNode(entry.getKey(), entry.getValue(), 0);
            form.setLeaf(false);
            root.getChildren().add(form);
        }
        view.getTreeTable().getSelectionModel().clearSelection();
        view.getTreeTable().updateUI();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        if (path == null) {
            // no selection, let concordance view clear it's table
            eventBus.fireEvent(new FormSelectedEvent(null));
        } else {
            LexiconTreeNode node = (LexiconTreeNode) path.getLastPathComponent();
            eventBus.fireEvent(new FormSelectedEvent(node.getForm()));
        }
    }
}

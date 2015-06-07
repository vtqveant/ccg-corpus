package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeNode;
import ru.eventflow.ccg.annotation.ui.view.SearchView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class SearchPresenter implements Presenter<SearchView>, ActionListener, TreeSelectionListener {

    private SearchView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public SearchPresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new SearchView();

        view.getSearchField().addActionListener(this);
        view.getSearchBtn().addActionListener(this);
        view.getTreeTable().addTreeSelectionListener(this);
    }

    @Override
    public SearchView getView() {
        return view;
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

    /**
     * rebuild tree view with new data, tree selection will change and fire on it's own
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String text = view.getSearchField().getText();
        eventBus.fireEvent(new StatusUpdateEvent(text));

        // TODO refactor
        SearchView.LexiconTreeTableModel model = (SearchView.LexiconTreeTableModel) view.getTreeTable().getTreeTableModel();
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
}

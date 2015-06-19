package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.SearchEvent;
import ru.eventflow.ccg.annotation.ui.event.SearchEventHandler;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.model.AbstractLexiconEntry;
import ru.eventflow.ccg.annotation.ui.model.CategoryEntry;
import ru.eventflow.ccg.annotation.ui.model.LexiconEntry;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeTableModel;
import ru.eventflow.ccg.annotation.ui.view.LexiconTreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

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

        String input = e.getInput();
        eventBus.fireEvent(new StatusUpdateEvent(input));

        LexiconTreeTableModel model = (LexiconTreeTableModel) view.getTreeTable().getTreeTableModel();
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        Map<Form, List<String>> map = dataManager.getGrammemes(input);

        for (Map.Entry<Form, List<String>> grammemesList : map.entrySet()) {
            LexiconEntry lexEntry = new LexiconEntry(grammemesList.getKey(), grammemesList.getValue(), 0);
            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(lexEntry);

            // subcategorization
            for (Category category : grammemesList.getKey().getCategories()) {
                CategoryEntry catEntry = new CategoryEntry(grammemesList.getKey(), grammemesList.getValue(), category, 0);
                DefaultMutableTreeTableNode catNode = new DefaultMutableTreeTableNode(catEntry);
                catNode.setAllowsChildren(false);
                node.add(catNode);
            }
            root.add(node);
        }
        model.setRoot(root);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        TreePath path = e.getNewLeadSelectionPath();
        if (path != null) {
            TreeTableNode node = (TreeTableNode) path.getLastPathComponent();
            AbstractLexiconEntry synCatEntry = (AbstractLexiconEntry) node.getUserObject();
            eventBus.fireEvent(new FormSelectedEvent(synCatEntry.getForm(), synCatEntry.getCategory()));
        } else {
            // let concordance view clear it's table (either no selection or not a leaf)
            eventBus.fireEvent(new FormSelectedEvent(null, null));
        }
    }
}

package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeNode;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeTableModel;
import ru.eventflow.ccg.annotation.ui.view.SearchView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http://stackoverflow.com/questions/13517980/how-i-handle-keypress-event-for-jcombobox-in-java
 */
public class SearchPresenter implements Presenter<SearchView>, ActionListener {

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
    }

    @Override
    public SearchView getView() {
        return view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = view.getSearchField().getText();
        eventBus.fireEvent(new StatusUpdateEvent(text));

        // TODO refactor
        LexiconTreeTableModel model = (LexiconTreeTableModel) view.getTreeTable().getTreeTableModel();
        LexiconTreeNode root = (LexiconTreeNode) model.getRoot();
        root.getChildren().clear();

        Map<Form, List<String>> grammemes = dataManager.getGrammemes(text);
        for (Map.Entry<Form, List<String>> entry : grammemes.entrySet()) {
            Form f = entry.getKey();
            String orthography = f.getOrthography();
            String lemma = f.getLexeme().getLemma().getOrthography();
            LexiconTreeNode form = new LexiconTreeNode(orthography, lemma, entry.getValue(), 0);
            form.setLeaf(false);
            root.getChildren().add(form);
        }
        view.getTreeTable().updateUI();
    }
}

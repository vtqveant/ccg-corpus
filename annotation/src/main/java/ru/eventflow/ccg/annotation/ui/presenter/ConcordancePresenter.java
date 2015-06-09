package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.component.LazyJTableDataSource;
import ru.eventflow.ccg.annotation.ui.event.*;
import ru.eventflow.ccg.annotation.ui.view.ConcordanceView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ConcordancePresenter implements Presenter<ConcordanceView>, FormSelectedEventHandler, SearchEventHandler {

    private final EventBus eventBus;
    private final DataManager dataManager;
    private ConcordanceView view;
    private final SearchPresenter searchPresenter;

    @Inject
    public ConcordancePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new ConcordanceView();

        searchPresenter = new SearchPresenter(eventBus, this);
        this.view.addSearchPanel(searchPresenter.getView());
        this.eventBus.addHandler(SearchEvent.TYPE, this);

        this.eventBus.addHandler(FormSelectedEvent.TYPE, this);

        // open tab with sentence on double click
        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    Point point = e.getPoint();
                    int idx = table.rowAtPoint(point);
                    int modelIdx = table.convertRowIndexToModel(idx);
                    int sentenceId = (int) table.getModel().getValueAt(modelIdx, 2);
                    Sentence sentence = dataManager.getSentenceById(sentenceId);
                    eventBus.fireEvent(new TabEvent(sentence));
                }
            }
        });
    }

    @Override
    public void onEvent(FormSelectedEvent event) {
        Form form = event.getForm();
        if (form != null) {
            List<Sentence> sentences = dataManager.getSentencesByFormId(form.getId());
            LazyJTableDataSource dataSource = new ConcordanceLazyTableDataSource(sentences, form);
            view.setDataSource(dataSource);
        } else {
            view.setDataSource(null); // clears the concordance table
        }
        searchPresenter.clear(); // clears the search panel
    }

    @Override
    public void onEvent(SearchEvent e) {
        if (e.getTarget() != this) return;

        String text = e.getInput();
        eventBus.fireEvent(new StatusUpdateEvent(text));

        // TODO implement actual search
    }

    @Override
    public ConcordanceView getView() {
        return view;
    }

}

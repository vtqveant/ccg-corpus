package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.component.LazyJTableDataSource;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.FormSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.view.ConcordanceView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ConcordancePresenter implements Presenter<ConcordanceView>, FormSelectedEventHandler {

    private final EventBus eventBus;
    private final DataManager dataManager;
    private ConcordanceView view;

    @Inject
    public ConcordancePresenter(final EventBus eventBus, final DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        this.view = new ConcordanceView();

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
    }

    @Override
    public ConcordanceView getView() {
        return view;
    }

}

package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.TextView;
import ru.eventflow.ccg.datasource.model.corpus.Paragraph;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;

public class TextPresenter implements Presenter<TextView>, TextSelectedEventHandler {

    private TextView view;
    private EventBus eventBus;

    @Inject
    public TextPresenter(final EventBus eventBus) {
        this.view = new TextView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.eventBus.addHandler(TextSelectedEvent.TYPE, this);

        this.view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();
                int row = selectionModel.getMinSelectionIndex();
                if (row != -1) {
                    String id = view.getTable().getValueAt(row, 0).toString();
                    eventBus.fireEvent(new StatusUpdateEvent(id));
                } else {
                    eventBus.fireEvent(new StatusUpdateEvent("no selection"));
                }
            }
        });

        this.view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    TextView.SentenceTableModel model = (TextView.SentenceTableModel) view.getTable().getModel();
                    int idx = table.convertRowIndexToModel(row);
                    Sentence sentence = model.getSentences().get(idx);
                    eventBus.fireEvent(new TabEvent(sentence));
                }
            }
        });
    }

    @Override
    public TextView getView() {
        return view;
    }

    @Override
    public void onEvent(TextSelectedEvent e) {
        TextView.SentenceTableModel model = (TextView.SentenceTableModel) view.getTable().getModel();
        model.getSentences().clear();
        view.getTable().getSelectionModel().clearSelection();
        if (e.getText() != null) { // otherwise it's a root node
            for (Paragraph p : e.getText().getParagraphs()) {
                model.getSentences().addAll(p.getSentences());
            }
            Collections.sort(model.getSentences(), new Comparator<Sentence>() {
                @Override
                public int compare(Sentence o1, Sentence o2) {
                    return o1.getId() - o2.getId();
                }
            });
        }
        view.getTable().updateUI();
    }
}

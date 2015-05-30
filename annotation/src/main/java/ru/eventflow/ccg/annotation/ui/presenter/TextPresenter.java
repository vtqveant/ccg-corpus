package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.TextView;
import ru.eventflow.ccg.datasource.model.corpus.Paragraph;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextPresenter implements Presenter<TextView> {

    private TextView view;
    private EventBus eventBus;

    @Inject
    public TextPresenter(final EventBus eventBus) {
        this.view = new TextView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.eventBus.addHandler(TextSelectedEvent.TYPE, new TextSelectedEventHandler() {
            @Override
            public void onEvent(TextSelectedEvent e) {
                view.getSentences().clear();
                view.getTable().getSelectionModel().clearSelection();
                if (e.getText() == null) return;  // this is the root node
                for (Paragraph p : e.getText().getParagraphs()) {
                    view.getSentences().addAll(p.getSentences());
                }
            }
        });

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
                    int id = (int) view.getTable().getValueAt(row, 0);
                    eventBus.fireEvent(new TabEvent(id));
                }
            }
        });
    }

    @Override
    public TextView getView() {
        return view;
    }
}

package ru.eventflow.annotation.ui.presenter;

import ru.eventflow.annotation.EventBus;
import ru.eventflow.annotation.data.DataManager;
import ru.eventflow.annotation.model.Document;
import ru.eventflow.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.annotation.ui.event.LogEvent;
import ru.eventflow.annotation.ui.view.DocumentsView;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DocumentsPresenter implements Presenter<DocumentsView> {

    private DocumentsView view;
    private EventBus eventBus;

    @Inject
    public DocumentsPresenter(final DocumentsView view, final EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        for (Document document : DataManager.getAllDocuments()) {
            view.getModel().addElement(document);
        }
        eventBus.fireEvent(new LogEvent("fetched " + view.getModel().size() + " entries"));

        // delegates the selected document to whom it may concern
        view.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                Document document = view.getModel().get(lsm.getMinSelectionIndex());
                eventBus.fireEvent(new DocumentSelectedEvent(document));
            }
        });
    }

    @Override
    public DocumentsView getView() {
        return view;
    }
}

package ru.eventflow.annotation.ui.presenter;

import ru.eventflow.annotation.EventBus;
import ru.eventflow.ccgbank.data.DataManager;
import ru.eventflow.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.annotation.ui.view.DocumentsView;
import ru.eventflow.ccgbank.model.Document;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DocumentsPresenter implements Presenter<DocumentsView> {

    private DocumentsView view;
    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public DocumentsPresenter(final DocumentsView view,
                              final EventBus eventBus,
                              final DataManager dataManager) {
        this.view = view;
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        init();
    }

    private void init() {
        for (Document document : dataManager.getAllDocuments()) {
            view.getModel().addElement(document);
        }
        eventBus.fireEvent(new StatusUpdateEvent("fetched " + view.getModel().size() + " entries"));

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

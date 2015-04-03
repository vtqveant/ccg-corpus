package ru.eventflow.annotation.presenter;

import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.DocumentSelectedEvent;
import ru.eventflow.annotation.event.LogEvent;
import ru.eventflow.annotation.view.DocumentsView;
import ru.eventflow.fts.datasource.Document;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

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
        EntityManager em = Persistence.createEntityManagerFactory("fts-datasource").createEntityManager();
        List<Document> documents = em.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();

        for (Document document : documents) {
            view.getModel().addElement(document);
        }
        eventBus.fireEvent(new LogEvent("fetched " + documents.size() + " entries"));

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

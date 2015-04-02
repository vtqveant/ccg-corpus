package ru.eventflow.annotation.presenter;

import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.view.DocumentsView;
import ru.eventflow.fts.datasource.Document;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;

public class DocumentsPresenter implements Presenter<DocumentsView> {

    private DocumentsView view;

    private EventBus eventBus;

    @Inject
    public DocumentsPresenter(DocumentsView view, EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        EntityManager em = Persistence.createEntityManagerFactory("fts-datasource").createEntityManager();
        java.util.List<Document> documents = em.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();

        DefaultListModel<Document> documentListModel = new DefaultListModel<Document>();
        for (Document document : documents) {
            documentListModel.addElement(document);
        }

        view.setModel(documentListModel);
    }

    @Override
    public DocumentsView getView() {
        return view;
    }
}

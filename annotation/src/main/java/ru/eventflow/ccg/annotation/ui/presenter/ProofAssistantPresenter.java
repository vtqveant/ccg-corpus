package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DocumentMarkedEvent;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.ProofAssistantView;
import ru.eventflow.ccg.datasource.model.corpus.Document;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class ProofAssistantPresenter implements Presenter<ProofAssistantView> {

    private ProofAssistantView view;
    private EventBus eventBus;

    /**
     * currently selected document
     */
    private Document document;

    @Inject
    public ProofAssistantPresenter(final EventBus eventBus) {
        this.view = new ProofAssistantView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        // publish events from the view
        this.view.getRelevantBtn().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new StatusUpdateEvent("doc id = " + document.getId() + " marked relevant"));
                eventBus.fireEvent(new DocumentMarkedEvent(document, true));
            }
        });

        this.view.getNonrelevantBtn().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new StatusUpdateEvent("doc id = " + document.getId() + " marked nonrelevant"));
                eventBus.fireEvent(new DocumentMarkedEvent(document, false));
            }
        });

        // subscribe to events from the event bus
        this.eventBus.addHandler(DocumentSelectedEvent.TYPE, new DocumentSelectedEventHandler() {
            @Override
            public void onEvent(DocumentSelectedEvent e) {
                document = e.getDocument();
                view.getRelevantBtn().setEnabled(true);
                view.getNonrelevantBtn().setEnabled(true);
            }
        });
    }

    @Override
    public ProofAssistantView getView() {
        return view;
    }
}

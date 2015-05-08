package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.*;
import ru.eventflow.ccg.annotation.ui.view.MainView;
import ru.eventflow.ccg.model.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainPresenter implements Presenter<MainView> {

    private MainView view;
    private EventBus eventBus;

    /**
     * currently selected document
     */
    private Document document;

    @Inject
    public MainPresenter(final EventBus eventBus) {
        this.view = new MainView();
        this.eventBus = eventBus;
        init();
        this.eventBus.fireEvent(new StatusUpdateEvent("MainPresenter initialized"));
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

        this.eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.getStatusLabel().setText(e.getMessage());
            }
        });
    }

    @Override
    public MainView getView() {
        return view;
    }
}

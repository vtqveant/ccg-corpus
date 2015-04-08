package ru.eventflow.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.annotation.EventBus;
import ru.eventflow.annotation.model.Document;
import ru.eventflow.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.annotation.ui.event.DocumentSelectedEventHandler;
import ru.eventflow.annotation.ui.event.LogEvent;
import ru.eventflow.annotation.ui.event.LogEventHandler;
import ru.eventflow.annotation.ui.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainPresenter implements Presenter<MainView> {

    private MainView view;
    private EventBus eventBus;
    private Document currentDoc;

    @Inject
    public MainPresenter(final MainView view, final EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;

        // publish events from the view
        this.view.getRelevantBtn().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new LogEvent("doc id = " + currentDoc.getId() + " marked relevant"));
            }
        });
        this.view.getNonrelevantBtn().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new LogEvent("doc id = " + currentDoc.getId() + " marked nonrelevant"));
            }
        });

        // subscribe to events from the event bus
        this.eventBus.addHandler(DocumentSelectedEvent.TYPE, new DocumentSelectedEventHandler() {
            @Override
            public void onEvent(DocumentSelectedEvent e) {
                currentDoc = e.getDoc();
                view.getRelevantBtn().setEnabled(true);
                view.getNonrelevantBtn().setEnabled(true);
            }
        });
        this.eventBus.addHandler(LogEvent.TYPE, new LogEventHandler() {
            @Override
            public void onEvent(LogEvent e) {
                view.getStatusLabel().setText(e.getMessage());
            }
        });

        this.eventBus.fireEvent(new LogEvent("MainPresenter initialized"));
    }

    @Override
    public MainView getView() {
        return view;
    }
}

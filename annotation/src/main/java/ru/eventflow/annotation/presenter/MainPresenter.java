package ru.eventflow.annotation.presenter;

import com.google.inject.Inject;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.DocumentSelectedEvent;
import ru.eventflow.annotation.event.DocumentSelectedEventHandler;
import ru.eventflow.annotation.event.LogEvent;
import ru.eventflow.annotation.model.Document;
import ru.eventflow.annotation.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainPresenter implements Presenter<MainView> {

    private MainView view;
    private EventBus eventBus;
    private Document currentDocument;

    @Inject
    public MainPresenter(final MainView view, final EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;

        // publish
        this.view.getRelevantBtn().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new LogEvent("doc id = " + currentDocument.getId() + " marked relevant"));
            }
        });
        this.view.getNonrelevantBtn().addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new LogEvent("doc id = " + currentDocument.getId() + " marked nonrelevant"));
            }
        });

        // subscribe
        this.eventBus.addHandler(DocumentSelectedEvent.TYPE, new DocumentSelectedEventHandler() {
            @Override
            public void onEvent(DocumentSelectedEvent e) {
                currentDocument = e.getDocument();
                view.getRelevantBtn().setEnabled(true);
                view.getNonrelevantBtn().setEnabled(true);
            }
        });

        this.eventBus.fireEvent(new LogEvent("MainPresenter initialized"));
    }

    @Override
    public MainView getView() {
        return view;
    }
}

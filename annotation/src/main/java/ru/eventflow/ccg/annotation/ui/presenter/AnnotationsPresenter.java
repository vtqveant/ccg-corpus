package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEventHandler;
import ru.eventflow.ccg.annotation.ui.view.AnnotationsView;

import javax.swing.*;

public class AnnotationsPresenter implements Presenter<AnnotationsView> {

    private final AnnotationsView view;

    @Inject
    public AnnotationsPresenter(final EventBus eventBus) {
        this.view = new AnnotationsView();

        eventBus.addHandler(TabEvent.TYPE, new TabEventHandler() {
            @Override
            public void onEvent(TabEvent e) {
                view.addTab("FTS Annotation", new JLabel("sdfsd"));
            }
        });
    }

    @Override
    public AnnotationsView getView() {
        return view;
    }
}

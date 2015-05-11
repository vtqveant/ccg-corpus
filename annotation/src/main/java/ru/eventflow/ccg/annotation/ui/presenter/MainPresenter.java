package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;
import ru.eventflow.ccg.annotation.ui.event.ToggleFTSAnnotationEvent;
import ru.eventflow.ccg.annotation.ui.view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPresenter implements Presenter<MainView> {

    private MainView view;
    private EventBus eventBus;

    @Inject
    public MainPresenter(final EventBus eventBus) {
        this.view = new MainView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.getStatusLabel().setText(e.getMessage());
            }
        });

        view.getAnnotationBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new ToggleFTSAnnotationEvent());
            }
        });
    }

    @Override
    public MainView getView() {
        return view;
    }
}

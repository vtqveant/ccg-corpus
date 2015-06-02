package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;
import ru.eventflow.ccg.annotation.ui.view.EventLogView;

public class EventLogPresenter implements Presenter<EventLogView> {

    private EventLogView view;

    @Inject
    public EventLogPresenter(final EventBus eventBus) {
        this.view = new EventLogView();

        eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.addRecord(e.getMessage());
            }
        });
    }

    @Override
    public EventLogView getView() {
        return view;
    }
}

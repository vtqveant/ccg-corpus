package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;
import ru.eventflow.ccg.annotation.ui.view.MessagesView;

public class MessagesPresenter implements Presenter<MessagesView> {

    private MessagesView view;

    @Inject
    public MessagesPresenter(final EventBus eventBus) {
        this.view = new MessagesView();

        eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.addRecord(e.getMessage());
            }
        });
    }

    @Override
    public MessagesView getView() {
        return view;
    }
}

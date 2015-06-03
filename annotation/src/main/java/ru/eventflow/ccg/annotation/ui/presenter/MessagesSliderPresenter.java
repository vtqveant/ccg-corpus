package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;
import ru.eventflow.ccg.annotation.ui.view.MessagesSliderView;

public class MessagesSliderPresenter implements Presenter<MessagesSliderView> {

    private MessagesSliderView view;

    @Inject
    public MessagesSliderPresenter(final EventBus eventBus) {
        this.view = new MessagesSliderView();

        eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.addRecord(e.getMessage());
            }
        });
    }

    @Override
    public MessagesSliderView getView() {
        return view;
    }
}

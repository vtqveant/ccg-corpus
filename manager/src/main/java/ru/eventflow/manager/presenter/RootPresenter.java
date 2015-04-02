package ru.eventflow.manager.presenter;

import com.pennychecker.eventbus.HandlerManager;
import ru.eventflow.manager.event.LogEvent;
import ru.eventflow.manager.view.RootView;

public class RootPresenter implements Presenter {

    private RootView view;
    private HandlerManager eventBus;

    public RootPresenter(RootView view, HandlerManager eventBus) {
        this.view = view;
        this.eventBus = eventBus;

        eventBus.fireEvent(new LogEvent("RootPresenter initialized"));
    }
}

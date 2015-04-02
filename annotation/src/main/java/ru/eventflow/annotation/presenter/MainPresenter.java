package ru.eventflow.annotation.presenter;

import com.google.inject.Inject;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.LogEvent;
import ru.eventflow.annotation.view.MainView;

public class MainPresenter implements Presenter<MainView> {

    private MainView view;

    private EventBus eventBus;

    @Inject
    public MainPresenter(MainView view, EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;
        eventBus.fireEvent(new LogEvent("RootPresenter initialized"));
    }

    @Override
    public MainView getView() {
        return view;
    }
}

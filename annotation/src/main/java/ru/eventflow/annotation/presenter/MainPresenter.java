package ru.eventflow.annotation.presenter;

import com.google.inject.Inject;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.LogEvent;
import ru.eventflow.annotation.view.MainView;

public class MainPresenter implements Presenter<MainView> {

    private MainView view;

    private EventBus eventBus;

    private DocumentsPresenter documentsPresenter;

    @Inject
    public MainPresenter(MainView view, EventBus eventBus,
                         DocumentsPresenter documentsPresenter, DetailsPresenter detailsPresenter) {
        this.view = view;
        this.eventBus = eventBus;
        this.documentsPresenter = documentsPresenter;
        view.setLeftComponent(documentsPresenter.getView());
        view.setRightComponent(detailsPresenter.getView());

        eventBus.fireEvent(new LogEvent("MainPresenter initialized"));
    }

    @Override
    public MainView getView() {
        return view;
    }
}

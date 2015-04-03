package ru.eventflow.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.presenter.DetailsPresenter;
import ru.eventflow.annotation.presenter.DocumentsPresenter;
import ru.eventflow.annotation.presenter.MainPresenter;
import ru.eventflow.annotation.view.DetailsView;
import ru.eventflow.annotation.view.DocumentsView;
import ru.eventflow.annotation.view.MainView;

public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);
        bind(LoggingController.class).in(Singleton.class);

        bind(MainPresenter.class).in(Singleton.class);
        bind(DocumentsPresenter.class).in(Singleton.class);
        bind(DetailsPresenter.class).in(Singleton.class);

        bind(MainView.class).in(Singleton.class);
        bind(DocumentsView.class).in(Singleton.class);
        bind(DetailsView.class).in(Singleton.class);
    }
}

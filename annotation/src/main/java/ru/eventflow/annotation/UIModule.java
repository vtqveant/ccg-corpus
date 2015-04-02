package ru.eventflow.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.view.DocumentsView;
import ru.eventflow.annotation.view.MainView;

public class UIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);
        bind(LoggingController.class).in(Singleton.class);

        bind(MainView.class).in(Singleton.class);
        bind(DocumentsView.class).in(Singleton.class);
    }
}

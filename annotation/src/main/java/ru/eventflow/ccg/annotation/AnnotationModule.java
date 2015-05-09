package ru.eventflow.ccg.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import ru.eventflow.ccg.annotation.ui.presenter.*;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.DataManagerImpl;
import ru.eventflow.ccg.datasource.DataSource;

public class AnnotationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataManager.class).toProvider(DataManagerProvider.class).in(Singleton.class);

        bind(EventBus.class).in(Singleton.class);

        bind(LoggingController.class).in(Singleton.class);
        bind(DataAccessController.class).in(Singleton.class);

        bind(MainPresenter.class).in(Singleton.class);
        bind(DocumentsPresenter.class).in(Singleton.class);
        bind(DetailsPresenter.class).in(Singleton.class);
        bind(MenuPresenter.class).in(Singleton.class);
    }

    private static class DataManagerProvider implements Provider<DataManager> {
        @Override
        public DataManager get() {
            return new DataManagerImpl(DataSource.DEFAULT);
        }
    }
}

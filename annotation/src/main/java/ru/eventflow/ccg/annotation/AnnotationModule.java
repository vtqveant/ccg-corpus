package ru.eventflow.ccg.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import ru.eventflow.ccg.annotation.ui.presenter.DetailsPresenter;
import ru.eventflow.ccg.annotation.ui.presenter.DocumentsPresenter;
import ru.eventflow.ccg.annotation.ui.presenter.MainPresenter;
import ru.eventflow.ccg.annotation.ui.presenter.MenuPresenter;
import ru.eventflow.ccg.annotation.ui.view.DetailsView;
import ru.eventflow.ccg.annotation.ui.view.DocumentsView;
import ru.eventflow.ccg.annotation.ui.view.MainView;
import ru.eventflow.ccg.annotation.ui.view.MenuView;
import ru.eventflow.ccg.data.DataManager;
import ru.eventflow.ccg.data.DataManagerImpl;

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

        bind(MainView.class).in(Singleton.class);
        bind(DocumentsView.class).in(Singleton.class);
        bind(DetailsView.class).in(Singleton.class);
        bind(MenuView.class).in(Singleton.class);
    }

    private static class DataManagerProvider implements Provider<DataManager> {
        @Override
        public DataManager get() {
            return new DataManagerImpl("h2-openjpa");
        }
    }
}

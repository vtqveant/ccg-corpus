package ru.eventflow.ccg.annotation;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.presenter.*;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.DataManagerImpl;

import java.io.IOException;
import java.util.Properties;

public class GUIModule extends AbstractModule {

    @Override
    protected void configure() {
        Names.bindProperties(binder(), getProperties());

        bind(EventBus.class).in(Singleton.class);
        bind(DataManager.class).toProvider(new DataManagerProvider()).in(Singleton.class); // pg

        bind(LoggingController.class).in(Singleton.class);

        bind(MainPresenter.class).in(Singleton.class);
        bind(CorpusTreePresenter.class).in(Singleton.class);
        bind(TextPresenter.class).in(Singleton.class);
        bind(MenuPresenter.class).in(Singleton.class);
        bind(TabsPresenter.class).in(Singleton.class);
        bind(MessagesSliderPresenter.class).in(Singleton.class);
        bind(LexiconSliderPresenter.class).in(Singleton.class);
        bind(ConcordancePresenter.class).in(Singleton.class);

    }

    private Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/icarus.properties"));
        } catch (IOException e) {
            addError(e);
        }
        return properties;
    }

    private static class DataManagerProvider implements Provider<DataManager> {
        @Override
        public DataManager get() {
            return new DataManagerImpl("pg-openjpa");
        }
    }

}

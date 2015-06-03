package ru.eventflow.ccg.annotation.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.mockito.Mockito;
import ru.eventflow.ccg.annotation.DataAccessController;
import ru.eventflow.ccg.annotation.ui.view.CorpusTreeView;
import ru.eventflow.ccg.datasource.DataManager;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import java.util.ArrayList;

public class MockAnnotationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataManager.class).toProvider(DataManagerProvider.class).in(Singleton.class);
        bind(DataAccessController.class).in(Singleton.class);
        bind(CorpusTreeView.class).in(Singleton.class);
    }

    private static class DataManagerProvider implements Provider<DataManager> {
        @Override
        public DataManager get() {
            DataManager dataManager = Mockito.mock(DataManager.class);
            Mockito.when(dataManager.getAllTexts()).thenReturn(new ArrayList<Text>());
            return dataManager;
        }
    }

}

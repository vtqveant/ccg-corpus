package ru.eventflow.annotation.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.mockito.Mockito;
import ru.eventflow.annotation.DataAccessController;
import ru.eventflow.annotation.data.DataManager;
import ru.eventflow.annotation.model.Document;
import ru.eventflow.annotation.ui.view.DocumentsView;

import java.util.ArrayList;

public class MockAnnotationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataManager.class).toProvider(DataManagerProvider.class).in(Singleton.class);
        bind(DataAccessController.class).in(Singleton.class);
        bind(DocumentsView.class).in(Singleton.class);
    }

    private static class DataManagerProvider implements Provider<DataManager> {
        @Override
        public DataManager get() {
            DataManager dataManager = Mockito.mock(DataManager.class);
            Mockito.when(dataManager.getAllDocuments()).thenReturn(new ArrayList<Document>());
            return dataManager;
        }
    }

}

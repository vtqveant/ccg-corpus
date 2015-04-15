package ru.eventflow.annotation.ui;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import ru.eventflow.annotation.ui.view.DocumentsView;

public class MockAnnotationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DocumentsView.class).toProvider(DocumentsViewProvider.class).in(Singleton.class);
    }

    private static class DocumentsViewProvider implements Provider<DocumentsView> {
        @Override
        public DocumentsView get() {
            return new DocumentsView();
        }
    }
}

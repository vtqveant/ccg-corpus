package ru.eventflow.annotation.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import ru.eventflow.annotation.DataAccessController;
import ru.eventflow.annotation.EventBus;
import ru.eventflow.annotation.ui.event.DocumentMarkedEvent;
import ru.eventflow.annotation.ui.presenter.DocumentsPresenter;
import ru.eventflow.ccgbank.model.Document;

public class DocumentsPresenterTest {

    private DocumentsPresenter documentsPresenter;
    private EventBus eventBus;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MockAnnotationModule());
        eventBus = injector.getInstance(EventBus.class);
        documentsPresenter = injector.getInstance(DocumentsPresenter.class);
    }

    @Test
    public void testMarkDocument() {
        Document document = new Document(0, "test", "test");
        documentsPresenter.getView().getModel().addElement(document);

        eventBus.fireEvent(new DocumentMarkedEvent(document, true));
        eventBus.fireEvent(new DocumentMarkedEvent(document, false));
    }
}
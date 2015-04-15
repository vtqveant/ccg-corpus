package ru.eventflow.annotation.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import ru.eventflow.annotation.EventBus;
import ru.eventflow.annotation.ui.presenter.DocumentsPresenter;
import ru.eventflow.annotation.ui.view.DocumentsView;

public class DocumentsPresenterTest {

    private DocumentsPresenter documentsPresenter;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MockAnnotationModule());
        DocumentsView mockView = injector.getInstance(DocumentsView.class);
        EventBus mockEventBus = injector.getInstance(EventBus.class);
        documentsPresenter = new DocumentsPresenter(mockView, mockEventBus);
    }

    @Test
    public void testCreate() {
        documentsPresenter.getView();
    }
}

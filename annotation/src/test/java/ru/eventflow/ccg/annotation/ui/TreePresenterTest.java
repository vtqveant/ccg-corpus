package ru.eventflow.ccg.annotation.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.presenter.CorpusTreePresenter;

public class TreePresenterTest {

    private CorpusTreePresenter corpusTreePresenter;
    private EventBus eventBus;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MockAnnotationModule());
        eventBus = injector.getInstance(EventBus.class);
        corpusTreePresenter = injector.getInstance(CorpusTreePresenter.class);
    }

}

package ru.eventflow.ccg.annotation.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.presenter.TreePresenter;

public class TreePresenterTest {

    private TreePresenter treePresenter;
    private EventBus eventBus;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MockAnnotationModule());
        eventBus = injector.getInstance(EventBus.class);
        treePresenter = injector.getInstance(TreePresenter.class);
    }

}

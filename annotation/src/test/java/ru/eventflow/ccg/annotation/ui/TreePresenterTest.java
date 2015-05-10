package ru.eventflow.ccg.annotation.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DocumentMarkedEvent;
import ru.eventflow.ccg.annotation.ui.presenter.TreePresenter;
import ru.eventflow.ccg.datasource.model.corpus.Document;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class TreePresenterTest {

    private TreePresenter treePresenter;
    private EventBus eventBus;

    @Before
    public void setUp() {
        Injector injector = Guice.createInjector(new MockAnnotationModule());
        eventBus = injector.getInstance(EventBus.class);
        treePresenter = injector.getInstance(TreePresenter.class);
    }

    @Test
    public void testMarkDocument() {
        Document document = new Document(0, "test", 0, "test", "test");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePresenter.getView().getTree().getModel().getRoot();
        node.add(new DefaultMutableTreeNode(document));

        eventBus.fireEvent(new DocumentMarkedEvent(document, true));
        eventBus.fireEvent(new DocumentMarkedEvent(document, false));
    }
}

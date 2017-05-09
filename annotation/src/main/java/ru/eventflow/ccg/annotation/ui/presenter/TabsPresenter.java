package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.project.FileElement;
import ru.eventflow.ccg.annotation.project.FileSystemProjectManager;
import ru.eventflow.ccg.annotation.ui.event.EditorCaretEvent;
import ru.eventflow.ccg.annotation.ui.event.FileSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.FileSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.model.ContentModel;
import ru.eventflow.ccg.annotation.ui.model.ContentType;
import ru.eventflow.ccg.annotation.ui.view.FileEditorView;
import ru.eventflow.ccg.annotation.ui.view.TabsView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class TabsPresenter implements Presenter<TabsView>, FileSelectedEventHandler {

    private final TabsView view;
    private final EventBus eventBus;

    @Inject
    public TabsPresenter(final EventBus eventBus) {
        this.view = new TabsView();
        this.eventBus = eventBus;

        this.view.getTabbedPane().addChangeListener((ChangeEvent e) -> {
            JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
            if (tabbedPane.getTabCount() == 0) {
                eventBus.fireEvent(new EditorCaretEvent(-1, -1));
            } else {
                FileEditorView selected = (FileEditorView) tabbedPane.getSelectedComponent();
                selected.getTextPane().requestFocusInWindow();
            }
        });

        eventBus.addHandler(FileSelectedEvent.TYPE, this);
    }

    private static String resolveContentType(String name) {
        if (!name.contains(".") || name.lastIndexOf('.') == name.length() - 1) {
            return ContentType.TEXT_PLAIN;
        }

        String extension = name.substring(name.lastIndexOf('.') + 1);
        String contentType;
        switch (extension) {
            case "sparql": // break omitted
            case "rq":
                contentType = ContentType.TEXT_SPARQL;
                break;
            case "properties":
                contentType = ContentType.TEXT_PROPERTIES;
                break;
            case "json":
                contentType = ContentType.APPLICATION_JSON;
                break;
            case "jsonld":
                contentType = ContentType.APPLICATION_JSON_LD;
                break;
            case "js":
                contentType = ContentType.APPLICATION_JAVASCRIPT;
                break;
            case "xml":
                contentType = ContentType.TEXT_XML;
                break;
            default:
                contentType = ContentType.TEXT_PLAIN;
        }
        return contentType;
    }

    @Override
    public TabsView getView() {
        return view;
    }

    // TODO
    // refac ProjectManager initialization, put filename into FileEditorPresenter's model
    @Override
    public void onEvent(FileSelectedEvent event) {
        if (event.getRequestType() == FileSelectedEvent.RequestType.FILE) {
            FileElement element = (FileElement) event.getProjectElement();
            FileSystemProjectManager dataManager = new FileSystemProjectManager(event.getBase());
            ContentModel model = new ContentModel(dataManager.getFile(element), dataManager.getContentAsString(element),
                    resolveContentType(element.getPath()));

            FileEditorPresenter presenter = new FileEditorPresenter(eventBus);
            presenter.setModel(model);
            view.addTab(element.getPath(), presenter.getView());
        }
    }
}

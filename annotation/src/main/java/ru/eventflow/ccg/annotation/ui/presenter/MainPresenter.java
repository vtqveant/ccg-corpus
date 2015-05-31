package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.EditorCaretEvent;
import ru.eventflow.ccg.annotation.ui.event.EditorCaretEventHandler;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;
import ru.eventflow.ccg.annotation.ui.view.MainView;

public class MainPresenter implements Presenter<MainView> {

    private final MainView view;
    private final EventBus eventBus;

    @Inject
    public MainPresenter(final EventBus eventBus,
                         final ContainerPresenter containerPresenter,
                         final NavigationPresenter navigationPresenter,
                         final DictionaryPresenter dictionaryPresenter) {
        this.view = new MainView();
        this.eventBus = eventBus;
        this.view.getTopPanel().add(containerPresenter.getView());
        this.view.getNavigationPanel().add(navigationPresenter.getView());
        this.view.getDictionaryPanel().add(dictionaryPresenter.getView());
        init();
    }

    private void init() {
        this.eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.getStatusLabel().setText(e.getMessage());
            }
        });

        this.eventBus.addHandler(EditorCaretEvent.TYPE, new EditorCaretEventHandler() {
            @Override
            public void onEvent(EditorCaretEvent e) {
                view.getInfoLabel().setText(e.getRow() + ":" + e.getColumn());
            }
        });
    }

    @Override
    public MainView getView() {
        return view;
    }
}

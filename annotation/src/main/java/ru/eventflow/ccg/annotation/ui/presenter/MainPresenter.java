package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
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
                         final DictionaryPresenter dictionaryPresenter,
                         final MessagesPresenter messagesPresenter) {
        this.eventBus = eventBus;
        this.view = new MainView();
        this.view.setTopPanel(containerPresenter.getView());
        this.view.addSlidingPanel(messagesPresenter.getView());
        this.view.addSlidingPanel(dictionaryPresenter.getView());
        this.view.addSlidingPanel(navigationPresenter.getView());
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
                int row = e.getRow();
                int column = e.getColumn();
                String text = (row == -1 && column == -1) ? "n/a" : (row + ":" + column);
                view.getCaretPositionLabel().setText(text);
            }
        });
    }

    @Override
    public MainView getView() {
        return view;
    }
}

package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.*;
import ru.eventflow.ccg.annotation.ui.view.MainView;

public class MainPresenter implements Presenter<MainView>, StatusUpdateEventHandler,
        EditorCaretEventHandler, SettingsEventHandler {

    private final MainView view;
    private final EventBus eventBus;

    @Inject
    public MainPresenter(final EventBus eventBus,
                         final ContainerPresenter containerPresenter,
                         final NavigationSliderPresenter navigationPresenter,
                         final LexiconSliderPresenter lexiconPresenter,
                         final MessagesSliderPresenter messagesPresenter) {
        this.eventBus = eventBus;
        this.view = new MainView();
        this.view.setTopPanel(containerPresenter.getView());
        this.view.addSliderPanel(messagesPresenter.getView());
        this.view.addSliderPanel(lexiconPresenter.getView());
        this.view.addSliderPanel(navigationPresenter.getView());
        init();
    }

    private void init() {
        this.eventBus.addHandler(StatusUpdateEvent.TYPE, this);
        this.eventBus.addHandler(EditorCaretEvent.TYPE, this);
        this.eventBus.addHandler(SettingsEvent.TYPE, this);
    }

    @Override
    public MainView getView() {
        return view;
    }

    @Override
    public void onEvent(StatusUpdateEvent e) {
        view.getStatusLabel().setText(e.getMessage());
    }

    @Override
    public void onEvent(EditorCaretEvent e) {
        int row = e.getRow();
        int column = e.getColumn();
        String text = (row == -1 && column == -1) ? "n/a" : (row + ":" + column);
        view.getCaretPositionLabel().setText(text);
    }

    @Override
    public void onEvent(SettingsEvent e) {
        if (e.getSetting() == Setting.STATUSBAR) {
            view.setStatusBarVisible(e.isEnabled());
        }
    }
}

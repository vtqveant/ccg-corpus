package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.DialogEventHandler;
import ru.eventflow.ccg.annotation.ui.view.AboutDialogView;

public class AboutDialogPresenter implements Presenter<AboutDialogView>, DialogEventHandler {

    private final AboutDialogView view;
    private final EventBus eventBus;

    @Inject
    public AboutDialogPresenter(final EventBus eventBus) {
        this.view = new AboutDialogView();
        this.eventBus = eventBus;
        this.eventBus.addHandler(DialogEvent.TYPE, this);
    }

    @Override
    public AboutDialogView getView() {
        return view;
    }

    @Override
    public void onEvent(DialogEvent e) {
        view.setVisible(true);
    }
}

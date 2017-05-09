package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.SaveFileEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.DialogType;
import ru.eventflow.ccg.annotation.ui.view.ToolbarView;

public class ToolbarPresenter implements Presenter<ToolbarView> {

    private ToolbarView view;
    private EventBus eventBus;

    @Inject
    public ToolbarPresenter(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.view = new ToolbarView();

        view.getExecuteButton().addActionListener((e) -> eventBus.fireEvent(new StatusUpdateEvent("execute")));
        view.getOpenButton().addActionListener((e) -> eventBus.fireEvent(new DialogEvent(DialogType.OPEN_PROJECT, view)));
        view.getSaveButton().addActionListener((e) -> eventBus.fireEvent(new SaveFileEvent()));
        view.getSettingsButton().addActionListener((e) -> eventBus.fireEvent(new DialogEvent(DialogType.SETTINGS, view)));
    }

    @Override
    public ToolbarView getView() {
        return view;
    }
}

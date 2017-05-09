package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.SaveFileEvent;
import ru.eventflow.ccg.annotation.ui.event.SettingsEvent;
import ru.eventflow.ccg.annotation.ui.view.DialogType;
import ru.eventflow.ccg.annotation.ui.view.MenuView;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class MenuPresenter implements Presenter<MenuView> {

    private MenuView view;
    private EventBus eventBus;

    @Inject
    public MenuPresenter(final EventBus eventBus) {
        this.view = new MenuView();
        this.eventBus = eventBus;

        view.getExitMenuItem().addActionListener((e) -> {
            // TODO maybe we need to do some work before closing (save, free resources, etc.)
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });

        view.getAboutMenuItem().addActionListener(e -> eventBus.fireEvent(new DialogEvent(DialogType.ABOUT, view)));
        view.getOpenMenuItem().addActionListener(e -> eventBus.fireEvent(new DialogEvent(DialogType.OPEN_PROJECT, view)));
        view.getSaveMenuItem().addActionListener(e -> eventBus.fireEvent(new SaveFileEvent()));
        view.getSettingsMenuItem().addActionListener(e -> eventBus.fireEvent(new DialogEvent(DialogType.SETTINGS, view)));
    }

    @Override
    public MenuView getView() {
        return view;
    }
}

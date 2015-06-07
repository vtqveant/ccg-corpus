package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.SettingsEvent;
import ru.eventflow.ccg.annotation.ui.view.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MenuPresenter implements Presenter<MenuView> {

    private MenuView view;
    private EventBus eventBus;

    @Inject
    public MenuPresenter(final EventBus eventBus) {
        this.view = new MenuView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.view.getExitMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO maybe we need to do some work before closing (save, free resources, etc.)
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        this.view.getAboutMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new DialogEvent());
            }
        });

        final SettingsChangeListener settingsChangeListener = new SettingsChangeListener();
        this.view.getGlossesMenuItem().addActionListener(settingsChangeListener);
        this.view.getStatusBarMenuItem().addActionListener(settingsChangeListener);
    }

    @Override
    public MenuView getView() {
        return view;
    }

    /**
     * This has a drawback of manually checking the menu item,
     * but has an advantage of predictable behaviour with respect to events ordering
     */
    private class SettingsChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MenuView.SettingMenuItem source = (MenuView.SettingMenuItem) e.getSource();
            boolean newValue = !source.isChecked();
            source.setChecked(newValue);
            eventBus.fireEvent(new SettingsEvent(source.getSetting(), newValue));
        }
    }
}

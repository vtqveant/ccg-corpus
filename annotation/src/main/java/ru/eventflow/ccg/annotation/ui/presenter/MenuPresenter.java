package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.Setting;
import ru.eventflow.ccg.annotation.ui.event.SettingsEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.MenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        this.view.getFirstMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new StatusUpdateEvent("first menu item"));
            }
        });

        this.view.getSecondMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new StatusUpdateEvent("second menu item"));
            }
        });

        this.view.getGlossesMenuItem().addActionListener(new SettingActionListener(Setting.GLOSSES));
        this.view.getStatusBarMenuItem().addActionListener(new SettingActionListener(Setting.STATUSBAR));
    }

    @Override
    public MenuView getView() {
        return view;
    }

    private class SettingActionListener implements ActionListener {
        private Setting setting;

        public SettingActionListener(Setting setting) {
            this.setting = setting;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            MenuView.SettingMenuItem source = (MenuView.SettingMenuItem) e.getSource();
            boolean previous = source.isChecked();
            source.setChecked(!previous);
            eventBus.fireEvent(new SettingsEvent(setting, !previous));
        }
    }
}

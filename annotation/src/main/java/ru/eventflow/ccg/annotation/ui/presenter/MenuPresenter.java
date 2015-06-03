package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
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

        this.view.getGlossesMenuItem().addActionListener(new SettingActionListener());
        this.view.getStatusBarMenuItem().addActionListener(new SettingActionListener());
    }

    @Override
    public MenuView getView() {
        return view;
    }

    /**
     * This has a drawback of manually checking the menu item,
     * but has an advantage of predictable behaviour with respect to events ordering
     */
    private class SettingActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MenuView.SettingMenuItem source = (MenuView.SettingMenuItem) e.getSource();
            boolean newValue = !source.isChecked();
            source.setChecked(newValue);
            eventBus.fireEvent(new SettingsEvent(source.getSetting(), newValue));
        }
    }
}

package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
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
    }

    @Override
    public MenuView getView() {
        return view;
    }
}

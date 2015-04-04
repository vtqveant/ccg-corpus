package ru.eventflow.annotation.presenter;

import com.google.inject.Inject;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.LogEvent;
import ru.eventflow.annotation.view.MenuView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPresenter implements Presenter<MenuView> {

    private MenuView view;
    private EventBus eventBus;

    @Inject
    public MenuPresenter(MenuView view, final EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;

        this.view.getFirstMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new LogEvent("first menu item"));
            }
        });

        this.view.getSecondMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new LogEvent("second menu item"));
            }
        });
    }

    @Override
    public MenuView getView() {
        return view;
    }
}

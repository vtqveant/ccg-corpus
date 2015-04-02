package ru.eventflow.manager;

import com.pennychecker.eventbus.HandlerManager;
import ru.eventflow.manager.presenter.RootPresenter;
import ru.eventflow.manager.view.RootView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final HandlerManager eventBus = new HandlerManager(null);

                final LoggingController loggingController = new LoggingController(eventBus);

                RootView rootView = new RootView();
                rootView.setPresenter(new RootPresenter(rootView, eventBus));
            }
        });
    }
}


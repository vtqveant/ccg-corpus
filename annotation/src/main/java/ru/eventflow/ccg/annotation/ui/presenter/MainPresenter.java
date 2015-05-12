package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPresenter implements Presenter<MainView> {

    private final MainView view;
    private final EventBus eventBus;

    @Inject
    public MainPresenter(final EventBus eventBus,
                         final ContainerPresenter containerPresenter,
                         final NavigationPresenter navigationPresenter,
                         final DictionaryPresenter dictionaryPresenter) {
        this.view = new MainView();
        this.eventBus = eventBus;
        this.view.getTopPanel().add(containerPresenter.getView());
        this.view.getNavigationPanel().add(navigationPresenter.getView());
        this.view.getDictionaryPanel().add(dictionaryPresenter.getView());
        init();
    }

    private void init() {
        this.eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                view.getStatusLabel().setText(e.getMessage());
            }
        });

        view.getAnnotationBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new TabEvent());
            }
        });
    }

    @Override
    public MainView getView() {
        return view;
    }
}

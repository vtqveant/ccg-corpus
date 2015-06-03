package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.NavigationView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class NavigationPresenter implements Presenter<NavigationView> {

    private final NavigationView view;

    @Inject
    public NavigationPresenter(final EventBus eventBus,
                               final TreePresenter treePresenter,
                               final TextPresenter textPresenter) {
        view = new NavigationView();
        view.setLeftComponent(treePresenter.getView());
        view.setRightComponent(textPresenter.getView());

        view.getAmbiguousBtn().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String s = e.getStateChange() == ItemEvent.SELECTED ? "selected" : "deselected";
                eventBus.fireEvent(new StatusUpdateEvent("ambiguous toggle " + s));
            }
        });

        view.getAnnotatedBtn().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String s = e.getStateChange() == ItemEvent.SELECTED ? "selected" : "deselected";
                eventBus.fireEvent(new StatusUpdateEvent("annotated toggle " + s));
            }
        });
    }

    @Override
    public NavigationView getView() {
        return view;
    }
}

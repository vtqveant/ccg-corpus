package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.NavigationSliderView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class NavigationSliderPresenter implements Presenter<NavigationSliderView> {

    private final NavigationSliderView view;

    @Inject
    public NavigationSliderPresenter(final EventBus eventBus,
                                     final CorpusTreePresenter corpusTreePresenter,
                                     final TextPresenter textPresenter) {
        view = new NavigationSliderView();
        view.setLeftComponent(corpusTreePresenter.getView());
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
    public NavigationSliderView getView() {
        return view;
    }
}

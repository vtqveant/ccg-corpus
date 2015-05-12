package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEventHandler;
import ru.eventflow.ccg.annotation.ui.view.ContainerView;

public class ContainerPresenter implements Presenter<ContainerView> {

    private final ContainerView view;

    @Inject
    public ContainerPresenter(final EventBus eventBus) {
        this.view = new ContainerView();

        eventBus.addHandler(TabEvent.TYPE, new TabEventHandler() {
            @Override
            public void onEvent(TabEvent e) {
                AnnotationPresenter presenter = new AnnotationPresenter(eventBus);
                view.addTab("FTS Annotation", presenter.getView());
            }
        });
    }

    @Override
    public ContainerView getView() {
        return view;
    }
}

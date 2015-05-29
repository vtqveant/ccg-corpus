package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.view.AnnotationView;

import javax.inject.Inject;

public class AnnotationPresenter implements Presenter<AnnotationView> {

    private AnnotationView view;
    private EventBus eventBus;

    @Inject
    public AnnotationPresenter(final EventBus eventBus) {
        this.view = new AnnotationView();
        this.eventBus = eventBus;
    }

    @Override
    public AnnotationView getView() {
        return view;
    }
}

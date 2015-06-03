package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.view.ConcordanceView;

public class ConcordancePresenter implements Presenter<ConcordanceView> {

    private ConcordanceView view;
    private EventBus eventBus;

    @Inject
    public ConcordancePresenter(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.view = new ConcordanceView();
    }

    @Override
    public ConcordanceView getView() {
        return view;
    }
}

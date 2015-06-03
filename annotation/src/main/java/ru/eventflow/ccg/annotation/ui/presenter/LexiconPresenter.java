package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.view.LexiconView;

public class LexiconPresenter implements Presenter<LexiconView> {

    private final LexiconView view;

    @Inject
    public LexiconPresenter(final EventBus eventBus) {
        this.view = new LexiconView();
    }

    @Override
    public LexiconView getView() {
        return view;
    }
}

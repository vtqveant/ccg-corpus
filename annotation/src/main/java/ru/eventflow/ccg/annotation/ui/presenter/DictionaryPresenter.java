package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.view.DictionaryView;

public class DictionaryPresenter implements Presenter<DictionaryView> {

    private final DictionaryView view;

    @Inject
    public DictionaryPresenter(final EventBus eventBus) {
        this.view = new DictionaryView();
    }

    @Override
    public DictionaryView getView() {
        return view;
    }
}

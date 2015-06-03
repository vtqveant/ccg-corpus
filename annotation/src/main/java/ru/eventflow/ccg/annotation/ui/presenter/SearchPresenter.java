package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.view.SearchView;

public class SearchPresenter implements Presenter<SearchView> {

    private SearchView view;
    private EventBus eventBus;

    @Inject
    public SearchPresenter(final EventBus eventBus) {
        this.eventBus = eventBus;
        this.view = new SearchView();
    }

    @Override
    public SearchView getView() {
        return view;
    }
}

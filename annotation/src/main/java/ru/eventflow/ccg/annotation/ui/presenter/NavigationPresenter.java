package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.view.NavigationView;

public class NavigationPresenter implements Presenter<NavigationView> {

    private final NavigationView view;

    @Inject
    public NavigationPresenter(final EventBus eventBus,
                               final TreePresenter treePresenter,
                               final TextPresenter textPresenter) {
        view = new NavigationView();
        view.setLeftComponent(treePresenter.getView());
        view.setRightComponent(textPresenter.getView());
    }

    @Override
    public NavigationView getView() {
        return view;
    }
}

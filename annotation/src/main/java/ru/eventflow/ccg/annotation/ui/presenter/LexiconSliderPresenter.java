package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.view.LexiconSliderView;

public class LexiconSliderPresenter implements Presenter<LexiconSliderView> {

    private final LexiconSliderView view;

    @Inject
    public LexiconSliderPresenter(final EventBus eventBus,
                                  final CategoryTreePresenter categoryTreePresenter,
                                  final ConcordancePresenter concordancePresenter) {
        this.view = new LexiconSliderView();
        this.view.setLeftComponent(categoryTreePresenter.getView());
        this.view.setRightComponent(concordancePresenter.getView());
    }

    @Override
    public LexiconSliderView getView() {
        return view;
    }
}

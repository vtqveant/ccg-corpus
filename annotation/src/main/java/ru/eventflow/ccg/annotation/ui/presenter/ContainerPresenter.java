package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEventHandler;
import ru.eventflow.ccg.annotation.ui.view.ContainerView;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

public class ContainerPresenter implements Presenter<ContainerView> {

    private final ContainerView view;

    @Inject
    public ContainerPresenter(final EventBus eventBus) {
        this.view = new ContainerView();

        eventBus.addHandler(TabEvent.TYPE, new TabEventHandler() {
            @Override
            public void onEvent(TabEvent e) {
                Sentence sentence = e.getSentence();
                String title = "Sentence #" + sentence.getId();
                AnnotationPresenter presenter = new AnnotationPresenter(eventBus, sentence);
                view.addTab(title, presenter.getView());
            }
        });
    }

    @Override
    public ContainerView getView() {
        return view;
    }
}

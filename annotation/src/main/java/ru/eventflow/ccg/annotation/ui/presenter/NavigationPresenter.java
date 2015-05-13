package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.NavigationView;
import ru.eventflow.ccg.datasource.model.corpus.Document;

public class NavigationPresenter implements Presenter<NavigationView> {

    private final NavigationView view;

    @Inject
    public NavigationPresenter(final EventBus eventBus, final TreePresenter treePresenter, final TextPresenter textPresenter) {
        view = new NavigationView();
        view.setLeftComponent(treePresenter.getView());
        view.setRightComponent(textPresenter.getView());

        eventBus.addHandler(DocumentSelectedEvent.TYPE, new DocumentSelectedEventHandler() {
            @Override
            public void onEvent(DocumentSelectedEvent e) {
                Document document = e.getDocument();
                if (document != null) {
                    String text = e.getDocument().getText().trim();
                    int wordCount = text.isEmpty() ? 0 : text.split("\\s+").length;
                    view.setInfo("words: " + wordCount + ", sentences: ?, annotated: 0");
                } else {
                    view.setInfo(" ");
                }
            }
        });
    }

    @Override
    public NavigationView getView() {
        return view;
    }
}

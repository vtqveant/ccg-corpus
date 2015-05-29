package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.NavigationView;
import ru.eventflow.ccg.datasource.model.corpus.Text;

public class NavigationPresenter implements Presenter<NavigationView> {

    private final NavigationView view;

    @Inject
    public NavigationPresenter(final EventBus eventBus, final TreePresenter treePresenter, final TextPresenter textPresenter) {
        view = new NavigationView();
        view.setLeftComponent(treePresenter.getView());
        view.setRightComponent(textPresenter.getView());

        eventBus.addHandler(TextSelectedEvent.TYPE, new TextSelectedEventHandler() {
            @Override
            public void onEvent(TextSelectedEvent e) {
                Text text = e.getText();
                if (text != null) {
                    // TODO fix
                    // String t = e.getText().trim();
                    //int wordCount = t.isEmpty() ? 0 : t.split("\\s+").length;
                    // view.setInfo("words: " + wordCount + ", sentences: ?, annotated: 0");
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

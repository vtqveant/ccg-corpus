package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.DetailsView;


public class DetailsPresenter implements Presenter<DetailsView> {

    private DetailsView view;
    private EventBus eventBus;

    @Inject
    public DetailsPresenter(final EventBus eventBus) {
        this.view = new DetailsView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.eventBus.addHandler(DocumentSelectedEvent.TYPE, new DocumentSelectedEventHandler() {
            @Override
            public void onEvent(DocumentSelectedEvent e) {
                view.setText(e.getDocument().getText());
            }
        });
    }

    @Override
    public DetailsView getView() {
        return view;
    }
}

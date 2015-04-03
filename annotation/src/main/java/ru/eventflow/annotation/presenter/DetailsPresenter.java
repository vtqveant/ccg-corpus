package ru.eventflow.annotation.presenter;

import com.google.inject.Inject;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.DocumentSelectedEvent;
import ru.eventflow.annotation.event.DocumentSelectedEventHandler;
import ru.eventflow.annotation.view.DetailsView;


public class DetailsPresenter implements Presenter<DetailsView> {

    private DetailsView view;
    private EventBus eventBus;

    @Inject
    public DetailsPresenter(final DetailsView view, final EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;

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

package ru.eventflow.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.annotation.EventBus;
import ru.eventflow.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.annotation.ui.event.DocumentSelectedEventHandler;
import ru.eventflow.annotation.ui.view.DetailsView;


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
                view.setText(e.getDoc().getText());
            }
        });
    }

    @Override
    public DetailsView getView() {
        return view;
    }
}

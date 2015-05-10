package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.DocumentSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.DetailsView;
import ru.eventflow.ccg.datasource.model.corpus.Document;


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
                Document document = e.getDocument();
                if (document != null) {
                    view.setText(e.getDocument().getText());
                } else {
                    view.setText("");
                }
            }
        });
    }

    @Override
    public DetailsView getView() {
        return view;
    }
}

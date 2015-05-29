package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEvent;
import ru.eventflow.ccg.annotation.ui.event.TextSelectedEventHandler;
import ru.eventflow.ccg.annotation.ui.view.TextView;
import ru.eventflow.ccg.datasource.model.corpus.Text;

public class TextPresenter implements Presenter<TextView> {

    private TextView view;
    private EventBus eventBus;

    @Inject
    public TextPresenter(final EventBus eventBus) {
        this.view = new TextView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.eventBus.addHandler(TextSelectedEvent.TYPE, new TextSelectedEventHandler() {
            @Override
            public void onEvent(TextSelectedEvent e) {
                Text text = e.getText();
                if (text != null) {
                    String t = e.getText().getName(); // TODO fix
                    if (e.getText().getParent() != null) {
                        t += " parent_id=" + e.getText().getParent().getId();
                    }
                    view.setText(t);
                } else {
                    view.setText("");
                }
            }
        });
    }

    @Override
    public TextView getView() {
        return view;
    }

}

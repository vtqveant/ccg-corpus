package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.datasource.model.corpus.Text;

public class TextSelectedEvent extends Event<TextSelectedEventHandler> {

    public final static Type<TextSelectedEventHandler> TYPE = new Type<TextSelectedEventHandler>();

    private Text text;

    public TextSelectedEvent(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    @Override
    public Type<TextSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TextSelectedEventHandler handler) {
        handler.onEvent(this);
    }
}

package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.annotation.ui.presenter.Presenter;

import java.awt.*;

public class SearchEvent extends Event<SearchEventHandler> {

    public final static Type<SearchEventHandler> TYPE = new Type<SearchEventHandler>();

    private final Presenter<? extends Container> target;
    private final String input;

    public <T extends Container> SearchEvent(Presenter<T> target, String input) {
        this.target = target;
        this.input = input;
    }

    public Presenter getTarget() {
        return target;
    }

    public String getInput() {
        return input;
    }

    @Override
    public Type<SearchEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SearchEventHandler handler) {
        handler.onEvent(this);
    }
}

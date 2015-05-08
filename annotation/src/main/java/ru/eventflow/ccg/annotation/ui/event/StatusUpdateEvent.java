package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.Event;

public class StatusUpdateEvent extends Event<StatusUpdateEventHandler> {

    public final static Type<StatusUpdateEventHandler> TYPE = new Type<StatusUpdateEventHandler>();

    private final String message;

    public StatusUpdateEvent(String message) {
        this.message = message;
    }

    @Override
    public Type<StatusUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StatusUpdateEventHandler handler) {
        handler.onEvent(this);
    }

    public String getMessage() {
        return message;
    }
}

package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.Event;

public class LogEvent extends Event<LogEventHandler> {

    public final static Type<LogEventHandler> TYPE = new Type<LogEventHandler>();

    private final String message;

    public LogEvent(String message) {
        this.message = message;
    }

    @Override
    public Type<LogEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LogEventHandler handler) {
        handler.onEvent(this);
    }

    public String getMessage() {
        return message;
    }
}

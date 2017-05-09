package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;

/**
 * this event does not contain any information regarding the file to be saved,
 * the decision is made by the file editor component based on its visibility/focus/etc.
 * (currently all open tabs are saved)
 */
public class SaveFileEvent extends Event<SaveFileEventHandler> {

    public final static Type<SaveFileEventHandler> TYPE = new Type<>();

    public SaveFileEvent() {
    }

    @Override
    public Type<SaveFileEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SaveFileEventHandler handler) {
        handler.onEvent(this);
    }
}

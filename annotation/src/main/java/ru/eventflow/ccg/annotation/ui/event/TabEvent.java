package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.Event;

public class TabEvent extends Event<TabEventHandler> {

    public final static Type<TabEventHandler> TYPE = new Type<TabEventHandler>();

    @Override
    public Type<TabEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TabEventHandler handler) {
        handler.onEvent(this);
    }
}

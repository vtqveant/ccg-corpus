package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.Event;

public class TabEvent extends Event<TabEventHandler> {

    public final static Type<TabEventHandler> TYPE = new Type<TabEventHandler>();

    private int sentenceId;

    public TabEvent(int sentenceId) {
        this.sentenceId = sentenceId;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    @Override
    public Type<TabEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TabEventHandler handler) {
        handler.onEvent(this);
    }
}

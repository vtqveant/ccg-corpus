package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

public class TabEvent extends Event<TabEventHandler> {

    public final static Type<TabEventHandler> TYPE = new Type<TabEventHandler>();

    private Sentence sentence;

    public TabEvent(Sentence sentence) {
        this.sentence = sentence;
    }

    public Sentence getSentence() {
        return sentence;
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

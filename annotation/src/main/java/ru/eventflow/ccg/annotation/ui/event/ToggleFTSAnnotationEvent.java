package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.Event;

public class ToggleFTSAnnotationEvent extends Event<ToggleFTSAnnotationEventHandler> {

    public final static Type<ToggleFTSAnnotationEventHandler> TYPE = new Type<ToggleFTSAnnotationEventHandler>();

    @Override
    public Type<ToggleFTSAnnotationEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ToggleFTSAnnotationEventHandler handler) {
        handler.onEvent(this);
    }
}

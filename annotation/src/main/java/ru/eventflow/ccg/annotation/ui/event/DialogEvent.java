package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.annotation.ui.view.DialogType;

import java.awt.*;

public class DialogEvent extends Event<DialogEventHandler> {

    public final static ru.eventflow.ccg.annotation.eventbus.Event.Type<DialogEventHandler> TYPE = new ru.eventflow.ccg.annotation.eventbus.Event.Type<>();
    private DialogType dialogType;
    private Component source;

    public DialogEvent(DialogType dialogType, Component source) {
        this.dialogType = dialogType;
        this.source = source;
    }

    public DialogType getDialogType() {
        return dialogType;
    }

    @Override
    public Component getSource() {
        return source;
    }

    @Override
    public Type<DialogEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DialogEventHandler handler) {
        handler.onEvent(this);
    }
}

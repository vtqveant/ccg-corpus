package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

public class FormSelectedEvent extends Event<FormSelectedEventHandler> {

    public final static Type<FormSelectedEventHandler> TYPE = new Type<FormSelectedEventHandler>();

    private Form form;

    public FormSelectedEvent(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    @Override
    public Type<FormSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(FormSelectedEventHandler handler) {
        handler.onEvent(this);
    }
}

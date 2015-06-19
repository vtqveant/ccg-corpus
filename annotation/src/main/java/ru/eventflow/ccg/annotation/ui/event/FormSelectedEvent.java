package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

public class FormSelectedEvent extends Event<FormSelectedEventHandler> {

    public final static Type<FormSelectedEventHandler> TYPE = new Type<FormSelectedEventHandler>();

    private Form form;
    private Category category;

    public FormSelectedEvent(Form form, Category category) {
        this.form = form;
        this.category = category;
    }

    public Form getForm() {
        return form;
    }

    public Category getCategory() {
        return category;
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

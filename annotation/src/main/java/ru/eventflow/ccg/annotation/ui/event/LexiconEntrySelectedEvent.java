package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.syntax.Category;

public class LexiconEntrySelectedEvent extends Event<LexiconEntrySelectedEventHandler> {

    public final static Type<LexiconEntrySelectedEventHandler> TYPE = new Type<LexiconEntrySelectedEventHandler>();

    private Form form;
    private Category category;

    public LexiconEntrySelectedEvent(Form form, Category category) {
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
    public Type<LexiconEntrySelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(LexiconEntrySelectedEventHandler handler) {
        handler.onEvent(this);
    }
}

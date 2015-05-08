package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.Event;
import ru.eventflow.ccgbank.model.Document;

public class DocumentSelectedEvent extends Event<DocumentSelectedEventHandler> {

    public final static Type<DocumentSelectedEventHandler> TYPE = new Type<DocumentSelectedEventHandler>();

    private Document document;

    public DocumentSelectedEvent(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public Type<DocumentSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DocumentSelectedEventHandler handler) {
        handler.onEvent(this);
    }
}

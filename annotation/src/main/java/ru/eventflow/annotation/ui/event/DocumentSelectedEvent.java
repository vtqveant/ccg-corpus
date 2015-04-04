package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.Event;
import ru.eventflow.annotation.model.Document;

public class DocumentSelectedEvent extends Event<DocumentSelectedEventHandler> {

    public final static Type<DocumentSelectedEventHandler> TYPE = new Type<DocumentSelectedEventHandler>();

    private Document doc;

    public DocumentSelectedEvent(Document doc) {
        this.doc = doc;
    }

    public Document getDoc() {
        return doc;
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

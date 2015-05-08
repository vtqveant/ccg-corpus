package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.Event;
import ru.eventflow.ccgbank.model.Document;

public class DocumentMarkedEvent extends Event<DocumentMarkedEventHandler> {

    public final static Type<DocumentMarkedEventHandler> TYPE = new Type<DocumentMarkedEventHandler>();

    private Document doc;
    private boolean relevant;

    public DocumentMarkedEvent(Document doc, boolean relevant) {
        this.doc = doc;
        this.relevant = relevant;
    }

    public Document getDocument() {
        return doc;
    }

    public boolean isRelevant() {
        return relevant;
    }

    @Override
    public Type<DocumentMarkedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DocumentMarkedEventHandler handler) {
        handler.onEvent(this);
    }
}


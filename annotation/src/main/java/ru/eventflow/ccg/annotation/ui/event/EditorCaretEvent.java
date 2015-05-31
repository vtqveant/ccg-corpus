package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.Event;

public class EditorCaretEvent extends Event<EditorCaretEventHandler> {

    public final static Type<EditorCaretEventHandler> TYPE = new Type<EditorCaretEventHandler>();

    private int row;
    private int column;

    public EditorCaretEvent(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Type<EditorCaretEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditorCaretEventHandler handler) {
        handler.onEvent(this);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}

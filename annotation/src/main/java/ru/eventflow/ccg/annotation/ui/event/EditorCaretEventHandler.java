package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface EditorCaretEventHandler extends EventHandler {
    void onEvent(EditorCaretEvent e);
}

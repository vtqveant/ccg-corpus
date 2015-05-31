package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface EditorCaretEventHandler extends EventHandler {
    void onEvent(EditorCaretEvent e);
}

package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface SaveFileEventHandler extends EventHandler {
    void onEvent(final SaveFileEvent event);
}
package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface StatusUpdateEventHandler extends EventHandler {
    void onEvent(StatusUpdateEvent e);
}

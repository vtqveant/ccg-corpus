package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface StatusUpdateEventHandler extends EventHandler {
    void onEvent(StatusUpdateEvent e);
}

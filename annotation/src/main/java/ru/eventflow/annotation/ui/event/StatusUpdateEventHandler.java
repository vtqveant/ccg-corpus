package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface StatusUpdateEventHandler extends EventHandler {
    public void onEvent(StatusUpdateEvent e);
}

package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface DocumentMarkedEventHandler extends EventHandler {
    void onEvent(DocumentMarkedEvent e);
}

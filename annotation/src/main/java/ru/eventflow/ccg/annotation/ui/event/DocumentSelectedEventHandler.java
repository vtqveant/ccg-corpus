package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface DocumentSelectedEventHandler extends EventHandler {
    void onEvent(DocumentSelectedEvent e);
}
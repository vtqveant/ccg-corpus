package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface DocumentSelectedEventHandler extends EventHandler {
    public void onEvent(DocumentSelectedEvent e);
}
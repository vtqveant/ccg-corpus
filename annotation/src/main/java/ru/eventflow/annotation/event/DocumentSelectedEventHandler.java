package ru.eventflow.annotation.event;

import com.pennychecker.eventbus.EventHandler;

public interface DocumentSelectedEventHandler extends EventHandler {
    public void onEvent(DocumentSelectedEvent e);
}
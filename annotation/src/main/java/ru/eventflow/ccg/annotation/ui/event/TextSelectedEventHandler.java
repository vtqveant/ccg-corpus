package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface TextSelectedEventHandler extends EventHandler {
    void onEvent(TextSelectedEvent e);
}
package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface FileSelectedEventHandler extends EventHandler {
    void onEvent(FileSelectedEvent e);
}

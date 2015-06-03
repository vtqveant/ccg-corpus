package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface TabEventHandler extends EventHandler {
    void onEvent(TabEvent e);
}

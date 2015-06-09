package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface SearchEventHandler extends EventHandler {
    void onEvent(SearchEvent e);
}

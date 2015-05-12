package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface TabEventHandler extends EventHandler {
    void onEvent(TabEvent e);
}

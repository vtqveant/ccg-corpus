package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface TextSelectedEventHandler extends EventHandler {
    void onEvent(TextSelectedEvent e);
}
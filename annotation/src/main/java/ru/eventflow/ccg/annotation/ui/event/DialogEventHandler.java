package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface DialogEventHandler extends EventHandler {
    void onEvent(DialogEvent e);
}
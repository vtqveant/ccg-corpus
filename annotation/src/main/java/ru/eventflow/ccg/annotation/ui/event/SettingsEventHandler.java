package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface SettingsEventHandler extends EventHandler {
    void onEvent(SettingsEvent e);
}

package ru.eventflow.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface LogEventHandler extends EventHandler {
    public void onEvent(LogEvent e);
}

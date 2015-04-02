package ru.eventflow.annotation.event;

import com.pennychecker.eventbus.EventHandler;

public interface LogEventHandler extends EventHandler {
    public void onEvent(LogEvent e);
}

package ru.eventflow.manager.event;

import com.pennychecker.eventbus.EventHandler;

public interface LogEventHandler extends EventHandler {
    public void onLogEvent(LogEvent e);
}

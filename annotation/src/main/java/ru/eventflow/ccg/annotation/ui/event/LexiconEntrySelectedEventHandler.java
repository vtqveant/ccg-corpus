package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.EventHandler;

public interface LexiconEntrySelectedEventHandler extends EventHandler {
    void onEvent(LexiconEntrySelectedEvent e);
}

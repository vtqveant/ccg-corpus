package ru.eventflow.ccg.annotation.ui.event;

import com.pennychecker.eventbus.EventHandler;

public interface ToggleFTSAnnotationEventHandler extends EventHandler {
    void onEvent(ToggleFTSAnnotationEvent e);
}

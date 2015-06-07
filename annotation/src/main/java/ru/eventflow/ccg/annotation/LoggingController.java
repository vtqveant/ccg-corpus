package ru.eventflow.ccg.annotation;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;

import java.util.logging.Logger;

public class LoggingController implements StatusUpdateEventHandler {

    private static final Logger logger = Logger.getLogger(LoggingController.class.getName());

    @Inject
    public LoggingController(final EventBus eventBus) {
        eventBus.addHandler(StatusUpdateEvent.TYPE, this);
    }

    @Override
    public void onEvent(StatusUpdateEvent e) {
        logger.info(e.getMessage());
    }
}

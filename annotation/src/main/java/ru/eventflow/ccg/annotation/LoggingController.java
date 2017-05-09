package ru.eventflow.ccg.annotation;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;

public class LoggingController implements StatusUpdateEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class.getName());

    @Inject
    public LoggingController(final EventBus eventBus) {
        eventBus.addHandler(StatusUpdateEvent.TYPE, this);
    }

    @Override
    public void onEvent(StatusUpdateEvent e) {
        logger.info(e.getMessage());
    }
}

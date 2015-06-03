package ru.eventflow.ccg.annotation;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEventHandler;

import java.util.logging.Logger;

public class LoggingController {

    private static final Logger logger = Logger.getLogger(LoggingController.class.getName());

    private EventBus eventBus;

    @Inject
    public LoggingController(EventBus eventBus) {
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        eventBus.addHandler(StatusUpdateEvent.TYPE, new StatusUpdateEventHandler() {
            @Override
            public void onEvent(StatusUpdateEvent e) {
                logger.info(e.getMessage());
            }
        });
    }
}

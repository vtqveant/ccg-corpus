package ru.eventflow.annotation;

import com.google.inject.Inject;
import com.pennychecker.eventbus.EventBus;
import ru.eventflow.annotation.event.LogEvent;
import ru.eventflow.annotation.event.LogEventHandler;

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
        eventBus.addHandler(LogEvent.TYPE, new LogEventHandler() {
            @Override
            public void onEvent(LogEvent e) {
                logger.info(e.getMessage());
            }
        });
    }
}

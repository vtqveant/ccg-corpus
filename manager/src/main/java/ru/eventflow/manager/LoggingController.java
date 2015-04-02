package ru.eventflow.manager;

import com.pennychecker.eventbus.HandlerManager;
import ru.eventflow.manager.event.LogEvent;
import ru.eventflow.manager.event.LogEventHandler;

import java.util.logging.Logger;

public class LoggingController {

    private static final Logger logger = Logger.getLogger(LoggingController.class.getName());

    private final HandlerManager eventBus;

    public LoggingController(HandlerManager eventBus) {
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        eventBus.addHandler(LogEvent.TYPE, new LogEventHandler() {
            @Override
            public void onLogEvent(LogEvent e) {
                logger.info(e.getMessage());
            }
        });
    }
}

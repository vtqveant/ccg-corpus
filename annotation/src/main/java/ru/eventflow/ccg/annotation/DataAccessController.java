package ru.eventflow.ccg.annotation;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.datasource.DataManager;

public class DataAccessController {

    private EventBus eventBus;
    private DataManager dataManager;

    @Inject
    public DataAccessController(EventBus eventBus, DataManager dataManager) {
        this.eventBus = eventBus;
        this.dataManager = dataManager;
        init();
    }

    private void init() {
//        eventBus.addHandler(DocumentMarkedEvent.TYPE, new DocumentMarkedEventHandler() {
//            @Override
//            public void onEvent(DocumentMarkedEvent e) {
//                dataManager.setRelevant(e.getDocument());
//            }
//        });
    }
}

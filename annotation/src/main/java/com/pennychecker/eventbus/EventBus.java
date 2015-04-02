package com.pennychecker.eventbus;

/**
 * EventBus is for passing broadcast messages across the whole application
 * IoC container must ensure it is in the Singleton scope
 */
public class EventBus extends HandlerManager {
    public EventBus() {
        super(null);
    }
}

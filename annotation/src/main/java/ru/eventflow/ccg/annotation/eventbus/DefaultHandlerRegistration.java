/*
 * Copyright 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.eventflow.ccg.annotation.eventbus;

public class DefaultHandlerRegistration implements HandlerRegistration {

    private final HandlerManager manager;
    private final EventHandler handler;
    private final Event.Type<?> type;

    /**
     * Creates a new handler registration.
     *
     * @param <H>     Handler type
     * @param manager the handler manager
     * @param type    the event type
     * @param handler the handler
     */
    protected <H extends EventHandler> DefaultHandlerRegistration(HandlerManager manager, Event.Type<H> type, H handler) {
        this.manager = manager;
        this.handler = handler;
        this.type = type;
    }

    EventHandler getHandler() {
        return handler;
    }

    /**
     * Removes the given handler from its manager.
     */
    @SuppressWarnings("unchecked")
    public void removeHandler() {
        manager.removeHandler((Event.Type<EventHandler>) type, handler);
    }

}

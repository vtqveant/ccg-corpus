package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;

public class SettingsEvent extends Event<SettingsEventHandler> {

    public final static Type<SettingsEventHandler> TYPE = new Type<SettingsEventHandler>();

    private Setting setting;
    private boolean enabled;

    public SettingsEvent(Setting setting, boolean enabled) {
        this.setting = setting;
        this.enabled = enabled;
    }

    public Setting getSetting() {
        return setting;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Type<SettingsEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SettingsEventHandler handler) {
        handler.onEvent(this);
    }

}

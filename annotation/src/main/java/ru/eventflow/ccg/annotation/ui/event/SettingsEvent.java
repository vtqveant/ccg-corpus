package ru.eventflow.ccg.annotation.ui.event;

import ru.eventflow.ccg.annotation.eventbus.Event;
import ru.eventflow.ccg.annotation.ui.Settings;

public class SettingsEvent extends Event<SettingsEventHandler> {

    public final static Type<SettingsEventHandler> TYPE = new Type<SettingsEventHandler>();

    private Settings setting;
    private boolean enabled;

    public SettingsEvent(Settings setting, boolean enabled) {
        this.setting = setting;
        this.enabled = enabled;
    }

    public Settings getSetting() {
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

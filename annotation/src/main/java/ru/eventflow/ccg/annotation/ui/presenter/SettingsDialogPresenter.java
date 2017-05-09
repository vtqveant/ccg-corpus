package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.DialogEventHandler;
import ru.eventflow.ccg.annotation.ui.event.StatusUpdateEvent;
import ru.eventflow.ccg.annotation.ui.view.DialogType;
import ru.eventflow.ccg.annotation.ui.view.SettingsDialog;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;

public class SettingsDialogPresenter implements Presenter<JDialog>, DialogEventHandler {

    private EventBus eventBus;
    private String endpoint;
    private SettingsDialog dialog;

    @Inject
    public SettingsDialogPresenter(final EventBus eventBus, @Named("sparql.endpoint") final String endpoint) {
        this.eventBus = eventBus;
        this.endpoint = endpoint;

        eventBus.addHandler(DialogEvent.TYPE, this);
    }

    @Override
    public void onEvent(DialogEvent event) {
        if (event.getDialogType() == DialogType.SETTINGS) {
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(event.getSource());
            dialog = new SettingsDialog(owner);
            dialog.getTextField().setText(endpoint);
            dialog.getOkButton().addActionListener((e) -> {
                save();
                dialog.dispose();
            });
            dialog.setVisible(true);
        }
    }

    private void save() {
        String value = dialog.getTextField().getText();
        eventBus.fireEvent(new StatusUpdateEvent(value));
        // TODO actually save
    }

    @Override
    public JDialog getView() {
        return null;
    }
}

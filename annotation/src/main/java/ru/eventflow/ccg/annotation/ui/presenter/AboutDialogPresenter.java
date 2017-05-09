package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.DialogEvent;
import ru.eventflow.ccg.annotation.ui.event.DialogEventHandler;
import ru.eventflow.ccg.annotation.ui.view.AboutDialog;
import ru.eventflow.ccg.annotation.ui.view.DialogType;

import javax.inject.Inject;
import javax.swing.*;

public class AboutDialogPresenter implements Presenter<JDialog>, DialogEventHandler {

    private JDialog dialog;

    @Inject
    public AboutDialogPresenter(final EventBus eventBus) {
        eventBus.addHandler(DialogEvent.TYPE, this);
    }

    @Override
    public void onEvent(DialogEvent e) {
        if (e.getDialogType() == DialogType.ABOUT) {
            if (dialog == null) {
                JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(e.getSource());
                dialog = new AboutDialog(owner);
            }
            dialog.setVisible(true);
        }
    }

    @Override
    public JDialog getView() {
        return null;
    }
}

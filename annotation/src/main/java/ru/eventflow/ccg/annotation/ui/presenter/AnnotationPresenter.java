package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.EventBus;
import ru.eventflow.ccg.annotation.ui.event.ToggleFTSAnnotationEvent;
import ru.eventflow.ccg.annotation.ui.event.ToggleFTSAnnotationEventHandler;
import ru.eventflow.ccg.annotation.ui.view.AnnotationView;

import javax.swing.*;
import java.awt.*;

public class AnnotationPresenter implements Presenter<AnnotationView> {

    private final AnnotationView view;

    @Inject
    public AnnotationPresenter(final EventBus eventBus) {
        this.view = new AnnotationView();

        eventBus.addHandler(ToggleFTSAnnotationEvent.TYPE, new ToggleFTSAnnotationEventHandler() {
            @Override
            public void onEvent(ToggleFTSAnnotationEvent e) {
                JPanel panel = new JPanel(false);
                JLabel filler = new JLabel("kjlkjlkj");
                filler.setHorizontalAlignment(JLabel.CENTER);
                panel.setLayout(new GridLayout(1, 1));
                panel.add(filler);
                view.addTab("FTS Annotation", filler);
            }
        });
    }

    @Override
    public AnnotationView getView() {
        return view;
    }
}

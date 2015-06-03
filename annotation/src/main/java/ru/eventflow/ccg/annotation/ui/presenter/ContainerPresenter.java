package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.EditorCaretEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEvent;
import ru.eventflow.ccg.annotation.ui.event.TabEventHandler;
import ru.eventflow.ccg.annotation.ui.view.AnnotationView;
import ru.eventflow.ccg.annotation.ui.view.ContainerView;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ContainerPresenter implements Presenter<ContainerView> {

    private final ContainerView view;

    @Inject
    public ContainerPresenter(final EventBus eventBus) {
        this.view = new ContainerView();

        this.view.getTabbedPane().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
                if (tabbedPane.getTabCount() == 0) {
                    eventBus.fireEvent(new EditorCaretEvent(-1, -1));
                } else {
                    AnnotationView selected = (AnnotationView) tabbedPane.getSelectedComponent();
                    selected.getTextPane().requestFocusInWindow();
                }
            }
        });

        eventBus.addHandler(TabEvent.TYPE, new TabEventHandler() {
            @Override
            public void onEvent(TabEvent e) {
                Sentence sentence = e.getSentence();
                String title = "Sentence " + sentence.getId();
                AnnotationPresenter presenter = new AnnotationPresenter(eventBus, sentence);
                view.addTab(title, presenter.getView());
            }
        });
    }

    @Override
    public ContainerView getView() {
        return view;
    }
}

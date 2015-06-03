package ru.eventflow.ccg.annotation.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.*;
import ru.eventflow.ccg.annotation.ui.view.AnnotationView;
import ru.eventflow.ccg.annotation.ui.view.ContainerView;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ContainerPresenter implements Presenter<ContainerView> {

    private final ContainerView view;
    private List<AnnotationPresenter> annotationPresenters = new ArrayList<>();

    /**
     * this setting is global and should apply to newly created tabs as well
     */
    private boolean glossesVisible = true;

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
                annotationPresenters.add(presenter);
                presenter.getView().setGlossesVisible(glossesVisible);
                view.addTab(title, presenter.getView());
            }
        });

        eventBus.addHandler(SettingsEvent.TYPE, new SettingsEventHandler() {
            @Override
            public void onEvent(SettingsEvent e) {
                if (e.getSetting() == Setting.GLOSSES) {
                    glossesVisible = e.isEnabled();
                    for (AnnotationPresenter presenter : annotationPresenters) {
                        presenter.getView().setGlossesVisible(e.isEnabled());
                    }
                }
            }
        });

    }

    @Override
    public ContainerView getView() {
        return view;
    }
}

package ru.eventflow.ccg.annotation.ui.presenter;

import ru.eventflow.ccg.annotation.eventbus.EventBus;
import ru.eventflow.ccg.annotation.ui.event.SearchEvent;
import ru.eventflow.ccg.annotation.ui.view.SearchView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPresenter implements Presenter<SearchView>, ActionListener {

    private final SearchView view;
    private final EventBus eventBus;
    private Presenter<? extends Container> target;

    public SearchPresenter(final EventBus eventBus, Presenter<? extends Container> target) {
        this.eventBus = eventBus;
        this.target = target;
        this.view = new SearchView();

        this.view.getSearchBtn().addActionListener(this);
        this.view.getSearchTextField().addActionListener(this);
    }

    public void clear() {
        this.view.getSearchTextField().setText("");
    }

    @Override
    public SearchView getView() {
        return view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        eventBus.fireEvent(new SearchEvent(target, view.getSearchTextField().getText()));
    }

}

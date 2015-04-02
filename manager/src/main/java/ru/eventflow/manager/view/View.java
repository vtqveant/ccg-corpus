package ru.eventflow.manager.view;

import ru.eventflow.manager.presenter.Presenter;

public interface View<P extends Presenter> {

    public void setPresenter(P presenter);
}

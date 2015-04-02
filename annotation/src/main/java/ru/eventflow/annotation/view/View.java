package ru.eventflow.annotation.view;

import ru.eventflow.annotation.presenter.Presenter;

public interface View<P extends Presenter> {

    public void setPresenter(P presenter);
}

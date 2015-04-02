package ru.eventflow.annotation.presenter;

import java.awt.*;

public interface Presenter<T extends Container> {
    public T getView();
}

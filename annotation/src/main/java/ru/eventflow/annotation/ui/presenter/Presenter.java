package ru.eventflow.annotation.ui.presenter;

import java.awt.*;

public interface Presenter<T extends Container> {
    T getView();
}

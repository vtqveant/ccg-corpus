package ru.eventflow.annotation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.eventflow.annotation.presenter.DocumentsPresenter;
import ru.eventflow.annotation.presenter.MainPresenter;
import ru.eventflow.annotation.presenter.Presenter;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Injector injector = Guice.createInjector(new UIModule());

                final LoggingController loggingController = injector.getInstance(LoggingController.class);

                final Presenter mainPresenter = injector.getInstance(MainPresenter.class);
                final Presenter documentsPresenter = injector.getInstance(DocumentsPresenter.class);

                mainPresenter.getView().add(documentsPresenter.getView());

                // display
                JFrame frame = new JFrame("Eventflow Annotation Tool");
                frame.add(mainPresenter.getView());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}


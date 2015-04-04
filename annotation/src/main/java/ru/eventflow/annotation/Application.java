package ru.eventflow.annotation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.eventflow.annotation.presenter.*;
import ru.eventflow.annotation.view.DetailsView;
import ru.eventflow.annotation.view.DocumentsView;
import ru.eventflow.annotation.view.MainView;
import ru.eventflow.annotation.view.MenuView;

import javax.swing.*;
import java.awt.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Injector injector = Guice.createInjector(new UIModule());

                final LoggingController loggingController = injector.getInstance(LoggingController.class);

                final Presenter<MainView> mainPresenter = injector.getInstance(MainPresenter.class);
                final Presenter<DocumentsView> documentsPresenter = injector.getInstance(DocumentsPresenter.class);
                mainPresenter.getView().setLeftComponent(documentsPresenter.getView());
                final Presenter<DetailsView> detailsPresenter = injector.getInstance(DetailsPresenter.class);
                mainPresenter.getView().setRightComponent(detailsPresenter.getView());
                final Presenter<MenuView> menuPresenter = injector.getInstance(MenuPresenter.class);

                final JFrame frame = new JFrame("Eventflow Annotation Tool");
                frame.setJMenuBar(menuPresenter.getView());
                frame.setContentPane(mainPresenter.getView());
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setSize(new Dimension(640, 480));
                frame.setVisible(true);
            }
        });
    }
}


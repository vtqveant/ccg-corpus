package ru.eventflow.ccg.annotation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.eventflow.ccg.annotation.ui.presenter.*;
import ru.eventflow.ccg.annotation.ui.view.DetailsView;
import ru.eventflow.ccg.annotation.ui.view.DocumentsView;
import ru.eventflow.ccg.annotation.ui.view.MainView;
import ru.eventflow.ccg.annotation.ui.view.MenuView;

import javax.swing.*;
import java.awt.*;

public class Application {

    private static final Injector injector = Guice.createInjector(new AnnotationModule());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final LoggingController loggingController = injector.getInstance(LoggingController.class);
                final DataAccessController dataAccessController = injector.getInstance(DataAccessController.class);

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


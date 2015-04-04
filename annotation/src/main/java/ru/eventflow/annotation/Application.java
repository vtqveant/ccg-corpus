package ru.eventflow.annotation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.eventflow.annotation.data.CorpusDumpLoader;
import ru.eventflow.annotation.ui.presenter.*;
import ru.eventflow.annotation.ui.view.DetailsView;
import ru.eventflow.annotation.ui.view.DocumentsView;
import ru.eventflow.annotation.ui.view.MainView;
import ru.eventflow.annotation.ui.view.MenuView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

public class Application {

    private static final Injector injector = Guice.createInjector(new AnnotationModule());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));
                    String dumpLocation = properties.getProperty("opencorpora.dump.location");
                    if (dumpLocation != null) {
                        CorpusDumpLoader corpusDumpLoader = injector.getInstance(CorpusDumpLoader.class);
                        corpusDumpLoader.init(dumpLocation);
                    }
                } catch (IOException e) {
                    System.out.println("Premature exit due to misconfiguration");
                    System.exit(-1);
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

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


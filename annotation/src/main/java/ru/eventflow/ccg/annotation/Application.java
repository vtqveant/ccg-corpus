package ru.eventflow.ccg.annotation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import ru.eventflow.ccg.annotation.ui.presenter.*;
import ru.eventflow.ccg.annotation.ui.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class Application {

    private static final Injector injector = Guice.createInjector(new AnnotationModule());

    public static void main(String[] args) {
        try {
            // LaF properties
            Properties props = new Properties();
            props.put("logoString", "");
            props.put("windowDecoration", "off");
            props.put("linuxStyleScrollBar", "off");

            FastLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");

            final LoggingController loggingController = injector.getInstance(LoggingController.class);
            final DataAccessController dataAccessController = injector.getInstance(DataAccessController.class);

            final Presenter<MainView> mainPresenter = injector.getInstance(MainPresenter.class);
            final Presenter<MenuView> menuPresenter = injector.getInstance(MenuPresenter.class);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final JFrame frame = new JFrame("CCG Corpus Annotation Tool");
                    frame.setJMenuBar(menuPresenter.getView());
                    frame.setContentPane(mainPresenter.getView());
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setSize(new Dimension(640, 480));
                    frame.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}


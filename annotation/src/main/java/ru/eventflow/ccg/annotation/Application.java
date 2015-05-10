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
            // setup the look and feel properties
            Properties props = new Properties();
            props.put("logoString", "");
            props.put("windowDecoration", "off");
            props.put("linuxStyleScrollBar", "off");

            // set your theme
            FastLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final LoggingController loggingController = injector.getInstance(LoggingController.class);
                    final DataAccessController dataAccessController = injector.getInstance(DataAccessController.class);

                    final Presenter<TreeView> documentsPresenter = injector.getInstance(TreePresenter.class);
                    final Presenter<TextView> detailsPresenter = injector.getInstance(TextPresenter.class);
                    final Presenter<FTSAnnotationView> ftsAnnotationdetailsPresenter = injector.getInstance(FTSAnnotationPresenter.class);

                    final Presenter<MainView> mainPresenter = injector.getInstance(MainPresenter.class);
                    mainPresenter.getView().setLeftComponent(documentsPresenter.getView());
                    mainPresenter.getView().addTab("Text", detailsPresenter.getView());
                    mainPresenter.getView().addTab("FTS Annotation", ftsAnnotationdetailsPresenter.getView());

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

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}


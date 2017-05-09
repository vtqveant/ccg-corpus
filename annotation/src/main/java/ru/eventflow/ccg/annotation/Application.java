package ru.eventflow.ccg.annotation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import ru.eventflow.ccg.annotation.ui.presenter.*;
import ru.eventflow.ccg.annotation.ui.view.MainView;
import ru.eventflow.ccg.annotation.ui.view.MenuView;
import ru.eventflow.ccg.annotation.ui.view.OpenDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Properties;

public class Application {

    private static final Injector injector = Guice.createInjector(new GUIModule());
    private static final ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/corpus.png"));

    public static void main(String[] args) {
        try {
            // LaF properties
            Properties props = new Properties();
            props.put("logoString", "");
            props.put("windowDecoration", "off");
            props.put("linuxStyleScrollBar", "off");
            props.put("tooltipBorderSize", "1");
            props.put("tooltipBackgroundColor", "255 255 204"); // light yellow
            props.put("tooltipCastShadow", "on");
            props.put("tooltipShadowSize", "1");
            props.put("controlTextFont", "Arial plain 12");
            props.put("systemTextFont", "Arial plain 12");
            props.put("userTextFont", "Arial plain 12");
            props.put("menuTextFont", "Arial plain 12");
            props.put("subTextFont", "Arial plain 12");

            FastLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");

            final LoggingController loggingController = injector.getInstance(LoggingController.class);

            final Presenter<MainView> mainPresenter = injector.getInstance(MainPresenter.class);
            final Presenter<MenuView> menuPresenter = injector.getInstance(MenuPresenter.class);

            Arrays.asList(AboutDialogPresenter.class, SettingsDialogPresenter.class, OpenDialogPresenter.class).forEach(injector::getInstance);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final JFrame frame = new JFrame("CCG Corpus Annotation Tool");
                    frame.setJMenuBar(menuPresenter.getView());
                    frame.setContentPane(mainPresenter.getView());
                    frame.setLocationByPlatform(true);
                    frame.setIconImage(icon.getImage());
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


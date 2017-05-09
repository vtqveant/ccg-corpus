package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class AboutDialog extends AbstractDialog implements WindowFocusListener {

    private static final String HTML = "<html>" +
            "<div height=\"110\">" +
            "CCG Corpus Annotation Tool<br/>" +
            "</div>" +
            "<div align=\"right\"><font size=\"-2\">by Kosta Sokolov<br/>vtqveant@gmail.com</font></div></html>";

    @Inject
    public AboutDialog(final JFrame owner) {
        super(owner, null, false);

        setUndecorated(true);
        addWindowFocusListener(this);

        JLabel label = new JLabel();
        label.setBackground(Color.LIGHT_GRAY);
        label.setText(HTML);
        label.setFont(Defaults.SMALL_FONT);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(label);
        setContentPane(panel);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        // nothing
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        setVisible(false);
    }
}

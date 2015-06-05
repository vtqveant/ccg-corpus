package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SecondaryTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public void setValue(Object value) {
        setText(String.valueOf(value));
        setFont(Defaults.SMALL_FONT);
        setForeground(Color.GRAY);
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}

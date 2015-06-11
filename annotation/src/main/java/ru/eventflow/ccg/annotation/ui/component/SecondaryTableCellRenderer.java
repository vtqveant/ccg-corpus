package ru.eventflow.ccg.annotation.ui.component;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SecondaryTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public void setValue(Object value) {
        setText(String.valueOf(value));
        setBorder(noFocusBorder);
        setFont(Defaults.SMALL_FONT);
        setForeground(Color.GRAY);
        setHorizontalAlignment(SwingConstants.LEFT);
    }
}

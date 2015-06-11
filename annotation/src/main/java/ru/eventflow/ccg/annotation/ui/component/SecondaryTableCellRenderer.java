package ru.eventflow.ccg.annotation.ui.component;

import javax.swing.*;
import java.awt.*;

public class SecondaryTableCellRenderer extends NoBorderTableCellRenderer {
    @Override
    public void setValue(Object value) {
        setText(String.valueOf(value));
        setForeground(Color.GRAY);
        setHorizontalAlignment(SwingConstants.LEFT);
    }
}

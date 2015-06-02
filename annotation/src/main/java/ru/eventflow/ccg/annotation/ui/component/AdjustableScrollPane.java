package ru.eventflow.ccg.annotation.ui.component;

import javax.swing.*;
import java.awt.*;

/**
 * Contains a workaround to dynamically adjust a scrollbar.
 * <p>
 * s. http://stackoverflow.com/a/15712452
 */
public class AdjustableScrollPane extends JScrollPane {

    public AdjustableScrollPane() {
        getViewport().setLayout(new ConstrainedViewPortLayout());
    }

    private class ConstrainedViewPortLayout extends ViewportLayout {
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension preferredViewSize = super.preferredLayoutSize(parent);
            Container viewportContainer = parent.getParent();
            if (viewportContainer != null) {
                Dimension parentSize = viewportContainer.getSize();
                preferredViewSize.width = parentSize.width;
            }
            return preferredViewSize;
        }
    }

}

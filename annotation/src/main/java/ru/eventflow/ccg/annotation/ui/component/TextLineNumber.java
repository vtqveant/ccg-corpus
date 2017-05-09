package ru.eventflow.ccg.annotation.ui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

/**
 * This class will display line numbers for a related text component. The text component must use the same line height
 * for each line. TextLineNumber supports wrapped lines and will highlight the line number of the current line in
 * the text component.
 * <p>
 * This class was designed to be used as a component added to the row header of a JScrollPane.
 * <p>
 * https://tips4java.wordpress.com/2009/05/23/text-component-line-number/
 * <p>
 * Usage:
 * <p>
 * JTextPane textPane = new JTextPane();
 * JScrollPane scrollPane = new JScrollPane(textPane);
 * TextLineNumber tln = new TextLineNumber(textPane);
 * scrollPane.setRowHeaderView(tln);
 */
public class TextLineNumber extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {

    public final static float LEFT = 0.0f;
    public final static float CENTER = 0.5f;
    public final static float RIGHT = 1.0f;

    private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

    /**
     * Text component this TextTextLineNumber component is in sync with
     */
    private JTextComponent component;

    //  Properties that can be changed
    private boolean updateFont;
    private int borderGap;
    private Color currentLineBackground;
    private float digitAlignment;
    private int minimumDisplayDigits;

    //  Keep history information to reduce the number of times the component needs to be repainted
    private int lastDigits;
    private int lastHeight;
    private int lastLine;

    private HashMap<String, FontMetrics> fonts;

    /**
     * Color of line numbers
     */
    private Color color;

    /**
     * Create a line number component for a text component. This minimum display width will be based on 3 digits.
     *
     * @param component the related text component
     */
    public TextLineNumber(JTextComponent component) {
        this(component, 3);
    }

    /**
     * Create a line number component for a text component.
     *
     * @param component            the related text component
     * @param minimumDisplayDigits the number of digits used to calculate the minimum width of the component
     */
    public TextLineNumber(JTextComponent component, int minimumDisplayDigits) {
        this.component = component;

        setFont(component.getFont());
        setBorderGap(5);
        setColor(new Color(0x800000));
        setCurrentLineBackground(new Color(0xfffcdf));
        setDigitAlignment(LEFT);
        setMinimumDisplayDigits(minimumDisplayDigits);

        component.getDocument().addDocumentListener(this);
        component.addCaretListener(this);
        component.addPropertyChangeListener("font", this);
    }

    /**
     * Gets the update font property
     *
     * @return the update font property
     */
    public boolean getUpdateFont() {
        return updateFont;
    }

    /**
     * Set the update font property. Indicates whether this Font should be updated automatically when the Font of
     * the related text component is changed.
     *
     * @param updateFont when true update the Font and repaint the line numbers, otherwise just repaint the line numbers
     */
    public void setUpdateFont(boolean updateFont) {
        this.updateFont = updateFont;
    }

    /**
     * Gets the border gap
     *
     * @return the border gap in pixels
     */
    public int getBorderGap() {
        return borderGap;
    }

    /**
     * The border gap is used in calculating the left and right insets of the border. Default value is 5.
     *
     * @param borderGap the gap in pixels
     */
    public void setBorderGap(int borderGap) {
        this.borderGap = borderGap;
        setBorder(new EmptyBorder(0, borderGap, 0, borderGap));
        lastDigits = 0;
        setPreferredWidth();
    }

    /**
     * Gets the current line background rendering Color
     *
     * @return the Color used to render the current line number
     */
    public Color getCurrentLineBackground() {
        return currentLineBackground == null ? getBackground() : currentLineBackground;
    }

    /**
     * The Color used to render the current line digits background
     *
     * @param currentLineBackground the Color used to render the current line
     */
    public void setCurrentLineBackground(Color currentLineBackground) {
        this.currentLineBackground = currentLineBackground;
    }

    /**
     * Gets the digit alignment
     *
     * @return the alignment of the painted digits
     */
    public float getDigitAlignment() {
        return digitAlignment;
    }

    /**
     * Specify the horizontal alignment of the digits within the component.
     * Common values would be:
     * <ul>
     * <li>TextLineNumber.LEFT
     * <li>TextLineNumber.CENTER
     * <li>TextLineNumber.RIGHT (default)
     * </ul>
     */
    public void setDigitAlignment(float digitAlignment) {
        this.digitAlignment = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the minimum display digits
     *
     * @return the minimum display digits
     */
    public int getMinimumDisplayDigits() {
        return minimumDisplayDigits;
    }

    /**
     * Specify the mimimum number of digits used to calculate the preferred
     * width of the component. Default is 3.
     *
     * @param minimumDisplayDigits the number digits used in the preferred width calculation
     */
    public void setMinimumDisplayDigits(int minimumDisplayDigits) {
        this.minimumDisplayDigits = minimumDisplayDigits;
        setPreferredWidth();
    }

    /**
     * Calculate the width needed to display the maximum line number
     */
    private void setPreferredWidth() {
        Element root = component.getDocument().getDefaultRootElement();
        int lines = root.getElementCount();
        int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

        //  Update sizes when number of digits in the line number changes

        if (lastDigits != digits) {
            lastDigits = digits;
            FontMetrics fontMetrics = getFontMetrics(getFont());
            int width = fontMetrics.charWidth('0') * digits;
            Insets insets = getInsets();
            int preferredWidth = insets.left + insets.right + width;

            Dimension d = getPreferredSize();
            d.setSize(preferredWidth, HEIGHT);
            setPreferredSize(d);
            setSize(d);
        }
    }

    /**
     * Draw the line numbers
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);

        //	Determine the width of the space available to draw the line number
        FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
        Insets insets = getInsets();
        int availableWidth = getSize().width - insets.left - insets.right;

        //  Determine the rows to draw within the clipped bounds
        Rectangle clip = g.getClipBounds();
        int rowStartOffset = component.viewToModel(new Point(0, clip.y));
        int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

        while (rowStartOffset <= endOffset) {
            try {
                //  get the line number as a string and then determine the "X" and "Y" offsets for drawing the string
                String lineNumber = getTextLineNumber(rowStartOffset);
                int stringWidth = fontMetrics.stringWidth(lineNumber);
                int x = getOffsetX(availableWidth, stringWidth) + insets.left;
                int y = getOffsetY(rowStartOffset, fontMetrics);

                // background
                if (isCurrentLine(rowStartOffset)) {
                    Rectangle r = component.modelToView(rowStartOffset);
                    g.setColor(getCurrentLineBackground());
                    g.fillRect(0, r.y, getSize().width, r.height);
                    g.setColor(getColor());
                }

                // the number
                g.drawString(lineNumber, x, y);

                // move to the next row
                rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * We need to know if the caret is currently positioned on the line we
     * are about to paint so the line number can be highlighted.
     */
    private boolean isCurrentLine(int rowStartOffset) {
        int caretPosition = component.getCaretPosition();
        Element root = component.getDocument().getDefaultRootElement();
        return (root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition));
    }

    /**
     * Get the line number to be drawn. The empty string will be returned when a line of text has wrapped.
     */
    protected String getTextLineNumber(int rowStartOffset) {
        Element root = component.getDocument().getDefaultRootElement();
        int index = root.getElementIndex(rowStartOffset);
        Element line = root.getElement(index);

        if (line.getStartOffset() == rowStartOffset) {
            return String.valueOf(index + 1);
        } else {
            return "";
        }
    }

    /**
     * Determine the X offset to properly align the line number when drawn
     */
    private int getOffsetX(int availableWidth, int stringWidth) {
        return (int) ((availableWidth - stringWidth) * digitAlignment);
    }

    /**
     * Determine the Y offset for the current row
     */
    private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics) throws BadLocationException {
        //  Get the bounding rectangle of the row
        Rectangle r = component.modelToView(rowStartOffset);
        int lineHeight = fontMetrics.getHeight();
        int y = r.y + r.height;
        int descent = 0;

        //  The text needs to be positioned above the bottom of the bounding
        //  rectangle based on the descent of the font(s) contained on the row.
        if (r.height == lineHeight) { // default font is being used
            descent = fontMetrics.getDescent();
        } else { // We need to check all the attributes for font changes
            if (fonts == null) {
                fonts = new HashMap<String, FontMetrics>();
            }

            Element root = component.getDocument().getDefaultRootElement();
            int index = root.getElementIndex(rowStartOffset);
            Element line = root.getElement(index);

            for (int i = 0; i < line.getElementCount(); i++) {
                Element child = line.getElement(i);
                AttributeSet as = child.getAttributes();
                String fontFamily = (String) as.getAttribute(StyleConstants.FontFamily);
                Integer fontSize = (Integer) as.getAttribute(StyleConstants.FontSize);
                String key = fontFamily + fontSize;

                FontMetrics fm = fonts.get(key);

                if (fm == null) {
                    Font font = new Font(fontFamily, Font.PLAIN, fontSize);
                    fm = component.getFontMetrics(font);
                    fonts.put(key, fm);
                }

                descent = Math.max(descent, fm.getDescent());
            }
        }

        return y - descent;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        //  Get the line the caret is positioned on
        int caretPosition = component.getCaretPosition();
        Element root = component.getDocument().getDefaultRootElement();
        int currentLine = root.getElementIndex(caretPosition);

        //  Need to repaint so the correct line number can be highlighted
        if (lastLine != currentLine) {
            repaint();
            lastLine = currentLine;
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    /**
     * A document change may affect the number of displayed lines of text, therefore the lines numbers will also change
     */
    private void documentChanged() {
        //  View of the component has not been updated at the time the DocumentEvent is fired
        SwingUtilities.invokeLater(() -> {
            try {
                int endPos = component.getDocument().getLength();
                Rectangle rect = component.modelToView(endPos);

                if (rect != null && rect.y != lastHeight) {
                    setPreferredWidth();
                    repaint();
                    lastHeight = rect.y;
                }
            } catch (BadLocationException ex) { /* nothing to do */ }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof Font) {
            if (updateFont) {
                Font newFont = (Font) evt.getNewValue();
                setFont(newFont);
                lastDigits = 0;
                setPreferredWidth();
            } else {
                repaint();
            }
        }
    }
}

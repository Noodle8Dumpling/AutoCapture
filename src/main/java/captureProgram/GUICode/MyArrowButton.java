package captureProgram.GUICode;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;

public class MyArrowButton extends JButton implements SwingConstants {
    /**
     * The direction of the arrow. One of
     * {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
     * {@code SwingConstants.EAST} or {@code SwingConstants.WEST}.
     */
    protected int direction;

    private Color shadow;
    private Color darkShadow;
    private Color highlight;

    /**
     * Creates a {@code BasicArrowButton} whose arrow
     * is drawn in the specified direction and with the specified
     * colors.
     *
     * @param direction  the direction of the arrow; one of
     *                   {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
     *                   {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
     * @param background the background color of the button
     * @param shadow     the color of the shadow
     * @param darkShadow the color of the dark shadow
     * @param highlight  the color of the highlight
     * @since 1.4
     */
    public MyArrowButton(int direction, Color background, Color shadow,
                         Color darkShadow, Color highlight) {
        super();
        setRequestFocusEnabled(false);
        setDirection(direction);
        setBackground(background);
        this.shadow = shadow;
        this.darkShadow = darkShadow;
        this.highlight = highlight;
    }

    /**
     * Creates a {@code BasicArrowButton} whose arrow
     * is drawn in the specified direction.
     *
     * @param direction the direction of the arrow; one of
     *                  {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
     *                  {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
     */
    public MyArrowButton(int direction) {
        this(direction, UIManager.getColor("control"), UIManager.getColor("controlShadow"),
                UIManager.getColor("controlDkShadow"), UIManager.getColor("controlLtHighlight"));
    }

    /**
     * Returns the direction of the arrow.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the arrow.
     *
     * @param direction the direction of the arrow; one of
     *                  of {@code SwingConstants.NORTH},
     *                  {@code SwingConstants.SOUTH},
     *                  {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void paint(Graphics g) {
        Color origColor;
        boolean isPressed, isEnabled;
        int w, h, size;

        w = getSize().width;
        h = getSize().height;
        //origColor = g.getColor();
        origColor = new Color(248, 248, 248);
        isPressed = getModel().isPressed();
        isEnabled = isEnabled();

        //g.setColor(getBackground());
        g.setColor(new Color(248, 248, 248));
        g.fillRect(1, 1, w - 2, h - 2);

        /// Draw the proper Border
        if (getBorder() != null && !(getBorder() instanceof UIResource)) {
            paintBorder(g);
        } else if (isPressed) {
            //g.setColor(shadow);
            g.drawRect(0, 0, w - 1, h - 1);
        } else {
            // Using the background color set above
            g.drawLine(0, 0, 0, h - 1);
            g.drawLine(1, 0, w - 2, 0);

            //g.setColor(highlight);    // inner 3D border
            g.drawLine(1, 1, 1, h - 3);
            g.drawLine(2, 1, w - 3, 1);

            //g.setColor(shadow);       // inner 3D border
            g.drawLine(1, h - 2, w - 2, h - 2);
            g.drawLine(w - 2, 1, w - 2, h - 3);

            //g.setColor(darkShadow);     // black drop shadow  __|
            g.drawLine(0, h - 1, w - 1, h - 1);
            g.drawLine(w - 1, h - 1, w - 1, 0);
        }

        // If there's no room to draw arrow, bail
        if (h < 5 || w < 5) {
            g.setColor(origColor);
            return;
        }

        if (isPressed) {
            g.translate(1, 1);
        }

        // Draw the arrow
        size = Math.min((h - 4) / 3, (w - 4) / 3);
        size = Math.max(size, 2);
        paintTriangle(g, (w - size) / 2, (h - size) / 2,
                size, direction, isEnabled);

        // Reset the Graphics back to it's original settings
        if (isPressed) {
            g.translate(-1, -1);
        }
        g.setColor(origColor);

    }

    /**
     * Returns the preferred size of the {@code BasicArrowButton}.
     *
     * @return the preferred size
     */
    public Dimension getPreferredSize() {
        return new Dimension(16, 16);
    }

    /**
     * Returns the minimum size of the {@code BasicArrowButton}.
     *
     * @return the minimum size
     */
    public Dimension getMinimumSize() {
        return new Dimension(5, 5);
    }

    /**
     * Returns the maximum size of the {@code BasicArrowButton}.
     *
     * @return the maximum size
     */
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns whether the arrow button should get the focus.
     * {@code BasicArrowButton}s are used as a child component of
     * composite components such as {@code JScrollBar} and
     * {@code JComboBox}. Since the composite component typically gets the
     * focus, this method is overriden to return {@code false}.
     *
     * @return {@code false}
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * Paints a triangle.
     *
     * @param g         the {@code Graphics} to draw to
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param size      the size of the triangle to draw
     * @param direction the direction in which to draw the arrow;
     *                  one of {@code SwingConstants.NORTH},
     *                  {@code SwingConstants.SOUTH}, {@code SwingConstants.EAST} or
     *                  {@code SwingConstants.WEST}
     * @param isEnabled whether or not the arrow is drawn enabled
     */
    public void paintTriangle(Graphics g, int x, int y, int size,
                              int direction, boolean isEnabled) {
        Color oldColor = g.getColor();
        int mid, i, j;

        j = 0;
        size = Math.max(size, 2);
        mid = (size / 2) - 1;

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(x, y);
        if (isEnabled)
            g2.setColor(new Color(15, 155, 246));
        else
            g.setColor(new Color(15, 155, 246));

        switch (direction) {
            case NORTH:
                /*for (i = 0; i < size; i++) {
                    g.drawLine(mid - i, i, mid + i, i);
                }*/
                /*if (!isEnabled) {
                    g.setColor(highlight);
                    g.drawLine(mid - i + 2, i, mid + i, i);
                }*/
                g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                g2.drawLine(mid - 3, 5, mid + 1, 1);
                g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                g2.drawLine(mid + 1, 1, mid + 5, 5);
                break;
            case SOUTH:
                if (!isEnabled) {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(mid - i, j, mid + i, j);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(shadow);
                }

                /*j = 0;
                for(i = size-1; i >= 0; i--)   {
                    g.drawLine(mid-i, j, mid+i, j);
                    j++;
                }*/
                g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                g2.drawLine(mid - 3, 1, mid + 1, 5);
                g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                g2.drawLine(mid + 1, 5, mid + 5, 1);
                break;
            case WEST:
                for (i = 0; i < size; i++) {
                    g.drawLine(i, mid - i, i, mid + i);
                }
                if (!isEnabled) {
                    g.setColor(highlight);
                    g.drawLine(i, mid - i + 2, i, mid + i);
                }
                break;
            case EAST:
                if (!isEnabled) {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(j, mid - i, j, mid + i);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(shadow);
                }

                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(j, mid - i, j, mid + i);
                    j++;
                }
                break;
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }
}

package captureProgram.GUICode;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static sun.swing.SwingUtilities2.drawHLine;
import static sun.swing.SwingUtilities2.drawRect;
import static sun.swing.SwingUtilities2.drawVLine;

public class MyScrollBarUI extends BasicScrollBarUI {
    @Override
    protected JButton createDecreaseButton(int orientation) {
        MyArrowButton btn = new MyArrowButton(BasicArrowButton.NORTH);
        btn.setBorder(null);
        return btn;
        //return super.createDecreaseButton(orientation);
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        MyArrowButton btn = new MyArrowButton(BasicArrowButton.SOUTH);
        btn.setBorder(null);
        return btn;
        //return super.createIncreaseButton(orientation);
    }

    @Override
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if(thumbBounds.isEmpty() || !scrollbar.isEnabled())     {
            return;
        }

        int w = thumbBounds.width;
        int h = thumbBounds.height;

        g.translate(thumbBounds.x, thumbBounds.y);

        g.setColor(new Color(243, 244, 246));
        drawRect(g, 0, 0, w - 1, h - 1);
        g.setColor(new Color(243, 244, 246));
        g.fillRect(0, 0, w - 1, h - 1);

        g.setColor(thumbHighlightColor);
        drawVLine(g, 1, 1, h - 2);
        drawHLine(g, 2, w - 3, 1);

        g.setColor(new Color(15, 155, 246));
        drawHLine(g, 2, w - 2, h - 2);
        drawVLine(g, w - 2, 1, h - 3);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        super.paintTrack(g, c, trackBounds);
    }
}

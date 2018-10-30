package optimizationprogram.GUICode;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class MyClassicButtonUI extends BasicButtonUI {
    private static final Color BUTTON_COLOR1 = new Color(205,255,205);
    private static final Color BUTTON_COLOR2 = new Color(51,154,47);
    public MyClassicButtonUI(){
        super();
    }
    public void paint(Graphics g,JComponent c){
        super.paint(g, c);
        //Graphics2D g2d=(Graphics2D)g;
        /*int h=c.getHeight();
        int w=c.getWidth();
        float tran=1F;

        Graphics2D graphics2 = (Graphics2D) g;
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, w-1,h-1, 2, 2);
        graphics2.setColor(new Color(204, 0, 0));
        graphics2.draw(roundedRectangle);*/



        //graphics2.f
        //graphics2.fillRect(0,0,w-1,h-1);
        //graphics2.fillRoundRect(0,0,w-1,h-1,2,2);



        //graphics2.setFont(new Font("微软雅黑",0,12));
        //graphics2.setColor(Color.white);
        //graphics2.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL));
        //graphics2.drawString("开 始",w/2-15,h/2+5);
        //graphics2.setBackground(Color.green);

        //graphics2.dispose();
        /*g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint p1;
        GradientPaint p2;
        p1=new GradientPaint(0,0,new Color(0,0,0), 0,h-1,new Color(100,100,100));
        //p2=new GradientPaint(0,1,new Color(0,0,0,50),0,h-3,new Color(255,255,255,100));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,tran));
        RoundRectangle2D.Float r2d=new RoundRectangle2D.Float(0,0,w-1,h-1,5,5);
        Shape clip=g2d.getClip();
        g2d.clip(r2d);
        GradientPaint gp=new GradientPaint(0.0F,0.0F,BUTTON_COLOR1,0.0F,h,BUTTON_COLOR2,true);
        g2d.setPaint(gp);
        g2d.fillRect(0,0,w,h);
        g2d.setClip(clip);
        g2d.setPaint(p1);
        g2d.drawRoundRect(0,0,w-1,h-1,20,20);
        //g2d.setPaint(p2);
        //g2d.drawRoundRect(1,1,w-3,h-3,18,18);*/
        //g2d.dispose();
    }

    @Override
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Icon icon = b.getIcon();
        Icon tmpIcon = null;

        if(icon == null) {
            return;
        }

        Icon selectedIcon = null;

            /* the fallback icon should be based on the selected state */
        if (model.isSelected()) {
            selectedIcon = b.getSelectedIcon();
            if (selectedIcon != null) {
                icon = selectedIcon;
            }
        }

        if(!model.isEnabled()) {
            if(model.isSelected()) {
                tmpIcon = b.getIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }

            if (tmpIcon == null) {
                tmpIcon = b.getIcon();
            }
        } else if(model.isPressed() && model.isArmed()) {
            tmpIcon = b.getPressedIcon();
            if(tmpIcon != null) {
                // revert back to 0 offset
                clearTextShiftOffset();
            }
        } else if(b.isRolloverEnabled() && model.isRollover()) {
            if(model.isSelected()) {
                tmpIcon = b.getRolloverSelectedIcon();
                if (tmpIcon == null) {
                    tmpIcon = selectedIcon;
                }
            }

            if (tmpIcon == null) {
                tmpIcon = b.getRolloverIcon();
            }
        }

        if(tmpIcon != null) {
            icon = tmpIcon;
        }

        if(model.isPressed() && model.isArmed()) {
            icon.paintIcon(c, g, iconRect.x + getTextShiftOffset(),
                    iconRect.y + getTextShiftOffset());
        } else {
            icon.paintIcon(c, g, iconRect.x, iconRect.y);
        }

    }

    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
        int mnemonicIndex = b.getDisplayedMnemonicIndex();

        /* Draw the Text */
        if(model.isEnabled()) {
            /*** paint the text normally */
            g.setColor(b.getForeground());
            SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
                    textRect.x + getTextShiftOffset(),
                    textRect.y + fm.getAscent() + getTextShiftOffset());
        }
        else {
            /*** paint the text disabled ***/
            /*g.setColor(b.getForeground().brighter());
            SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
                    textRect.x, textRect.y + fm.getAscent());*/
            //g.setColor(b.getForeground().darker());
            g.setColor(new Color(153, 153, 153));
            SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
                    textRect.x - 1, textRect.y + fm.getAscent() - 1);
        }
    }
}

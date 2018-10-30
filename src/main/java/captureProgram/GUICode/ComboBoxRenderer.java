package captureProgram.GUICode;

import javafx.scene.layout.Border;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class ComboBoxRenderer extends JLabel
        implements ListCellRenderer {
    private String[] comboValue;

    public ComboBoxRenderer(String[] comboValue) {
        this.comboValue = comboValue;
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        int selectedIndex = ((Integer) value).intValue();

        //setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            //setBackground(new Color(153, 153, 153));
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            //setBackground(new Color(248, 248, 248));
            //setForeground(list.getForeground());
        }

        //String comboValue = CaptureGUI.netWorkComboValue[selectedIndex];
        //setBorder(BorderFactory.createLineBorder(new Color(153, 153, 153)));
        setText(comboValue[selectedIndex]);
        setToolTipText(comboValue[selectedIndex]);
        return this;
    }

}

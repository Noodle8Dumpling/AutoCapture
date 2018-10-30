package optimizationprogram.GUICode;

import javax.swing.*;

public class MyFileChooser extends JFileChooser {
    public MyFileChooser() {
        UIManager.put("ScrollBarUI", new MyScrollBarUI());
        UIManager.put("FileView.directoryIcon",
                new ImageIcon(getClass().getResource("/captureProgram/GUICode/u422.png")));
    }

}

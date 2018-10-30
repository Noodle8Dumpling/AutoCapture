package captureProgram.GUICode;


import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class MyFileChooserUI extends BasicFileChooserUI{
    public static ComponentUI createUI(JComponent c) {
        return new MyFileChooserUI((JFileChooser) c);
    }
    public MyFileChooserUI(JFileChooser b) {
        super(b);
    }
}

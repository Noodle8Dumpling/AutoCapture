package optimizationprogram.tools;

import javax.swing.*;
import java.awt.*;

/**
 *
 * 设置对话弹窗
 */
public class ShowMsgDialogue {
    public void showInfoWin(String content){
        JLabel label = new JLabel(content);
        label.setFont(new Font("微软雅黑", 0, 13));
        JOptionPane.showMessageDialog(new JFrame(), label, "Tip", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorWin(String content){
        JLabel label = new JLabel(content);
        label.setFont(new Font("微软雅黑", 0, 13));
        JOptionPane.showMessageDialog(new JFrame(), label, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

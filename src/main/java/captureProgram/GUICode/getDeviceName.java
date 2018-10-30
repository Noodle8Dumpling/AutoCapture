package captureProgram.GUICode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

public class getDeviceName {
    public void getComboBoxValue() {
        Boolean flag = false;
        String comboBoxValue = "";
        String cmdStr = "";
        //获得adb.exe文件路径
        Map map = System.getenv();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if ("ANDROID_HOME".equals(entry.getKey().toString())) {
                cmdStr = entry.getValue().toString() + "\\platform-tools\\adb.exe" + " devices";
                break;
            }

        }

        BufferedReader adbCmd = null;
        String line = null;
        try {
            //将命令行放到cmd中运行
            //Process p = Runtime.getRuntime().exec("E:\\Android\\android-sdk_r24.4.1-windows\\android-sdk-windows\\platform-tools\\adb.exe devices");
            if ("".equals(cmdStr)){
                return;
            }
            Process p = Runtime.getRuntime().exec(cmdStr);
            //读取执行命令后的输出结果
            adbCmd = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = adbCmd.readLine()) != null) {
                if ("List of devices attached".equals(line)) {
                    flag = true;
                    continue;
                }
                if (flag && !"".equals(line) && line.contains("device")) {
                    comboBoxValue = comboBoxValue + line.substring(0, line.indexOf("device")) + ";";
                }
            }
            if (comboBoxValue.contains(";") ) {
                if (comboBoxValue.split(";").length >= 2){
                    comboBoxValue = comboBoxValue.substring(0, comboBoxValue.length() - 1);
                    //System.out.println("xxxxxxxxxxxxx"+comboBoxValue);
                    //CaptureGUI.deviceNameComboValue = comboBoxValue.split(";");
                    CaptureGUI.deviceNameComboValue = comboBoxValue.split(";");
                }else {
                    //CaptureGUI.deviceNameComboValue = new String[1];
                    //CaptureGUI.deviceNameComboValue[0] = comboBoxValue.substring(0, comboBoxValue.length() - 1);
                    CaptureGUI.deviceNameComboValue = new String[1];
                    CaptureGUI.deviceNameComboValue[0] = comboBoxValue.substring(0, comboBoxValue.length() - 1);
                }

            } else {
                //CaptureGUI.deviceNameComboValue = new String[1];
                //CaptureGUI.deviceNameComboValue[0] = "<无>";
                CaptureGUI.deviceNameComboValue = new String[1];
                CaptureGUI.deviceNameComboValue[0] = "<无>";

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (adbCmd != null) {
                try {
                    adbCmd.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

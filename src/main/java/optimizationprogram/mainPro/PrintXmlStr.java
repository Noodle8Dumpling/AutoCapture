package optimizationprogram.mainPro;

import io.appium.java_client.android.AndroidDriver;
import optimizationprogram.GUICode.CaptureGUI;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrintXmlStr {
    private AndroidDriver driver;
    private String btnXp;
    private String appName;
    private List<String> pkgList;

    public PrintXmlStr(AndroidDriver driver, SelectedAppInfo sai) {
        this.driver = driver;
        this.btnXp = sai.getOpenXPath();
        this.appName = sai.getAppName();
    }

    public void outputXmlFile() {
        String xmlstr = "";
        FileWriter writer;
        File file;
        //启动应用 点击“打开按钮”
        if (StringUtils.isNotEmpty(btnXp)) {
            try {
                CaptureGUI.textArea_log.append("INFO:正在打开应用···\n");
                driver.findElementByXPath(btnXp).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(45 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            xmlstr = driver.getPageSource();
            try {
                file = new File("C:\\Users\\admain\\Desktop\\自动化测试\\test"
                        + new SimpleDateFormat("yyyyMMdd")
                        .format(new Date())
                        + "\\xml");
                if (!file.exists()) {
                    file.mkdirs();
                }
                writer = new FileWriter("C:\\Users\\admain\\Desktop\\自动化测试\\test"
                        + new SimpleDateFormat("yyyyMMdd")
                        .format(new Date())
                        + "\\xml\\" + appName + ".xml");
                writer.write(xmlstr);
                writer.flush();
                writer.close();
                System.out.println("------------ " + appName + " 主界面xml文件已输出" + "----------------");
            } catch (IOException e) {
                CaptureGUI.textArea_log.append("ERROR:xml信息输出到文件时出现异常。\n");
                e.printStackTrace();
            }
            pkgList = getAppPackage();
            for (String pkg : pkgList) {
                if (StringUtils.isNotEmpty(pkg)) {
                    int ustStatus = uninstallApp(pkg);
                    if (ustStatus == 1) {
                        CaptureGUI.textArea_log.append("INFO:应用已卸载。\n");
                    }
                } else {
                    CaptureGUI.textArea_log.append("INFO:未发现安装包\n");
                }
            }
        }
    }

    public List<String> getAppPackage() {
        List<String> pkgList = new ArrayList<>();
        BufferedReader br = null;
        try {
            //将命令行放到cmd中运行
            Process p = Runtime.getRuntime().exec("adb shell pm list packages -3");
            //读取执行命令后的输出结果
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                //读取包含packagename的行
                if (!line.contains("io.appium") && line.contains("package:")) {
                    pkgList.add(line.substring(8, line.length()));
                }
            }

        } catch (Exception e) {
            CaptureGUI.textArea_log.append("ERROR:获取apk包名失败。\n");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return pkgList;
    }

    public int uninstallApp(String pkg) {
        BufferedReader installBr = null;
        try {
            CaptureGUI.textArea_log.append("INFO:应用卸载中···\n");
            //将命令行放到cmd中运行
            Runtime.getRuntime().exec("adb uninstall " + pkg);

            /*//读取执行命令后的输出结果
            installBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = installBr.readLine()) != null) {
                System.out.println(line.toString());
            }*/
            return 1;
        } catch (Exception e) {
            CaptureGUI.textArea_log.append("ERROR:应用卸载失败...\n");
            e.printStackTrace();
            return -1;
        } finally {
            if (installBr != null) {
                try {
                    installBr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

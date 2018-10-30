package optimizationprogram.mainPro;

import io.appium.java_client.DriverMobileCommand;
import io.appium.java_client.android.AndroidDriver;
import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.services.XPathConstant;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.And;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.openqa.selenium.By;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UninstallApp {
    private SelectedAppInfo bean;
    private Document document;
    private String appName;
    private AndroidDriver driver;
    private String xmlStr = null;

    public UninstallApp(AndroidDriver driver, SelectedAppInfo bean) {
        this.driver = driver;
        this.bean = bean;
        appName = bean.getAppName();
    }

    public void uninstallApp() {
        List<String> pkgList = getAppPackage();
        BufferedReader installBr = null;
        for (String pkg : pkgList) {
            try {
                CaptureGUI.textArea_log.append("INFO:应用卸载中···\n");
                //将命令行放到cmd中运行
                Runtime.getRuntime().exec("adb uninstall " + pkg);
                Thread.sleep(3000);
                CaptureGUI.textArea_log.append("INFO:应用已卸载。\n");
            } catch (Exception e) {
                CaptureGUI.textArea_log.append("ERROR:应用卸载失败...\n");
                e.printStackTrace();
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
        checkIsAppMarket();
    }

    /**
     * 检查应用卸载后，当前界面是否在应用商店下载界面
     * 如不是处理。
     * 有2种情况：
     * 2）当前界面是应用安装界面
     * 1）华为手机卸载应用后，会提示删除残留文件
     */
    public void checkIsAppMarket() {
        for (int i=0;i <4;i++){
            try {
                Thread.sleep(5000);
                xmlStr = driver.getPageSource();
            } catch (Exception e) {
                CaptureGUI.textArea_log.append("ERROR:获取界面元素时出错！\n");
                e.printStackTrace();
            }

            if (xmlStr.contains("暂不删除") && xmlStr.contains("立即删除")) {
                try {
                    CaptureGUI.textArea_log.append("INFO:未进入应用下载界面...\n");
                    driver.findElement(By.xpath(XPathConstant.DELETE_BTN_XPATH)).click();
                    break;
                } catch (Exception e) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e.printStackTrace();
                }
            }
            if (xmlStr.contains("取消") && xmlStr.contains("安装")) {
                CaptureGUI.textArea_log.append("INFO:未进入应用下载界面...\n");
                try {
                    driver.findElement(By.xpath(XPathConstant.CANCEL_BTN_XPATH)).click();
                    break;
                } catch (Exception e) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e.printStackTrace();
                }
            }
        }


            /*else {
                //需要重新打开应用商店
            }*/

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
                    if (!line.contains("com.duokan.reader")) {
                        pkgList.add(line.substring(8, line.length()));
                    }
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
}

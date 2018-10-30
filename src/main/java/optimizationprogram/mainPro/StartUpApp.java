package optimizationprogram.mainPro;

import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.event.PlanThree;
import optimizationprogram.mainPro.welcomePage.*;
import io.appium.java_client.android.AndroidDriver;
import optimizationprogram.tools.GotoSleep;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StartUpApp extends Thread {

    private AndroidDriver driver;
    private String btnXp;
    private String appName;
    private String appType;
    private String appSize;
    private String appMarket;
    private List<String> pkgList;

    private HandleWelcomePage jtmp;
    private String outputPath;
    private GotoSleep sleep = new GotoSleep();


    public StartUpApp(AndroidDriver driver, SelectedAppInfo sai, GUIInfo gi) {
        this.driver = driver;
        this.btnXp = sai.getOpenXPath();
        this.appName = sai.getAppName();
        this.appType = sai.getAppType();
        this.appSize = sai.getAppSize();
        this.appMarket = gi.getAppMarket();
        this.outputPath = gi.getDataDestination();
    }

    @Override
    public void run() {
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return;
        }
        //启动应用 点击“打开按钮”
        if (StringUtils.isNotEmpty(btnXp)) {
            try {
                CaptureGUI.textArea_log.append("INFO:正在打开应用···\n");
                driver.findElementByXPath(btnXp).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (appType.contains("，")) {
            appType = appType.split("，")[0];
            System.out.println(appType);
        }
        if (appSize.contains(".")) {
            appSize = appSize.substring(0, appSize.indexOf("."));
        }
        //appMarket = "华为";
        sleep.startSleep(3 * 1000);
        switch (appMarket) {
            case "华为应用市场":
                jumpToMainPage_Huawei(appType);
                break;
            case "小米应用商店":
                jumpToMainPage_Xiaomi(appType);
                break;
            default:
                //ssssss
        }
        /**
         * 因为抓取数据包并打印出包的信息有时间延迟，为确保该app的数据包信息
         * 打印完成后终止线程，故设置等待3秒
         */
        sleep.startSleep(3 * 1000);
    }

    /**
     * 华为应用市场
     *
     * @param appType
     */
    public void jumpToMainPage_Huawei(String appType) {
        switch (appType) {
            /*
                游戏
             */
            case "休闲益智":
            case "经营策略":
            case "体育竞技":
            case "棋牌桌游":
            case "动作射击":
            case "角色扮演":
                if (200 > Integer.valueOf(appSize)) {
                    sleep.startSleep(240 * 1000);
                } else {
                    sleep.startSleep(360 * 1000);
                }
                break;
            default:
                jtmp = new HandleWelcomePage();
                //yyst_main = new YystAppJumpToMainPage();
                if (jtmp.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    sleep.startSleep(5 * 1000);
                    saveScreenShotFile();
                    sleep.startSleep(5 * 1000);
                    saveScreenXmlFile();
                    new PlanThree(appType, btnXp).handle(driver);
                    //new PlanTwo(appType,btnXp).handle(driver);
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    sleep.startSleep(60 * 1000);
                }
        }
    }

    /**
     * 小米应用商店
     *
     * @param appType
     */
    public void jumpToMainPage_Xiaomi(String appType) {

        switch (appType) {
            case "游戏":
                if (200 > Integer.valueOf(appSize)) {
                    sleep.startSleep(240 * 1000);
                } else {
                    sleep.startSleep(360 * 1000);
                }
                break;
            default:
                jtmp = new HandleWelcomePage();
                //yyst_main = new YystAppJumpToMainPage();
                if (jtmp.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    sleep.startSleep(5 * 1000);
                    saveScreenShotFile();
                    sleep.startSleep(5 * 1000);
                    saveScreenXmlFile();
                    //new PlanFour(appType, btnXp).handle(driver);
                    new PlanThree(appType, btnXp).handle(driver);
                    sleep.startSleep(30 * 1000);
                    //new PlanTwo(appType, btnXp).handle(driver);
                    //new HandleElement(appType,btnXp).handle(driver);
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    sleep.startSleep(30 * 1000);
                }
        }
    }

    public void saveScreenShotFile() {
        File file = new File(outputPath
                + "\\主界面截图");
        if (!file.exists()) {
            file.mkdirs();
        }
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(outputPath + "\\主界面截图\\"
                    + appName + new SimpleDateFormat("yyyyMMddHH")
                    .format(new Date()) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveScreenXmlFile(){
        File file = new File(outputPath
                + "\\主界面XML");
        if (!file.exists()) {
            file.mkdirs();
        }
        String xmlStr = driver.getPageSource();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath
                    + "\\主界面XML\\" + appName + new SimpleDateFormat("yyyyMMddHH")
                    .format(new Date()) + ".xml", false), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            writer.write(xmlStr);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

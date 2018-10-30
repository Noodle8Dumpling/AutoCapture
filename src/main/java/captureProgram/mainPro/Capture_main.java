package captureProgram.mainPro;

import captureProgram.GUICode.CaptureGUI;
import captureProgram.mainPro.services.ParaConfigService;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class Capture_main {

    private GUIInfo GUIEntity;
    private AppMarketInfo appEntity;
    private List<String> appConfList;
    private String tmpStr = "";
    private String cmdStr = "";
    private AndroidDriver driver;
    private List<String> list;
    private SelectAndDownloadAtXiaoMiStore sada_xiaomi;
    private SelectAndDownloadAtHuaWeiMarket sada_huawei;
    private SelectedAppInfo sai;
    private CapturePkgs cap;
    private JFrame frame;
    private JLabel label;
    private FileWriter writer;
    private File file;


    // private CapturePkg cap;


    public Capture_main(String deviceName, String appMarket, String networkName, String dataSource, String dataDestination) {

        /*
         *获取系统变量中的Appium_HOME
         */
        Map map = System.getenv();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if ("APPIUM_HOME".equals(entry.getKey().toString())) {
                cmdStr = entry.getValue().toString() + "\\node_modules\\appium\\bin\\appium.js";
            }
        }
        GUIEntity = new GUIInfo();
        GUIEntity.setDeviceName(deviceName);
        GUIEntity.setAppMarket(appMarket);
        GUIEntity.setNetworkName(networkName);
        GUIEntity.setDataSource(dataSource);
        GUIEntity.setDataDestination(dataDestination);
    }

    /**
     * 读取要下载的App名称。
     * <p>
     * 注意：数据来源配置文档为.txt文档，文档编码格式:UTF-8无BOM格式，
     * 不选择无BOM格式，读到的第一行会多一个“?”，java自身原因。
     *
     * @param filePath
     * @return
     */
    public void readAppName(String filePath) {
        BufferedReader br;
        String rec;
        list = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(filePath)), "UTF-8"));
            while ((rec = br.readLine()) != null) {// 使用readLine方法，一次读一行
                if (StringUtils.isNotEmpty(rec.trim())) {
                    list.add(rec);
                }
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            CaptureGUI.textArea_log.append("ERROR:转码出错。\n");
            e.printStackTrace();
            return;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            CaptureGUI.textArea_log.append("ERROR:未找到配置文件，请检查app配置文档路径是否正确！\n");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 配置连接手机的属性
     *
     * @return
     */
    public DesiredCapabilities getDesiredCapabilities() {
        tmpStr = GUIEntity.getDeviceName().trim();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //连接的设备名称
        capabilities.setCapability("deviceName", tmpStr);
        capabilities.setCapability("udid", tmpStr);
        //默认
        capabilities.setCapability("automationName", ParaConfigService.AUTOMATIONNAME);
        //手机系统类型 Android/iOS
        capabilities.setCapability("platformName", ParaConfigService.PLATFORMNAME);
        //手机系统版本
        capabilities.setCapability("platformVersion", ParaConfigService.PLATFORMVERSION);
        //等待下一个命令时限，单位:s。若下一个命令超过设定的时长，AppiumClient关闭
        capabilities.setCapability("newCommandTimeout", ParaConfigService.NEWCOMMANDTIMEOUT);
        //要测试的应用包名，需要用adb命令查看
        capabilities.setCapability("appPackage", appEntity.getPkgName());
        //要测试的应用入口，通过调起活动来启动应用，需要用命令查看启动活动
        capabilities.setCapability("appActivity", appEntity.getActivityName());
        //有的应用启动由两个活动构成：入口活动和完成活动，只用一个入口活动会报错
        if (appEntity.getWaitActivityName() != null) {
            capabilities.setCapability("appWaitActivity", appEntity.getWaitActivityName());
        }
        //开启web视图测试环境，针对混合App（有原生也有web）
        capabilities.setCapability("autoWebview", "true");
        //开启Unicode输入，由Appium安装到手机，会导致手机内部的输入法不可用
        capabilities.setCapability("unicodeKeyboard", "true");
        //重置键入状态
        capabilities.setCapability("resetKeyboard", "true");

        return capabilities;
    }

    /**
     * 关启AppiumServer
     *
     * @return driver
     */
    public AndroidDriver startAndStopAppium() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM node.exe");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 处理外部命令执行的结果，释放当前线程，不会阻塞线程
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        CommandLine commandLine = CommandLine.parse("cmd.exe /c node " + cmdStr);

        // 创建监控时间60s,超过60s则中断执行
        ExecuteWatchdog dog = new ExecuteWatchdog(60 * 1000);
        Executor executor = new DefaultExecutor();

        // 设置命令执行退出值为1，如果命令成功执行并且没有错误，则返回1
        executor.setExitValue(1);
        executor.setWatchdog(dog);
        try {
            CaptureGUI.textArea_log.append("INFO:Appium启动中···\n");
            executor.execute(commandLine, resultHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resultHandler.waitFor(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CaptureGUI.textArea_log.append("INFO:Appium准备就绪···\n");
        AndroidDriver driver = null;
        if (CaptureGUI.stopFlag) {
            //连接Appium
            try {
                CaptureGUI.textArea_log.append("INFO:开始连接手机···\n");
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), getDesiredCapabilities());
                CaptureGUI.textArea_log.append("INFO:手机连接成功，应用市场已启动。\n");
            } catch (MalformedURLException e) {
                CaptureGUI.textArea_log.append("ERROR:Appium连接手机应用时出现错误，请检查参数配置是否正确。\n");
                if (CaptureGUI.stopFlag) {
                    label = new JLabel("连接手机时出错！请您检查参数配置后，重新开始操作。");
                    label.setFont(new Font("微软雅黑", 0, 13));
                    JOptionPane.showMessageDialog(frame, label, "Error", JOptionPane.ERROR_MESSAGE);
                }
                e.printStackTrace();
                return null;
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                driver.findElement(By.id(appEntity.getSearchBox())).click();
            } catch (Exception e) {
                e.printStackTrace();
                CaptureGUI.textArea_log.append("ERROR:未找到搜索框对应的元素ID，请检查。\n");
                return null;
            }
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return driver;
        } else {
            return driver;
        }
    }

    public void checkPara() {
        if (appConfList.size() > 0) {
            for (int i = 0; i < appConfList.size(); i++) {
                tmpStr = appConfList.get(i);
                if (StringUtils.isEmpty(tmpStr) || "".equals(tmpStr)) {
                    //记录日志并提示
                    label = new JLabel("请检查应用市场（商店）的参数是否配置正确！");
                    label.setFont(new Font("微软雅黑", 0, 13));
                    JOptionPane.showMessageDialog(frame, label, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //System.out.println(appConfList.get(i));
            }
            appEntity = new AppMarketInfo();

            if (appConfList.size() == 4) {
                appEntity.setPkgName(appConfList.get(0));
                appEntity.setActivityName(appConfList.get(1));
                appEntity.setSearchBox(appConfList.get(2));
                appEntity.setInputBox(appConfList.get(3));
            }
            if (appConfList.size() == 5) {
                appEntity.setPkgName(appConfList.get(0));
                appEntity.setActivityName(appConfList.get(1));
                appEntity.setWaitActivityName(appConfList.get(2));
                appEntity.setSearchBox(appConfList.get(3));
                appEntity.setInputBox(appConfList.get(4));
            }

        } else {
            //记录日志并提示
            label = new JLabel("请检查应用市场（商店）的参数是否配置正确！");
            label.setFont(new Font("微软雅黑", 0, 13));
            JOptionPane.showMessageDialog(frame, label, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if ("".equals(cmdStr)) {
            CaptureGUI.textArea_log.append("ERROR:检测不到Appium的安装文件，请配置Appium的系统变量。\n");
            label = new JLabel("请检查Appium系统变量是否配置正确!");
            label.setFont(new Font("微软雅黑", 0, 13));
            JOptionPane.showMessageDialog(frame, label, "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        driver = startAndStopAppium();
        if (driver == null) {
                /*if (CaptureGUI.stopFlag) {
                    label = new JLabel("请检查应用市场（商店）的参数是否配置正确！");
                    label.setFont(new Font("微软雅黑", 0, 13));
                    JOptionPane.showMessageDialog(frame, label, "Error", JOptionPane.ERROR_MESSAGE);
                }*/
            //driver.quit();
            return;
        }
        CaptureGUI.textArea_log.append("INFO:开始读取App列表···\n");
        readAppName(GUIEntity.getDataSource());
    }

    public void StartUpCapture() {
        String appMarket = GUIEntity.getAppMarket();
        if ("华为应用市场".equals(appMarket)) {
            ConfigHuaWeiMarket confHW = new ConfigHuaWeiMarket();
            appConfList = confHW.defAppPara();
        }
        if ("小米应用商店".equals(appMarket)) {
            ConfigXiaoMiStore confXM = new ConfigXiaoMiStore();
            appConfList = confXM.defAppPara();
        }
        checkPara();
        String keyword;
        if (list != null) {
            for (int i = CaptureGUI.capturedTotalCount; i < list.size(); i++) {
                if (!CaptureGUI.stopFlag) {
                    break;
                }
                //定量关启Appium
                if ((i + 1) % 15 == 0) {
                    driver.quit();
                    CaptureGUI.textArea_log.append("INFO:Appium重启中···\n");
                    try {
                        Runtime.getRuntime().exec("taskkill /F /IM adb.exe");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    driver = startAndStopAppium();
                }
                keyword = list.get(i);
                try {
                    CaptureGUI.textArea_log.append("INFO:开始搜索···\n");
                    Thread.sleep(3 * 1000);
                    driver.findElementById(appEntity.getInputBox()).clear();//清空
                    driver.findElementById(appEntity.getInputBox()).sendKeys(keyword);//键入
                    driver.pressKeyCode(AndroidKeyCode.ENTER);//提交搜索内容
                } catch (Exception e) {
                    e.printStackTrace();
                    CaptureGUI.textArea_log.append("ERROR:未找到搜索框对应的元素ID，请检查。\n");
                    label = new JLabel("请检查应用市场（商店）的参数是否配置正确！");
                    label.setFont(new Font("微软雅黑", 0, 13));
                    JOptionPane.showMessageDialog(frame, label, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //加载搜索结果界面
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!CaptureGUI.stopFlag) {
                    driver.quit();
                    break;
                }

                CaptureGUI.textArea_log.append("INFO:搜索完成···\n");
                if ("小米应用商店".equals(appMarket)) {
                    sada_xiaomi = new SelectAndDownloadAtXiaoMiStore();
                    sai = sada_xiaomi.selectAndDownload(driver, keyword);
                }
                if ("华为应用市场".equals(appMarket)) {
                    sada_huawei = new SelectAndDownloadAtHuaWeiMarket();
                    sai = sada_huawei.selectAndDownload(driver, keyword);
                }

                if (sai != null) {
                    if (StringUtils.isEmpty(sai.getAppName())) {
                        sai.setAppName("");
                    }
                    if (StringUtils.isEmpty(sai.getAppType())) {
                        sai.setAppType("");
                    }
                    if (StringUtils.isEmpty(sai.getAppSize())) {
                        sai.setAppSize("");
                    }
                    if (StringUtils.isEmpty(sai.getFlag())) {
                        sai.setFlag("");
                    }
                    if (StringUtils.isEmpty(sai.getFailInfo())) {
                        sai.setFailInfo("");
                    }
                    tmpStr = "安装成功：" + sai.getFlag() + "\n"
                            + "名称：" + sai.getAppName() + "\n"
                            + "分类：" + sai.getAppType() + "\n"
                            + "大小：" + sai.getAppSize() + "\n"
                            + "安装失败信息：" + sai.getFailInfo() + "\n"
                            + "来源应用：" + appMarket;
                    try {
                        file = new File(GUIEntity.getDataDestination()
                                + "\\app信息");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        writer = new FileWriter(GUIEntity.getDataDestination()
                                + "\\app信息\\" + keyword + ".txt");
                        writer.write(tmpStr);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        CaptureGUI.textArea_log.append("ERROR:App信息输出到文件时出现异常。\n");
                        e.printStackTrace();
                    }
                    if (!CaptureGUI.stopFlag) {
                        driver.quit();
                        return;
                    }
                    //输出日志信息
                    tmpStr = sai.getOpenXPath();
                    if (StringUtils.isNotEmpty(tmpStr) && (!"".equals(tmpStr))) {
                        cap = new CapturePkgs(driver, sai, GUIEntity);
                        cap.capture();
                        CaptureGUI.capturedAppNum = CaptureGUI.capturedAppNum + 1;
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if ("华为应用市场".equals(appMarket)) {
                            driver.pressKeyCode(AndroidKeyCode.BACK);
                        }
                        //CaptureGUI.textArea_log.append("INFO:\n");
                    } else {
                        CaptureGUI.failAppNum = CaptureGUI.failAppNum + 1;
                    }
                }
            }
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            CaptureGUI.textArea_log.append("INFO:抓包结束。\n");
            driver.quit();
        }

    }


}

package optimizationprogram.mainPro;

import io.appium.java_client.android.AndroidDriver;
import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.services.SelectAndDownloadService;
import optimizationprogram.mainPro.services.XPathConstant;
import optimizationprogram.tools.GotoSleep;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.List;

/**
 * date:2018-08-15
 * author:hj
 * description:选取符合条件的app
 */
public class SelectAndDownloadAtXiaoMiStore implements SelectAndDownloadService {
    private Document document;
    private String xmlStr;
    private List<Node> list;
    private List<Node> list1;
    private List<Node> list2;
    private GotoSleep sleep = new GotoSleep();
    private SelectedAppInfo sai = new SelectedAppInfo();
    private Element node;
    private Element node1;
    private Element node2;
    private Element node3;
    private Element node4;

    private List<Attribute> attr;
    private List<Attribute> attr1;
    private List<Attribute> attr2;
    private List<Attribute> attr3;
    private List<Attribute> attr4;

    private String xp;
    private String xp1;
    private String xp2;
    private String xp3;
    private String btnXp;

    private String appName;
    private String appType;
    private String appSize;


    /**
     * “用户搜索后选择了它”情况
     *
     * @param driver
     * @param document
     * @param list
     * @param keyword
     */
    public void selectApp2(AndroidDriver driver, Document document, List<Node> list, String keyword) {
        if (list.size() == 1) {
            node = (Element) list.get(0);
            xp1 = node.getUniquePath();
            xp2 = xp1 + "/android.view.View[3]";
            node1 = (Element) document.selectSingleNode(xp2 + "/android.view.View[2]");
            attr = node1.attributes();
            for (Attribute e : attr) {
                if ("content-desc".equals(e.getName())) {
                    appName = e.getValue();
                    if (appName.contains(keyword) || keyword.contains(appName)) {
                        //获取app类别
                        node2 = (Element) document.selectSingleNode(xp2 + "/android.view.View[3]" + "/android.view.View[2]");
                        attr2 = node2.attributes();
                        for (Attribute e2 : attr2) {
                            if ("content-desc".equals(e2.getName())) {
                                appType = e2.getValue();
                                break;
                            }
                        }
                        //获取app大小
                        node3 = (Element) document.selectSingleNode(xp2 + "/android.view.View[3]" + "/android.view.View[1]");
                        attr3 = node3.attributes();
                        for (Attribute e3 : attr3) {
                            if ("content-desc".equals(e3.getName())) {
                                appSize = e3.getValue();
                                break;
                            }
                        }
                        //获取安装按钮路径
                        btnXp = xp1 + "/android.view.View[4]"
                                /*+ "/android.view.View[1]"
                                + "/android.view.View[2]"*/;
                        //含有按钮文字的元素
                        xp3 = xp1 + "/android.view.View[4]"
                                + "/android.view.View[1]"
                                + "/android.view.View[2]";
                        CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                        sai.setAppName(appName);
                        sai.setAppType(appType);
                        sai.setAppSize(appSize);
                        if (afterSelect(driver)) {
                            CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                            sai.setOpenXPath(btnXp);
                            sai.setFlag("是");
                            sai.setFailInfo("");
                        } else {
                            CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                            sai.setFlag("否");
                            sai.setFailInfo("下载安装失败");
                        }
                    }
                }
            }
        } else {
            OUT:
            for (int i = 0; i < list.size(); i++) {
                if (i == 7) {
                    break OUT;
                }
                node = (Element) list.get(i);
                xp1 = node.getUniquePath() + "/android.view.View[1]";
                if (i == 0) {
                    xp2 = xp1 + "/android.view.View[1]"
                            + "/android.view.View[1]"
                            + "/android.view.View[3]";
                } else {
                    xp2 = xp1 + "/android.view.View[1]"
                            + "/android.view.View[1]"
                            + "/android.view.View[2]";
                }

                node1 = (Element) document.selectSingleNode(xp2 + "/android.view.View[1]");
                if (node1 == null){
                    continue ;
                }
                attr = node1.attributes();
                for (Attribute e : attr) {
                    if ("content-desc".equals(e.getName())) {
                        appName = e.getValue();
                        if (appName.contains(keyword) || keyword.contains(appName)) {
                            //获取app类别
                            node2 = (Element) document.selectSingleNode(xp2 + "/android.view.View[2]");
                            attr2 = node2.attributes();
                            for (Attribute e2 : attr2) {
                                if ("content-desc".equals(e2.getName())) {
                                    appType = e2.getValue();
                                    break;
                                }
                            }
                            //获取app大小
                            node3 = (Element) document.selectSingleNode(xp2 + "/android.view.View[3]");
                            attr3 = node3.attributes();
                            for (Attribute e3 : attr3) {
                                if ("content-desc".equals(e3.getName())) {
                                    appSize = e3.getValue();
                                    break;
                                }
                            }
                            //获取安装按钮路径
                            btnXp = xp1 + "/android.view.View[2]"
                                    /*+ "/android.view.View[1]"
                                    + "/android.view.View[2]"*/;
                            //含有按钮文字的元素
                            xp3 = xp1 + "/android.view.View[2]"
                                    + "/android.view.View[1]"
                                    + "/android.view.View[2]";
                            CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                            sai.setAppName(appName);
                            sai.setAppType(appType);
                            sai.setAppSize(appSize);
                            if (afterSelect(driver)) {
                                CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                sai.setOpenXPath(btnXp);
                                sai.setFlag("是");
                                sai.setFailInfo("");
                            } else {
                                CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                sai.setFlag("否");
                                sai.setFailInfo("下载安装失败");
                            }
                            break OUT;
                        }
                    }
                }

            }
        }
    }


    /**
     * 非“用户搜索后选择了它”情况
     *
     * @param driver
     * @param document
     * @param list
     * @param keyword
     */
    public void selectApp1(AndroidDriver driver, Document document, List<Node> list, String keyword) {
        OUT:
        for (int i = 0; i < list.size(); i++) {
            if (i == 7) {
                break OUT;
            }
            node = (Element) list.get(i);
            xp1 = node.getUniquePath() + "/android.view.View[1]";
            xp2 = xp1 + "/android.view.View[1]"
                    + "/android.view.View[1]"
                    + "/android.view.View[2]";

            node1 = (Element) document.selectSingleNode(xp2 + "/android.view.View[1]");
            if (node1 == null){
                continue ;
            }
            attr = node1.attributes();
            for (Attribute e : attr) {
                if ("content-desc".equals(e.getName())) {
                    appName = e.getValue();
                    if (appName.contains(keyword) || keyword.contains(appName)) {
                        //获取app类别
                        node2 = (Element) document.selectSingleNode(xp2 + "/android.view.View[2]");
                        attr2 = node2.attributes();
                        for (Attribute e2 : attr2) {
                            if ("content-desc".equals(e2.getName())) {
                                appType = e2.getValue();
                                break;
                            }
                        }
                        //获取app大小
                        node3 = (Element) document.selectSingleNode(xp2 + "/android.view.View[3]");
                        attr3 = node3.attributes();
                        for (Attribute e3 : attr3) {
                            if ("content-desc".equals(e3.getName())) {
                                appSize = e3.getValue();
                                break;
                            }
                        }
                        //获取安装按钮路径
                        btnXp = xp1 + "/android.view.View[2]"
                                /*+ "/android.view.View[1]"
                                + "/android.view.View[2]"*/;
                        //含有按钮文字的元素
                        xp3 = xp1 + "/android.view.View[2]"
                                + "/android.view.View[1]"
                                + "/android.view.View[2]";
                        CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                        sai.setAppName(appName);
                        sai.setAppType(appType);
                        sai.setAppSize(appSize);
                        if (afterSelect(driver)) {
                            CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                            sai.setOpenXPath(btnXp);
                            sai.setFlag("是");
                            sai.setFailInfo("");
                        } else {
                            CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                            sai.setFlag("否");
                            sai.setFailInfo("下载安装失败");
                        }
                        break OUT;
                    }
                }
            }

        }
    }

    @Override
    public Boolean beforeSelect(AndroidDriver driver, int count) {
        for (int i = 0; i < 5; i++) {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return false;
            }
            try {
                xmlStr = driver.getPageSource();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (xmlStr.contains(XPathConstant.NET_REQ_FAIL_WORDS)) {
                driver.tap(1, driver.findElement(By.xpath(XPathConstant.RETRY_BTN_XPATH)), 500);
                sleep.startSleep(20 * 1000);
            } else {
                return true;
            }
        }
        sai.setFlag("否");
        sai.setFailInfo("网络连接出错，搜索失败");
        return false;
    }

    @Override
    public void startSelect(AndroidDriver driver, String keyword) {
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return;
        }
        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (xmlStr.contains("没有找到相关的应用")) {
            sai.setFlag("否");
            sai.setFailInfo("应用商店未搜到任何结果");
            CaptureGUI.textArea_log.append("INFO:应用商店未找到" + keyword + "，开始搜索下一个App···\n");
        } else if (xmlStr.contains("用户搜索后选择了它")) {
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            list = document.selectNodes(XPathConstant.XPATH_ListView);
            //球球大作战
            if (list.size() == 2) {
                list2 = document.selectNodes(XPathConstant.XPATH_ListView + "[1]"
                        + "/android.view.View");
            }
            if (list.size() == 3) {
                list1 = document.selectNodes(XPathConstant.ADVERTISEMENT_PATH);
                if (list1.size() == 0) {
                    //大众点评
                    list2 = document.selectNodes(XPathConstant.XPATH_ListView + "[1]"
                            + "/android.view.View");
                } else {
                    //大智慧、支付宝、荔枝tv
                    list2 = document.selectNodes(XPathConstant.XPATH_ListView + "[2]"
                            + "/android.view.View");
                }
            }
            if (list2.size() > 0) {
                selectApp2(driver, document, list2, keyword);
            }
        } else {
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            list = document.selectNodes(XPathConstant.XPATH_ListView);
            if (list.size() == 2) {
                list1 = document.selectNodes(XPathConstant.XPATH_ListView + "[1]"
                        + "/android.view.View");
            }
            if (list.size() == 3) {
                list1 = document.selectNodes(XPathConstant.XPATH_ListView + "[2]"
                        + "/android.view.View");
            }
            if (list1.size() > 0) {
                selectApp1(driver, document, list1, keyword);
            }
        }
    }

    @Override
    public Boolean afterSelect(AndroidDriver driver) {
        for (int i = 0; i < 300; i++) {
            try {
                xmlStr = driver.getPageSource();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                e.printStackTrace();
                return false;
            }
            node = (Element) document.selectSingleNode(xp3);
            attr = node.attributes();
            if (node != null) {
                for (Attribute e : attr) {
                    if ("content-desc".equals(e.getName())) {
                        if (!"打开".equals(e.getValue())) {
                            if ("安装中".equals(e.getValue())) {
                                CaptureGUI.textArea_log.append("INFO:安装中···" + "\n");
                                sleep.startSleep(15 * 1000);
                            } else if ("安装".equals(e.getValue())
                                    || "继续".equals(e.getValue())
                                    || "连接中".equals(e.getValue())
                                    || "升级".equals(e.getValue())) {
                                driver.tap(1, driver.findElement(By.xpath(btnXp)), 500);
                                sleep.startSleep(6 * 1000);
                                //判断是否有弹窗
                                try {
                                    xmlStr = driver.getPageSource();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    return false;
                                }
                                if (xmlStr.contains("带有扩展包")) {
                                    try {
                                        driver.findElement(By.xpath(XPathConstant.BEFOREINSTALLDIALOGBOX_PATH)).click();

                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                    }
                                }
                                CaptureGUI.textArea_log.append("INFO:正在下载···" + "\n");
                                sleep.startSleep(20 * 1000);
                            } else {
                                CaptureGUI.textArea_log.append("INFO:正在下载···" + "\n");
                                sleep.startSleep(20 * 1000);
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public SelectedAppInfo selectAndDownload(AndroidDriver driver, String keyword) {
        sai.setFlag("否");
        sai.setFailInfo("未找到符合条件的应用");
        //检查网络状况
        CaptureGUI.textArea_log.append("INFO:检查网络连接中···\n");
        if (beforeSelect(driver, 0)) {
            CaptureGUI.textArea_log.append("INFO:网络连接正常\n");
            startSelect(driver, keyword);
        }
        return sai;
    }

}

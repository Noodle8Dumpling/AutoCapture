package captureProgram.mainPro;


import captureProgram.GUICode.CaptureGUI;
import captureProgram.mainPro.services.SelectAndDownloadService;
import captureProgram.mainPro.services.XPathConstant;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.List;

/**
 * 创建人：郝京
 * <p>
 * 创建日期：2018-03-16
 * <p>
 * 文件描述：选取符合搜索条件的App
 */
public class SelectAndDownloadAtXiaoMiStore implements SelectAndDownloadService {
    Document document;
    String xmlStr;
    String xp1;
    String xp2;
    String xp3;
    String xp4;
    String xp5;
    String xp6;
    List<Node> list1;
    List<Node> list2;
    Element node;
    Element node1;
    Element node2;
    Element node3;
    Element node4;

    List<Attribute> attr;
    List<Attribute> attr1;
    List<Attribute> attr2;
    List<Attribute> attr3;
    List<Attribute> attr4;

    String appName;
    String appType;
    String appSize;
    String btnXPath;
    String failInfo = "";
    Boolean flag = false;
    SelectedAppInfo sai = new SelectedAppInfo();

    Integer count1 = 0;

    public void selectApp_Step4(String xp3, AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp3);
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return;
        }
        if (list1.size() > 7) {
            OUT:
            for (int i = 1; i < 4; i++) {
                node1 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                        XPathConstant.DETAIL_View + "/android.view.View[1]");
                if (node1 != null) {
                    attr1 = node1.attributes();
                    for (Attribute e : attr1) {
                        if ("content-desc".equals(e.getName())) {
                            appName = e.getValue();
                            if (appName.contains(keyword) || keyword.contains(appName)) {

                                node2 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[2]");

                                attr2 = node2.attributes();
                                for (Attribute e2 : attr2) {
                                    if ("content-desc".equals(e2.getName())) {
                                        appType = e2.getValue();
                                    }
                                }
                                node3 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[3]");

                                attr3 = node3.attributes();
                                for (Attribute e3 : attr3) {
                                    if ("content-desc".equals(e3.getName())) {
                                        appSize = e3.getValue();
                                    }
                                }
                                node4 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View +
                                        "/android.view.View[4]");

                                attr4 = node4.attributes();
                                for (Attribute e4 : attr4) {
                                    if ("content-desc".equals(e4.getName())) {
                                        appSize = appSize + e4.getValue();
                                    }
                                }
                                xp4 = xp3 + "[" + (i + 1) + "]" + XPathConstant.BTN_Button;
                                xp6 = xp3 + "[" + (i + 1) + "]" + XPathConstant.BTN_View;
                                CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                                if (!CaptureGUI.stopFlag) {
                                    driver.quit();
                                    return;
                                }
                                if (afterSelect(driver)) {
                                    CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                    sai.setAppName(appName);
                                    sai.setAppType(appType);
                                    sai.setAppSize(appSize);
                                    sai.setOpenXPath(btnXPath);
                                    sai.setFlag("是");
                                } else {
                                    CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                    sai.setAppName(appName);
                                    sai.setAppType(appType);
                                    sai.setAppSize(appSize);
                                    sai.setFlag("否");
                                    sai.setFailInfo("下载安装失败");
                                }
                                break OUT;
                            } else {
                                if (!CaptureGUI.stopFlag) {
                                    driver.quit();
                                    return;
                                }
                                failInfo = failInfo + appName + ",";

                                if (i == 3) {
                                    sai.setFlag("否");
                                    sai.setFailInfo("应用商店未找到符合条件的应用，找到与搜索词相似的应用："
                                            + failInfo.substring(0, failInfo.length() - 1));
                                    CaptureGUI.textArea_log.append("INFO:应用商店未找到" + keyword + "，开始搜索下一个App···\n");
                                }
                                appName = null;
                                btnXPath = null;
                            }
                        }
                    }
                }
            }
        }
    }

    public void selectApp_Step3(String xp2, String xp3, AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp2);
        if (list1.size() == 4) {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            xp5 = xp2 + "[3]" + "/android.view.View[1]";
            node1 = (Element) document.selectSingleNode(xp5);
            if (node1 != null) {
                attr1 = node1.attributes();
                for (Attribute e : attr1) {
                    if ("content-desc".equals(e.getName())) {
                        appName = e.getValue();
                        if (appName.contains(keyword) || keyword.contains(appName)) {
                            node2 = (Element) document.selectSingleNode(xp2 + "[3]" + "/android.view.View[2]");
                            attr2 = node2.attributes();
                            for (Attribute e2 : attr2) {
                                if ("content-desc".equals(e2.getName())) {
                                    appType = e2.getValue();
                                }
                            }
                            node3 = (Element) document.selectSingleNode(xp2 + "[3]" + "/android.view.View[3]");

                            attr3 = node3.attributes();
                            for (Attribute e3 : attr3) {
                                if ("content-desc".equals(e3.getName())) {
                                    appSize = e3.getValue();
                                }
                            }
                            node4 = (Element) document.selectSingleNode(xp2 + "[3]" + "/android.view.View[4]");

                            attr4 = node4.attributes();
                            for (Attribute e4 : attr4) {
                                if ("content-desc".equals(e4.getName())) {
                                    appSize = appSize + e4.getValue();
                                }
                            }
                            xp4 = xp3 + "[1]" + XPathConstant.BTN_Button;
                            xp6 = xp3 + "[1]" + XPathConstant.BTN_View;
                            CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                            if (!CaptureGUI.stopFlag) {
                                driver.quit();
                                return;
                            }
                            if (afterSelect(driver)) {
                                CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                sai.setAppName(appName);
                                sai.setAppType(appType);
                                sai.setAppSize(appSize);
                                sai.setOpenXPath(btnXPath);
                                sai.setFlag("是");
                            } else {
                                CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                sai.setAppName(appName);
                                sai.setAppType(appType);
                                sai.setAppSize(appSize);
                                sai.setFlag("否");
                                sai.setFailInfo("下载安装失败");
                            }
                            break;
                        } else {
                            if (!CaptureGUI.stopFlag) {
                                driver.quit();
                                return;
                            }
                            selectApp_Step4(xp3, driver, keyword);
                        }
                    }
                }
            }

        }
    }

    /**
     * 新的情况 搜索“PU”、“BBM”
     * list1.size<=7
     * else{}
     *
     * @param xp3
     * @param driver
     * @param keyword
     */
    public void selectApp_Step2(String xp3, AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp3);
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return;
        }
        if (list1.size() > 7) {
            OUT:
            for (int i = 0; i < 7; i++) {
                if (!CaptureGUI.stopFlag) {
                    driver.quit();
                    return;
                }
                node1 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                        XPathConstant.DETAIL_View + "/android.view.View[1]");
                if (node1 != null) {
                    attr1 = node1.attributes();
                    for (Attribute e : attr1) {
                        if ("content-desc".equals(e.getName())) {
                            appName = e.getValue();
                            if (appName.contains(keyword) || keyword.contains(appName)) {
                                node2 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[2]");
                                attr2 = node2.attributes();
                                for (Attribute e2 : attr2) {
                                    if ("content-desc".equals(e2.getName())) {
                                        appType = e2.getValue();
                                    }
                                }
                                node3 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[3]");

                                attr3 = node3.attributes();
                                for (Attribute e3 : attr3) {
                                    if ("content-desc".equals(e3.getName())) {
                                        appSize = e3.getValue();
                                    }
                                }
                                node4 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[4]");

                                attr4 = node4.attributes();
                                for (Attribute e4 : attr4) {
                                    if ("content-desc".equals(e4.getName())) {
                                        appSize = appSize + e4.getValue();
                                    }
                                }
                                xp4 = xp3 + "[" + (i + 1) + "]" + XPathConstant.BTN_Button;
                                xp6 = xp3 + "[" + (i + 1) + "]" + XPathConstant.BTN_View;
                                CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                                if (!CaptureGUI.stopFlag) {
                                    driver.quit();
                                    return;
                                }
                                if (afterSelect(driver)) {
                                    CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                    sai.setAppName(appName);
                                    sai.setAppType(appType);
                                    sai.setAppSize(appSize);
                                    sai.setOpenXPath(btnXPath);
                                    sai.setFlag("是");
                                } else {
                                    CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                    sai.setAppName(appName);
                                    sai.setAppType(appType);
                                    sai.setAppSize(appSize);
                                    sai.setFlag("否");
                                    sai.setFailInfo("下载安装失败");
                                }
                                break OUT;
                            } else {
                                if (!CaptureGUI.stopFlag) {
                                    driver.quit();
                                    return;
                                }
                                failInfo = failInfo + appName + "，";
                                if (i == 6) {
                                    sai.setFlag("否");
                                    sai.setFailInfo("应用商店未找到符合条件的应用，找到与搜索词相似的应用："
                                            + failInfo.substring(0, failInfo.length() - 1));
                                    CaptureGUI.textArea_log.append("INFO:应用商店未找到" + keyword + "，开始搜索下一个App···\n");
                                }
                                appName = null;
                                btnXPath = null;
                            }
                        }
                    }
                }
            }
        } else {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            OUT:
            for (int i = 0; i < list1.size(); i++) {
                if (!CaptureGUI.stopFlag) {
                    driver.quit();
                    return;
                }
                node1 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                        XPathConstant.DETAIL_View + "/android.view.View[1]");
                if (node1 != null) {
                    attr1 = node1.attributes();
                    for (Attribute e : attr1) {
                        if ("content-desc".equals(e.getName())) {
                            appName = e.getValue();
                            if (appName.contains(keyword) || keyword.contains(appName)) {
                                node2 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[2]");
                                attr2 = node2.attributes();
                                for (Attribute e2 : attr2) {
                                    if ("content-desc".equals(e2.getName())) {
                                        appType = e2.getValue();
                                    }
                                }
                                node3 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[3]");

                                attr3 = node3.attributes();
                                for (Attribute e3 : attr3) {
                                    if ("content-desc".equals(e3.getName())) {
                                        appSize = e3.getValue();
                                    }
                                }
                                node4 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        XPathConstant.DETAIL_View + "/android.view.View[4]");

                                attr4 = node4.attributes();
                                for (Attribute e4 : attr4) {
                                    if ("content-desc".equals(e4.getName())) {
                                        appSize = appSize + e4.getValue();
                                    }
                                }
                                xp4 = xp3 + "[" + (i + 1) + "]" + XPathConstant.BTN_Button;
                                xp6 = xp3 + "[" + (i + 1) + "]" + XPathConstant.BTN_View;
                                CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                                if (!CaptureGUI.stopFlag) {
                                    driver.quit();
                                    return;
                                }
                                if (afterSelect(driver)) {
                                    CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                    sai.setAppName(appName);
                                    sai.setAppType(appType);
                                    sai.setAppSize(appSize);
                                    sai.setOpenXPath(btnXPath);
                                    sai.setFlag("是");
                                } else {
                                    CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                    sai.setAppName(appName);
                                    sai.setAppType(appType);
                                    sai.setAppSize(appSize);
                                    sai.setFlag("否");
                                    sai.setFailInfo("下载安装失败");
                                }
                                break OUT;
                            } else {
                                if (!CaptureGUI.stopFlag) {
                                    driver.quit();
                                    return;
                                }
                                failInfo = failInfo + appName + "，";
                                if (i == list1.size() - 1) {
                                    sai.setFlag("否");
                                    sai.setFailInfo("应用商店未找到符合条件的应用，找到与搜索词相似的应用："
                                            + failInfo.substring(0, failInfo.length() - 1));
                                    CaptureGUI.textArea_log.append("INFO:应用商店未找到" + keyword + "，开始搜索下一个App···\n");
                                }
                                appName = null;
                                btnXPath = null;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 搜索应用时出现了新情况，新的xml结构，搜索“快手”。
     *
     * @param xp
     * @param driver
     * @param keyword
     */

    public void selectApp_Step1(String xp, AndroidDriver driver, String keyword) {
        System.out.println("================瓜子二手车===============");
        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp + "/android.view.View");
        System.out.println("-------------路径：" + xp + "/android.view.View");
        System.out.println("================list1。size===============" + list1.size());
        if (list1.size() == 5) {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            node1 = (Element) document.selectSingleNode(xp + "/android.view.View[4]/android.view.View[1]");
            if (node1 != null) {
                attr1 = node1.attributes();
                for (Attribute e : attr1) {
                    if ("content-desc".equals(e.getName())) {
                        appName = e.getValue();
                        if (appName.contains(keyword) || keyword.contains(appName)) {
                            node2 = (Element) document.selectSingleNode(xp + "/android.view.View[4]" +
                                    "/android.view.View[2]/android.view.View[1]/android.view.View[4]");

                            attr2 = node2.attributes();
                            for (Attribute e2 : attr2) {
                                if ("content-desc".equals(e2.getName())) {
                                    appType = e2.getValue();
                                }
                            }
                            node3 = (Element) document.selectSingleNode(xp + "/android.view.View[4]" +
                                    "/android.view.View[2]/android.view.View[1]/android.view.View[1]");

                            attr3 = node3.attributes();
                            for (Attribute e3 : attr3) {
                                if ("content-desc".equals(e3.getName())) {
                                    appSize = e3.getValue();
                                }
                            }

                            flag = true;
                            xp4 = xp + "/android.view.View[5]/android.widget.Button[1]";
                            xp6 = xp + "/android.view.View[5]/android.view.View[1]/android.view.View[2]";
                            CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "\n");
                            if (!CaptureGUI.stopFlag) {
                                driver.quit();
                                return;
                            }
                            if (afterSelect(driver)) {
                                CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                sai.setAppName(appName);
                                sai.setAppType(appType);
                                sai.setAppSize(appSize);
                                sai.setOpenXPath(btnXPath);
                                sai.setFlag("是");
                            } else {
                                CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                sai.setAppName(appName);
                                sai.setAppType(appType);
                                sai.setAppSize(appSize);
                                sai.setFlag("否");
                                sai.setFailInfo("下载安装失败");
                            }
                        } else {
                            if (!CaptureGUI.stopFlag) {
                                driver.quit();
                                return;
                            }
                            failInfo = appName;
                            sai.setFlag("否");
                            sai.setFailInfo("应用商店未找到符合条件的应用，找到与搜索词相似的应用：" + failInfo);
                            CaptureGUI.textArea_log.append("INFO:应用商店未找到" + keyword + "，开始搜索下一个App···\n");
                            flag = false;
                            appName = null;
                            btnXPath = null;
                        }
                    }
                }
            }
        } else {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            flag = false;
        }
    }

    @Override
    public Boolean beforeSelect(AndroidDriver driver, int count) {
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return false;
        }
        boolean flag = false;
        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //查找网络请求失败的画面节点
        list1 = document.selectNodes(XPathConstant.NET_REQ_FAIL_XPATH);
        if (list1.size() == 1) {
            //查找当前界面是否有 重试按钮 的节点
            list2 = document.selectNodes(XPathConstant.RETRY_BTN_XPATH);
            if (list2.size() == 1) {
                driver.findElement(By.xpath(XPathConstant.RETRY_BTN_XPATH)).click();
            }
            /*
             * 点击“重试”按钮后刷新界面等待30s
             */
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count = count + 1;
            if (3 == count) {
                sai.setFlag("否");
                sai.setFailInfo("网络连接出错，搜索失败");
                flag = false;
            } else {
                beforeSelect(driver, count);
            }

        } else {
            flag = true;
        }
        return flag;
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
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        xp1 = XPathConstant.XPATH_ListView;
        list1 = document.selectNodes(xp1);
        if (list1.size() == 0) {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            xp1 = XPathConstant.NORESULT_View;
            list1 = document.selectNodes(xp1);
            if (list1.size() == 1) {
                sai.setFlag("否");
                sai.setFailInfo("应用商店未搜到任何结果");
                CaptureGUI.textArea_log.append("INFO:应用商店未找到" + keyword + "，开始搜索下一个App···\n");
            }
        }
        if (list1.size() == 3) {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            CaptureGUI.textArea_log.append("INFO:开始筛选App···\n");
            selectApp_Step1(xp1 + "[1]/android.view.View[1]", driver, keyword);
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            if (!flag) {
                xp2 = xp1 + "[2]";
                xp3 = xp1 + "[2]";
            }

        }
        if (list1.size() == 2) {
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            xp2 = xp1 + "[1]";
            xp3 = xp1 + "[1]";
        }
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return;
        }
        if (StringUtils.isNotEmpty(xp2)) {
            xp2 = xp2 + XPathConstant.XPATH_View;
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            list2 = document.selectNodes(xp2);
            if (!CaptureGUI.stopFlag) {
                driver.quit();
                return;
            }
            if (list2.size() == 2) {
                xp3 = xp3 + "/android.view.View";
                CaptureGUI.textArea_log.append("INFO:开始筛选App···\n");
                selectApp_Step2(xp3, driver, keyword);
            } else {
                xp3 = xp3 + "/android.view.View";
                CaptureGUI.textArea_log.append("INFO:开始筛选App···\n");
                selectApp_Step3(xp2, xp3, driver, keyword);
            }
        }
    }

    @Override
    public Boolean afterSelect(AndroidDriver driver) {
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return false;
        }
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
        node = (Element) document.selectSingleNode(xp6);
        if(node != null){
            attr = node.attributes();
            for (Attribute e : attr) {
                if ("content-desc".equals(e.getName())) {
                    if (!"打开".equals(e.getValue())) {
                        if ("安装中".equals(e.getValue())) {
                            count1 = count1 + 1;
                            if (count1 > 20) {
                                count1 = 0;
                                return false;
                            }

                            CaptureGUI.textArea_log.append("INFO:安装中···" + "\n");
                            try {
                                Thread.sleep(15 * 1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            afterSelect(driver);
                        } else if ("安装".equals(e.getValue()) || "继续".equals(e.getValue())
                                || "连接中".equals(e.getValue())) {
                            driver.findElementByXPath(xp4).click();
                            CaptureGUI.textArea_log.append("INFO:正在下载···" + "\n");
                            try {
                                Thread.sleep(20 * 1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            afterSelect(driver);
                        } else {
                            CaptureGUI.textArea_log.append("INFO:正在下载···" + "\n");
                            try {
                                Thread.sleep(20 * 1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            afterSelect(driver);
                        }
                    } else {
                        //启动按钮路径
                        btnXPath = xp4;
                        xp2 = null;
                        xp3 = null;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public SelectedAppInfo selectAndDownload(AndroidDriver driver, String keyword) {
        //检查网络状况
        CaptureGUI.textArea_log.append("INFO:检查网络连接中···\n");
        if (beforeSelect(driver, 0)) {
            CaptureGUI.textArea_log.append("INFO:网络连接正常\n");
            startSelect(driver, keyword);
        }
        return sai;
    }
}

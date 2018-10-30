package capture.main;

import capture.capt.CapturePkg;
import capture.constant.CONFIGConstant;
import capture.entity.appInfo;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 日 期： 20171220
 * <p>
 * 编写者： 郝 京
 * <p>
 * 文件名： Automation_V2.java
 * <p>
 * 功能描述：在手机应用商店搜索应用，选取符合条件的应用安装，
 * 安装成功后，启动应用一段时间，在这段时间内抓取
 * 访问网络产生的数据包并输出为文档，最后卸载应用，
 * 并开始下一个应用。
 * <p>
 * 修改日期： 20171226
 * <p>
 * 修改人： 郝京
 * <p>
 * 修改内容： 1.增加 node1!=null的判断，搜索应用后App列表中出现了 广告栏，广告栏和应用栏的xml结构不同。
 * 例如，搜索“应用宝”后，出现了广告栏，广告栏含有文字“"应用宝"相关的应用都在这里”
 * 2.增加 selectApp_Step5方法。
 * <p>
 * 修改日期： 20171227
 * <p>
 * 修改人： 郝京
 * <p>
 * 修改内容： 1.增加命令行启动、关闭AppiumServer的方法startAndStopAppium()，之前是人工启动关闭Appium；
 * 将main方法中的初始搜索代码放到了该方法中。之前版本初始搜索代码在main方法中。
 * 2.增加定量关启AppiumServer的代码。程序运行期间，每完成一个搜索App流程,Appium压力都会增加，不隔段时间关停
 * 会影响程序的运行，造成adb.exe停止中断或搜索过程变慢卡住的现象
 * <p>
 * 修改日期： 20180126
 * <p>
 * 修改人： 郝京
 * <p>
 * 修改内容：1.keyword和appname双向包含的关系 keyword包含appname或appname包含keyword
 *
 * 修改日期： 20180129
 *
 * 修改人： 郝京
 *
 * 修改内容：1.在selectApp_Step2()方法中增加 新情况的处理 list1.size()<=7
 * 例如，搜索“PU”、“BBM”等，搜索结果小于等于7个，并且可视界面内有 “去百度搜索”和“去豌豆荚搜索”栏
 * 2.在selectApp_Step1()方法中增加 “含应用商店属性的应用分发类应用暂不收录”情况的处理。
 */
public class Automation_V2 {
    private static PrintStream log_pr;
    private static String xmlStr;
    private static String xp1;
    private static String xp2;
    private static String xp3;
    private static String xp4;
    private static String xp5;
    private static String xp6;
    private static List<Node> list1;
    private static List<Node> list2;
    private static Document document;
    private static Element node1;
    private static Element node2;
    private static Element node3;
    private static Element node4;
    private static Element node;
    private static List<Attribute> attr1;
    private static List<Attribute> attr2;
    private static List<Attribute> attr3;
    private static List<Attribute> attr4;
    private static List<Attribute> attr;
    private static String appName;
    private static String appType;
    private static String appSize;
    private static String btnXPath;
    private static Boolean flag = false;

    /**
     * 监控按钮状态，直到应用安装完成。完成标志：“打开”
     *
     * @param xp4
     * @param xp6
     * @param driver
     * @param keyword
     */
    public static void monitorStatus(String xp4, String xp6, AndroidDriver driver, String keyword) {
        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        node = (Element) document.selectSingleNode(xp6);
        attr = node.attributes();
        for (Attribute e : attr) {
            if ("content-desc".equals(e.getName())) {
                System.out.println(e.getValue() + "...");
                if (!"打开".equals(e.getValue())) {
                    if ("安装中".equals(e.getValue())) {
                        try {
                            Thread.sleep(15 * 1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        monitorStatus(xp4, xp6, driver, keyword);
                    } else if ("安装".equals(e.getValue()) || "继续".equals(e.getValue())
                            || "连接中".equals(e.getValue())) {
                        driver.findElementByXPath(xp4).click();
                        System.out.println("单击按钮！");
                        try {
                            Thread.sleep(30 * 1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        monitorStatus(xp4, xp6, driver, keyword);
                    } else {
                        try {
                            Thread.sleep(30 * 1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("继续监控安装过程...");
                        monitorStatus(xp4, xp6, driver, keyword);
                    }
                } else {
                    System.out.println("“" + keyword + "”安装成功！！！");
                    //启动按钮路径
                    btnXPath = xp4;
                    appName = keyword;
                    xp2 = null;
                    xp3 = null;
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

    public static void selectApp_Step5(String xp, AndroidDriver driver, String keyword) {
        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp + "/android.view.View");
        //System.out.println("list1.size() = " + list1.size());
        if (list1.size() == 5) {
            node1 = (Element) document.selectSingleNode(xp + "/android.view.View[4]/android.view.View[1]");
            if (node1 != null) {
                attr1 = node1.attributes();
                for (Attribute e : attr1) {
                    if ("content-desc".equals(e.getName())) {
                        System.out.println(e.getName() + ":" + e.getValue());
                        appName = e.getValue();
                        if (appName.contains(keyword)) {
                            keyword = appName;
                            //获取App应用信息
                            System.out.println("App应用信息：");
                            System.out.println("App名称： " + appName);
                            node2 = (Element) document.selectSingleNode(xp + "/android.view.View[4]" +
                                    "/android.view.View[2]/android.view.View[1]/android.view.View[4]");

                            attr2 = node2.attributes();
                            for (Attribute e2 : attr2) {
                                if ("content-desc".equals(e2.getName())) {
                                    appType = e2.getValue();
                                    System.out.println("App类别： " + e2.getValue());
                                }
                            }
                            node3 = (Element) document.selectSingleNode(xp + "/android.view.View[4]" +
                                    "/android.view.View[2]/android.view.View[1]/android.view.View[1]");

                            attr3 = node3.attributes();
                            for (Attribute e3 : attr3) {
                                if ("content-desc".equals(e3.getName())) {
                                    appSize = e3.getValue();
                                    System.out.println("App文件大小： " + e3.getValue());
                                }
                            }

                            System.out.println("找到符合条件的App，该App名称为：“" + appName + "”。");
                            flag = true;
                            xp4 = xp + "/android.view.View[5]/android.widget.Button[1]";
                            xp6 = xp + "/android.view.View[5]/android.view.View[1]/android.view.View[2]";
                            //System.out.println(driver.getPageSource());
                            System.out.println("开始监控安装过程...");
                            monitorStatus(xp4, xp6, driver, keyword);
                        } else {
                            flag = false;
                            appName = null;
                            btnXPath = null;
                            System.out.println("应用商店内未找到“" + keyword + "”，请检查搜索关键词！");
                        }
                    }
                }
            }
        } else {
            flag = false;
        }
    }

    public static void selectApp_Step4(String xp3, AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp3);
        if (list1.size() > 7) {
            OUT:
            for (int i = 1; i < 4; i++) {
                node1 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                        CONFIGConstant.DETAIL_View + "/android.view.View[1]");
                if (node1 != null) {
                    attr1 = node1.attributes();
                    for (Attribute e : attr1) {
                        if ("content-desc".equals(e.getName())) {
                            System.out.println(e.getName() + ":" + e.getValue());
                            appName = e.getValue();
                            if (appName.contains(keyword) || keyword.contains(appName)) {
                                keyword = appName;
                                //获取App应用信息
                                System.out.println("App应用信息：");
                                System.out.println("App名称： " + appName);
                                node2 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[2]");

                                attr2 = node2.attributes();
                                for (Attribute e2 : attr2) {
                                    if ("content-desc".equals(e2.getName())) {
                                        appType = e2.getValue();
                                        System.out.println("App类别： " + e2.getValue());
                                    }
                                }
                                node3 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[3]");

                                attr3 = node3.attributes();
                                for (Attribute e3 : attr3) {
                                    if ("content-desc".equals(e3.getName())) {
                                        appSize = e3.getValue();
                                        System.out.println("App文件大小数字： " + e3.getValue());
                                    }
                                }
                                node4 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View +
                                        "/android.view.View[4]");

                                attr4 = node4.attributes();
                                for (Attribute e4 : attr4) {
                                    if ("content-desc".equals(e4.getName())) {
                                        System.out.println("App文件大小单位： " + e4.getValue());
                                    }
                                }
                                System.out.println("找到符合条件的App，该App名称为：“" + appName + "”。");
                                xp4 = xp3 + "[" + (i + 1) + "]" + CONFIGConstant.BTN_Button;
                                xp6 = xp3 + "[" + (i + 1) + "]" + CONFIGConstant.BTN_View;

                                //System.out.println(driver.getPageSource());
                                System.out.println("开始监控安装过程...");
                                monitorStatus(xp4, xp6, driver, keyword);
                                break OUT;
                            } else {
                                appName = null;
                                btnXPath = null;
                                System.out.println("应用商店内未找到“" + keyword + "”，请检查搜索关键词！");
                            }
                        }
                    }
                }
            }
        }
    }

    public static void selectApp_Step3(String xp2, String xp3, AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp2);
        if (list1.size() == 4) {
            xp5 = xp2 + "[3]" + "/android.view.View[1]";
            node1 = (Element) document.selectSingleNode(xp5);
            if (node1 != null) {
                attr1 = node1.attributes();
                for (Attribute e : attr1) {
                    if ("content-desc".equals(e.getName())) {
                        System.out.println(e.getName() + ":" + e.getValue());
                        appName = e.getValue();
                        if (appName.contains(keyword) || keyword.contains(appName)) {
                            keyword = appName;
                            //获取App应用信息
                            System.out.println("App应用信息：");
                            System.out.println("App名称： " + appName);
                            node2 = (Element) document.selectSingleNode(xp2 + "[3]" + "/android.view.View[2]");

                            attr2 = node2.attributes();
                            for (Attribute e2 : attr2) {
                                if ("content-desc".equals(e2.getName())) {
                                    appType = e2.getValue();
                                    System.out.println("App类别： " + e2.getValue());
                                }
                            }
                            node3 = (Element) document.selectSingleNode(xp2 + "[3]" + "/android.view.View[3]");

                            attr3 = node3.attributes();
                            for (Attribute e3 : attr3) {
                                if ("content-desc".equals(e3.getName())) {
                                    appSize = e3.getValue();
                                    System.out.println("App文件大小数字： " + e3.getValue());
                                }
                            }
                            node4 = (Element) document.selectSingleNode(xp2 + "[3]" + "/android.view.View[4]");

                            attr4 = node4.attributes();
                            for (Attribute e4 : attr4) {
                                if ("content-desc".equals(e4.getName())) {
                                    System.out.println("App文件大小单位： " + e4.getValue());
                                }
                            }
                            System.out.println("找到符合条件的App，该App名称为：“" + appName + "”。");
                            xp4 = xp3 + "[1]" + CONFIGConstant.BTN_Button;
                            xp6 = xp3 + "[1]" + CONFIGConstant.BTN_View;
                            System.out.println("开始监控安装过程...");
                            monitorStatus(xp4, xp6, driver, keyword);
                            break;
                        } else {
                            System.out.println("执行 Step4...");
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
    public static void selectApp_Step2(String xp3, AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        list1 = document.selectNodes(xp3);
        if (list1.size() > 7) {
            OUT:
            for (int i = 0; i < 7; i++) {
                node1 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                        CONFIGConstant.DETAIL_View + "/android.view.View[1]");
                if (node1 != null) {
                    attr1 = node1.attributes();
                    for (Attribute e : attr1) {
                        if ("content-desc".equals(e.getName())) {
                            System.out.println(e.getName() + ":" + e.getValue());
                            appName = e.getValue();
                            if (appName.contains(keyword) || keyword.contains(appName)) {
                                keyword = appName;
                                //获取App应用信息
                                System.out.println("App应用信息：");
                                System.out.println("App名称： " + appName);
                                node2 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[2]");
                                attr2 = node2.attributes();
                                for (Attribute e2 : attr2) {
                                    if ("content-desc".equals(e2.getName())) {
                                        appType = e2.getValue();
                                        System.out.println("App类别： " + e2.getValue());
                                    }
                                }
                                node3 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[3]");

                                attr3 = node3.attributes();
                                for (Attribute e3 : attr3) {
                                    if ("content-desc".equals(e3.getName())) {
                                        appSize = e3.getValue();
                                        System.out.println("App文件大小数字： " + e3.getValue());
                                    }
                                }
                                node4 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[4]");

                                attr4 = node4.attributes();
                                for (Attribute e4 : attr4) {
                                    if ("content-desc".equals(e4.getName())) {
                                        System.out.println("App文件大小单位： " + e4.getValue());
                                    }
                                }
                                System.out.println("找到符合条件的App，该App名称为：" + appName + "。");
                                xp4 = xp3 + "[" + (i + 1) + "]" + CONFIGConstant.BTN_Button;
                                xp6 = xp3 + "[" + (i + 1) + "]" + CONFIGConstant.BTN_View;
                                System.out.println("开始监控安装过程...");

                                monitorStatus(xp4, xp6, driver, keyword);
                                break OUT;
                            } else {
                                appName = null;
                                btnXPath = null;
                                System.out.println("应用商店内未找到“" + keyword + "”，请检查搜索关键词！");
                            }
                        }
                    }
                } else {
                    System.out.println("广告栏！");
                }
            }
        }else {
            OUT:
            for (int i = 0; i < list1.size(); i++) {
                node1 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                        CONFIGConstant.DETAIL_View + "/android.view.View[1]");
                if (node1 != null) {
                    attr1 = node1.attributes();
                    for (Attribute e : attr1) {
                        if ("content-desc".equals(e.getName())) {
                            System.out.println(e.getName() + ":" + e.getValue());
                            appName = e.getValue();
                            if (appName.contains(keyword) || keyword.contains(appName)) {
                                keyword = appName;
                                //获取App应用信息
                                System.out.println("App应用信息：");
                                System.out.println("App名称： " + appName);
                                node2 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[2]");
                                attr2 = node2.attributes();
                                for (Attribute e2 : attr2) {
                                    if ("content-desc".equals(e2.getName())) {
                                        appType = e2.getValue();
                                        System.out.println("App类别： " + e2.getValue());
                                    }
                                }
                                node3 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[3]");

                                attr3 = node3.attributes();
                                for (Attribute e3 : attr3) {
                                    if ("content-desc".equals(e3.getName())) {
                                        appSize = e3.getValue();
                                        System.out.println("App文件大小数字： " + e3.getValue());
                                    }
                                }
                                node4 = (Element) document.selectSingleNode(xp3 + "[" + (i + 1) + "]" +
                                        CONFIGConstant.DETAIL_View + "/android.view.View[4]");

                                attr4 = node4.attributes();
                                for (Attribute e4 : attr4) {
                                    if ("content-desc".equals(e4.getName())) {
                                        System.out.println("App文件大小单位： " + e4.getValue());
                                    }
                                }
                                System.out.println("找到符合条件的App，该App名称为：" + appName + "。");
                                xp4 = xp3 + "[" + (i + 1) + "]" + CONFIGConstant.BTN_Button;
                                xp6 = xp3 + "[" + (i + 1) + "]" + CONFIGConstant.BTN_View;
                                System.out.println("开始监控安装过程...");

                                monitorStatus(xp4, xp6, driver, keyword);
                                break OUT;
                            } else {
                                appName = null;
                                btnXPath = null;
                                System.out.println("应用商店内未找到“" + keyword + "”，请检查搜索关键词！");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断搜索结果情况，共两种情况：
     * condition1:页面中推荐了符合条件的应用，该应用栏被放大突出
     * condition2:与condition1相对，每个应用栏都相同，无突出
     * new condition:搜索 “快手” step 5
     * new condition:搜索 “PU”、“BBM”等
     * new condition:搜索“百度手机助手” ，当前页面显示“含应用商店属性的应用分发类应用暂不收录”
     *
     *
     * @param driver
     * @param keyword
     */
    public static void selectApp_Step1(AndroidDriver driver, String keyword) {

        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        xp1 = CONFIGConstant.XPATH_ListView;
        list1 = document.selectNodes(xp1);
        System.out.println("android.widget.ListView 个数：" + list1.size());
        if (list1.size() == 0){
            xp1 = CONFIGConstant.NORESULT_View;
            list1 = document.selectNodes(xp1);
            if (list1.size() == 1){
                System.out.println("应用商店内未找到“" + keyword + "”，该应用属于“应用商店类”应用。");
            }
        }
        if (list1.size() == 3) {
            System.out.println("执行 Step5...");
            selectApp_Step5(xp1 + "[1]/android.view.View[1]", driver, keyword);
            if (!flag) {
                //System.out.println("listView[1]无合适应用。。。");
                xp2 = xp1 + "[2]";
                xp3 = xp1 + "[2]";
            }

        }
        if (list1.size() == 2) {
            xp2 = xp1 + "[1]";
            xp3 = xp1 + "[1]";
        }
        if (StringUtils.isNotEmpty(xp2)) {
            xp2 = xp2 + CONFIGConstant.XPATH_View;
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                System.out.println("字符串转换文本失败！");
                e.printStackTrace();
            }
            list2 = document.selectNodes(xp2);
            //System.out.println("android.view.View 个数：" + list2.size());
            if (list2.size() == 2) {
                xp3 = xp3 + "/android.view.View";
                //Condition1:
                System.out.println("执行 Step2...");
                selectApp_Step2(xp3, driver, keyword);
            } else {
                xp3 = xp3 + "/android.view.View";
                //Condition2:
                System.out.println("执行 Step3...");
                selectApp_Step3(xp2, xp3, driver, keyword);
            }
        }

    }

    /**
     * 检查网络状况，搜索app的时候，若WLAN中断或网速极差，
     * 搜索页面会加载不出来，需要点击页面上的重试按钮，
     * 点击“重试”按钮后等待30s,继续验证搜索结果页面是
     * 否加载出来，3次失败后默认无网络，退出程序。
     *
     * @param failCount
     * @param driver
     * @return
     */
    public static Boolean checkNetAvailable(int failCount, AndroidDriver driver) {
        boolean flag = false;
        try {
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            System.out.println("获取界面元素时出错！");
            e.printStackTrace();
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            System.out.println("字符串转换文本失败！");
            e.printStackTrace();
        }
        //查找网络请求失败的画面节点
        list1 = document.selectNodes(CONFIGConstant.NET_REQ_FAIL_XPATH);
        if (list1.size() == 1) {
            System.out.println("网络连接出错！");
            //查找当前界面是否有 重试按钮 的节点
            list2 = document.selectNodes(CONFIGConstant.RETRY_BTN_XPATH);
            if (list2.size() == 1) {
                driver.findElement(By.xpath(CONFIGConstant.RETRY_BTN_XPATH)).click();
            }
            /*
             * 点击“重试”按钮后刷新界面等待30s
             */
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            failCount = failCount + 1;
            if (3 == failCount) {
                System.out.println("请检查网络连接！");
                flag = false;
                //log_pr.close();
                //System.exit(5);
            } else {
                checkNetAvailable(failCount, driver);
            }

        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查所输文件路径是否存在
     *
     * @param app_filePath
     * @param log_outPath
     * @param pkg_outPath
     * @return
     */
    public static Boolean checkFileExist(String app_filePath, String log_outPath, String pkg_outPath) {
        boolean flag;
        if (new File(app_filePath).exists()
                && new File(log_outPath).exists()
                && new File(pkg_outPath).exists()) {
            flag = true;
        } else {
            if (!new File(app_filePath).exists()) {
                System.out.println("请检查第一个参数的文件路径是否存在。");
            }
            if (!new File(log_outPath).exists()) {
                System.out.println("请检查第二个参数的文件路径是否存在。");
            }
            if (!new File(pkg_outPath).exists()) {
                System.out.println("请检查第三个参数的文件路径是否存在。");
            }
            flag = false;
        }
        return flag;
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
    public static List<String> readAppName(String filePath) {
        BufferedReader br = null;
        String rec = null;
        List<String> list = new ArrayList<String>();
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(filePath)), "UTF-8"));
            while ((rec = br.readLine()) != null) {// 使用readLine方法，一次读一行
                if (StringUtils.isNotEmpty(rec.trim())) {
                    list.add(rec);
                }
                System.out.println(rec);
            }
            System.out.println("文档中有" + list.size() + "个App名称。");
            br.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            System.out.println("不支持的编码格式！");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("未找到配置文件，请检查app配置文档路径是否正确！");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 配置连接手机的属性
     *
     * @return
     */
    public static DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        //连接的设备名称
        capabilities.setCapability("deviceName", CONFIGConstant.DEVICENAME);
        //默认
        capabilities.setCapability("automationName", CONFIGConstant.AUTOMATIONNAME);
        //手机系统类型 Android/iOS
        capabilities.setCapability("platformName", CONFIGConstant.PLATFORMNAME);
        //手机系统版本
        capabilities.setCapability("platformVersion", CONFIGConstant.PLATFORMVERSION);
        //等待下一个命令时限，单位:s。若下一个命令超过设定的时长，AppiumClient关闭
        capabilities.setCapability("newCommandTimeout", CONFIGConstant.NEWCOMMANDTIMEOUT);
        //要测试的应用包名，需要用adb命令查看
        capabilities.setCapability("appPackage", CONFIGConstant.APPPACKAGE);
        //要测试的应用入口，通过调起活动来启动应用，需要用adb命令查看启动活动
        capabilities.setCapability("appActivity", CONFIGConstant.APPACTIVITY);
        //启动运行日志记录，默认为false
        capabilities.setCapability("enablePerformanceLogging", "true");
        //开启web视图测试环境，针对混合App（有原生也有web）
        capabilities.setCapability("autoWebview", "true");
        //开启Unicode输入，由Appium安装到手机，会导致手机内部的输入法不可用
        capabilities.setCapability("unicodeKeyboard", "True");
        //重置键入状态
        capabilities.setCapability("resetKeyboard", "True");

        return capabilities;
    }

    /**
     * 关启AppiumServer
     *
     * @return driver
     */
    public static AndroidDriver startAndStopAppium() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM node.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Appium Server stopped");
        // 处理外部命令执行的结果，释放当前线程，不会阻塞线程
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        //CommandLine commandLine = CommandLine.parse("cmd.exe /c node E:\\Appium\\node_modules\\appium\\bin\\appium.js");

        CommandLine commandLine = CommandLine.parse("cmd.exe /c node E:\\20180302\\Appium\\node_modules\\appium\\bin\\appium.js");

        // 创建监控时间60s,超过60s则中断执行
        ExecuteWatchdog dog = new ExecuteWatchdog(60 * 1000);
        Executor executor = new DefaultExecutor();

        // 设置命令执行退出值为1，如果命令成功执行并且没有错误，则返回1
        executor.setExitValue(1);
        executor.setWatchdog(dog);
        try {
            executor.execute(commandLine, resultHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resultHandler.waitFor(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Appium server start...");
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Appium server started");

        AndroidDriver driver = null;
        //连接Appium
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), getDesiredCapabilities());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始点击搜索输入框。");
        try {
            driver.findElement(By.id(CONFIGConstant.MARKET_SEARCH_BOX)).click();
            System.out.println("已点击搜索框，待输入搜索app名称...");
        } catch (Exception e) {
            System.out.println("未找到搜索框界面元素！");
            driver.quit();
            System.exit(-1);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return driver;
    }

    /**
     * @param args args[0] app配置文档存放路径
     *             args[1] app搜索下载安装过程日志的输出路径
     *             args[2] app启动到卸载产生的数据包文档输出路径
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("请检查输入的参数是否完整！args[0]:app名称配置文档存放路径 args[1]:安装日志输出路径 args[2]:数据包文档输出路径");
            System.exit(-1);
        }

        /*
           检查三个路径是否存在，若都存在，进行后续操作
         */
        if (checkFileExist(args[0], args[1], args[2])) {
            //启动Appium
            AndroidDriver driver = startAndStopAppium();

            List<String> appList = readAppName(args[0]);

            String log_outPath = null;
            appInfo appInfo = null;
            CapturePkg cap = null;

            for (int i = 0; i < appList.size(); i++) {
                //定量关启Appium
                if ((i + 1) % 17 == 0) {
                    driver = startAndStopAppium();
                }
                //设置日志命名
                log_outPath = args[1] + "pro_log_" + appList.get(i) + ".txt";
                //启动App之前的
                try {
                    log_pr = new PrintStream(log_outPath);
                    System.setOut(log_pr);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("清空输入框内容并键入App名称。");
                try {
                    driver.findElementById(CONFIGConstant.INPUT_BOX).clear();//清空
                    driver.findElementById(CONFIGConstant.INPUT_BOX).sendKeys(appList.get(i));//键入
                    driver.pressKeyCode(CONFIGConstant.SUBMIT_CODE);//提交搜索内容
                } catch (Exception e) {
                    System.out.println("未找到该界面元素！");
                    driver.quit();
                    System.exit(-2);
                }
                //加载搜索结果界面
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //检查网络状况
                if (checkNetAvailable(0, driver)) {
                    System.out.println("执行 Step1...");
                    selectApp_Step1(driver, appList.get(i));
                    log_pr.close();
                    //启动App，开始抓包
                    if (StringUtils.isNotEmpty(appName)) {
                        appInfo = new appInfo();
                        appInfo.setAppName(appName);
                        appInfo.setAppType(appType);
                        if (appSize.contains(".")) {
                            appSize = appSize.substring(0, appSize.indexOf("."));
                        }
                        appInfo.setAppSize(Integer.valueOf(appSize.trim()));
                        appInfo.setPkg_outPath(args[2]);
                        appInfo.setBtnXp(btnXPath);
                        cap = new CapturePkg(appInfo, driver);
                        cap.capture();
                    } else {
                        continue;
                    }

                } else {
                    log_pr.close();
                    driver.quit();
                    try {
                        Runtime.getRuntime().exec("taskkill /F /IM node.exe");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(-1);
                }


            }
            driver.quit();
            try {
                Runtime.getRuntime().exec("taskkill /F /IM node.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.exit(-1);
        }
    }
}

package optimizationprogram.mainPro;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.services.HuaWeiMarketXPath;
import optimizationprogram.mainPro.services.SelectAndDownloadService;
import optimizationprogram.tools.GotoSleep;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.List;

public class SelectAndDownloadAtHuaWeiMarket1 implements SelectAndDownloadService {
    private Document document;
    private String xmlStr;
    private String appName;
    private String appType;
    private String appSize;
    private String btnXPath;
    private String failInfo = "";
    private String tmpStr;
    private Boolean flag = false;
    private Integer count = 0;
    private Integer count1 = 0;
    private Integer width = 0;
    private Integer height = 0;

    private SelectedAppInfo sai = new SelectedAppInfo();
    private GotoSleep sleep = new GotoSleep();
    private List<Node> list;
    private List<Node> list1;
    private List<Node> list2;
    private Element node;
    private Element node1;
    private Element node2;
    private List<Attribute> attr;
    private List<Attribute> attr1;
    private List<Attribute> attr2;

    public void getAppLabels(AndroidDriver driver) {
        CaptureGUI.textArea_log.append("INFO:开始获取app标签。" + "\n");
        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
        count = 0;
        appType = "";
        tmpStr = "";
        OUT:
        for (int i = 0; i < 6; i++) {
            try {
                driver.swipe(width / 2, height * 5 / 6, width / 2, height * 1 / 6, 8000);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
            xmlStr = driver.getPageSource();
            //tmpStr = xmlStr;
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                System.out.println("字符串转换文本失败！");
                e.printStackTrace();
            }
            list = document.selectNodes(HuaWeiMarketXPath.APPLABEL_PATH);
            if (list.size() > 0) {
                //System.out.println(list.size());

                list1 = document.selectNodes(HuaWeiMarketXPath.APPLABEL_LINEARLAYOUT_PATH1);

                if (list1.size() > 0) {

                    for (int j = 0; j < list1.size(); j++) {
                        //这块出错
                        node1 = (Element) document.selectSingleNode(HuaWeiMarketXPath.APPLABEL_LINEARLAYOUT_PATH1 + "[" + (j + 1) + "]" +
                                "/android.widget.TextView[@text = '应用标签']");
                        if (node1 != null) {
                            list2 = document.selectNodes(HuaWeiMarketXPath.APPLABEL_LINEARLAYOUT_PATH1 + "[" + (j + 1) + "]" +
                                    HuaWeiMarketXPath.APPLABEL_LINEARLAYOUT_PATH2);
                            if (list2.size() > 0) {
                                for (int k = 0; k < list2.size(); k++) {
                                    node2 = (Element) document.selectSingleNode(HuaWeiMarketXPath.APPLABEL_LINEARLAYOUT_PATH1 + "[" + (j + 1) + "]" +
                                            HuaWeiMarketXPath.APPLABEL_LINEARLAYOUT_PATH2 + "[" + (k + 1) + "]" +
                                            "/android.widget.TextView[1]");
                                    if (node2 != null) {
                                        attr2 = node2.attributes();
                                        for (Attribute e2 : attr2) {
                                            if ("text".equals(e2.getName())) {
                                                if (e2.getValue() != null && !"".equals(e2.getValue())) {
                                                    if (count == 0) {
                                                        appType = appType + e2.getValue() + "，";
                                                    }
                                                    if (count == 1) {
                                                        tmpStr = tmpStr + e2.getValue() + "，";
                                                    }
                                                    sleep.startSleep(2 * 1000);
                                                }
                                            }
                                        }
                                    }
                                }
                                count = count + 1;
                                if (count == 2) {
                                    if (appType.length() > 0) {
                                        appType = appType.substring(0, appType.length() - 1);
                                    }
                                    if (tmpStr.length() > 0) {
                                        tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
                                    }
                                    if (appType.length() < tmpStr.length()) {
                                        appType = tmpStr;
                                    }
                                    count = 0;
                                    break OUT;
                                }
                            }
                        }
                    }
                }
            }
            sleep.startSleep(3 * 1000);
        }
        if (count == 1) {
            if (appType.length() > 0) {
                appType = appType.substring(0, appType.length() - 1);
            }
            count = 0;
        }
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
        if(xmlStr.contains("没有符合条件的内容")){
            sai.setFlag("否");
            sai.setFailInfo("应用商店未搜到任何结果。");
            CaptureGUI.textArea_log.append("INFO:应用市场未找到“" + keyword + "”，开始搜索下一个App···\n");
        }else {
            try {
                document = DocumentHelper.parseText(xmlStr);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            list = document.selectNodes(HuaWeiMarketXPath.LISTVIEW_LINEARLAYOUT_PATH);
            //System.out.println(list.size());
            if (list.size() > 0) {
            /*
                有最佳推荐:list.size() = 5
                无最佳推荐:list.size() = 6
             */
                if (list.size() >= 5) {
                    OUT:
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 2 && list.size() == 5) {
                            continue;
                        }
                        node1 = (Element) document.selectSingleNode(HuaWeiMarketXPath.LISTVIEW_LINEARLAYOUT_PATH + "[" + (i + 1) + "]" +
                                HuaWeiMarketXPath.APPNAME_TEXTVIEW);
                        if (node1 != null) {
                            attr1 = node1.attributes();
                            for (Attribute e1 : attr1) {
                                if ("text".equals(e1.getName())) {
                                    appName = e1.getValue();
                                    if (appName.contains(keyword) || keyword.contains(appName)) {
                                        //爬取app大小
                                        list1 = document.selectNodes(HuaWeiMarketXPath.LISTVIEW_LINEARLAYOUT_PATH + "[" + (i + 1) + "]" +
                                                HuaWeiMarketXPath.APPSIZE_TEXTVIEW);
                                        if (list1.size() == 2) {
                                            node2 = (Element) document.selectSingleNode(HuaWeiMarketXPath.LISTVIEW_LINEARLAYOUT_PATH + "[" + (i + 1) + "]" +
                                                    HuaWeiMarketXPath.APPSIZE_TEXTVIEW + "[2]");
                                            attr2 = node2.attributes();
                                            for (Attribute e2 : attr2) {
                                                if ("text".equals(e2.getName())) {
                                                    appSize = e2.getValue();
                                                    //sai.setAppSize(appSize.substring(0, appSize.length() - 1).trim());
                                                    break;
                                                }
                                            }
                                        }
                                        if (list1.size() == 1) {
                                            node2 = (Element) document.selectSingleNode(HuaWeiMarketXPath.LISTVIEW_LINEARLAYOUT_PATH + "[" + (i + 1) + "]" +
                                                    HuaWeiMarketXPath.APPSIZE_TEXTVIEW + "[1]");
                                            attr2 = node2.attributes();
                                            for (Attribute e2 : attr2) {
                                                if ("text".equals(e2.getName())) {
                                                    appSize = e2.getValue();
                                                    //sai.setAppSize(appSize.substring(0, appSize.length() - 1).trim());
                                                    break;
                                                }
                                            }
                                        }
                                        //点击进入app详请爬取app标签并下载
                                        try {
                                            CaptureGUI.textArea_log.append("INFO:已找到符合条件的App：" + appName + "，开始进入app详情界面\n");
                                            driver.findElement(By.xpath(HuaWeiMarketXPath.LISTVIEW_LINEARLAYOUT_PATH + "[" + (i + 1) + "]")).click();
                                        } catch (Exception e) {
                                            CaptureGUI.textArea_log.append("ERROR:点击进入app详情界面时出错。\n");
                                            e.printStackTrace();
                                            return;
                                        }
                                        CaptureGUI.textArea_log.append("INFO:已进入app详情界面。" + "\n");
                                        sleep.startSleep(5 * 1000);
                                    /*
                                        获取应用所属标签
                                     */
                                        getAppLabels(driver);

                                        if (afterSelect(driver)) {
                                            CaptureGUI.textArea_log.append("INFO:下载安装完成。\n");
                                            sai.setAppName(appName);
                                            sai.setAppType(appType);
                                            try{
                                                sai.setAppSize(appSize.substring(0, appSize.indexOf("B")).trim());
                                            }catch (IndexOutOfBoundsException e){
                                                sai.setAppSize("");
                                                e.printStackTrace();
                                            }
                                            sai.setFailInfo("");
                                            sai.setOpenXPath(btnXPath);
                                            sai.setFlag("是");
                                        } else {
                                            CaptureGUI.textArea_log.append("INFO:下载安装失败。\n");
                                            sai.setAppName(appName);
                                            sai.setAppType(appType);
                                            try{
                                                sai.setAppSize(appSize.substring(0, appSize.indexOf("B")).trim());
                                            }catch (IndexOutOfBoundsException e){
                                                sai.setAppSize("");
                                                e.printStackTrace();
                                            }

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
                                        if (i == list.size() - 1) {
                                            sai.setFlag("否");
                                            sai.setFailInfo("应用商店未找到符合条件的应用，找到与搜索词相似的应用："
                                                    + failInfo.substring(0, failInfo.length() - 1));
                                            CaptureGUI.textArea_log.append("INFO:应用商店未找到“" + keyword + "”，开始搜索下一个App···\n");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            /*
                无推荐

            if (list.size() == 6) {

            }*/
            } else {
                //结构改变
            }
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
        list = document.selectNodes(HuaWeiMarketXPath.NET_REQ_FAIL_PATH);
        //如果网络连接出错，则再次搜索刷新
        if (list.size() > 0) {
            driver.pressKeyCode(AndroidKeyCode.ENTER);//提交搜索内容
            sleep.startSleep(30 * 1000);
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
        node = (Element) document.selectSingleNode(HuaWeiMarketXPath.BUTTON_PATH);
        if (node != null) {
            CaptureGUI.textArea_log.append("INFO:点击安装..." + "\n");
            attr = node.attributes();
            for (Attribute e : attr) {
                if ("content-desc".equals(e.getName())) {
                    if (!"打开".equals(e.getValue())) {
                        if (e.getValue().contains("安装中")) {
                            count = count + 1;
                            if (count > 20) {
                                count = 0;
                                return false;
                            }
                            CaptureGUI.textArea_log.append("INFO:安装中···" + "\n");
                            sleep.startSleep(15 * 1000);

                            afterSelect(driver);
                        } else if ("安装".equals(e.getValue()) || "继续".equals(e.getValue())
                                ) {
                            driver.findElementByXPath(HuaWeiMarketXPath.BUTTON_PATH).click();

                            if (count1 == 0) {
                                sleep.startSleep(6 * 1000);
                                //判断是否有弹窗
                                try {
                                    xmlStr = driver.getPageSource();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                    return false;
                                }
                                try {
                                    document = DocumentHelper.parseText(xmlStr);
                                } catch (DocumentException e1) {
                                    e1.printStackTrace();
                                    return false;
                                }
                                list1 = document.selectNodes(HuaWeiMarketXPath.BEFOREINSTALLDIALOGBOX_PATH);

                                if (list1.size() > 0) {
                                    driver.findElementByXPath(HuaWeiMarketXPath.BEFOREINSTALLDIALOGBOX_PATH).click();
                                }
                            }
                            count1 = count1 + 1;
                            CaptureGUI.textArea_log.append("INFO:正在下载···" + "\n");
                            sleep.startSleep(20 * 1000);
                            afterSelect(driver);
                        } else {
                            CaptureGUI.textArea_log.append("INFO:正在下载···" + count1 + "\n");
                            sleep.startSleep(20 * 1000);
                            afterSelect(driver);
                        }
                    } else {
                        //启动按钮路径
                        btnXPath = HuaWeiMarketXPath.BUTTON_PATH;
                    }
                }
            }
        }
        return true;
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

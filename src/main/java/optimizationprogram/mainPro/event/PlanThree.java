package optimizationprogram.mainPro.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.services.XPathConstant;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class PlanThree {
    private Element node = null;
    private String xmlStr;
    private Document document = null;
    private List<Node> list = null;
    private List<Node> list_text = null;
    private List<Node> list_contentdesc = null;
    private List<Node> list_null = null;
    private List<Attribute> attr = null;
    private String appType;
    private String btnXp;
    private List<String> xpList = null;
    private String tmp;
    private Integer successcount = 0;
    private String marketXml;
    private String mainpageXp = "";

    public PlanThree(String appType, String btnXp) {
        this.appType = appType;
        this.btnXp = btnXp;
    }

    public void clickElementReverse(Integer count, AndroidDriver driver) {
        String currxp = "";
        String mainxp = "";
        for (int i = count; i > 0; i--) {
            mainxp = xpList.get(i-1);
            try {
                //CaptureGUI.textArea_log.append("点击元素:  " + mainxp + "\n");
                CaptureGUI.textArea_log.append("点击元素:\n");
                driver.findElement(By.xpath(mainxp)).click();
            } catch (Exception e) {
                return;
            }
            try {
                Thread.sleep(6 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currxp = xpList.get(count);
            try {
                //CaptureGUI.textArea_log.append("点击元素:  " + currxp + "\n");
                CaptureGUI.textArea_log.append("点击元素:\n");
                driver.findElement(By.xpath(currxp)).click();
                successcount = successcount + 1;
                //CaptureGUI.textArea_log.append("找到主界面Xpath:  " + mainxp + "\n");
                mainpageXp = mainxp;
                break;
            } catch (Exception e) {
                CaptureGUI.textArea_log.append("未找到找到主界面Xpath。。。\n");
            }
        }
    }

    public void clickElement(AndroidDriver driver) {
        String xp = "";
        for (int i = 0; i < xpList.size(); i++) {
            marketXml = driver.getPageSource();
            //if (marketXml.contains("安装") && marketXml.contains("打开") && marketXml.contains(appType)) {
            if (marketXml.contains("打开")) {
                CaptureGUI.textArea_log.append("已退出程序，重新打开应用。" + "\n");
                try {
                    driver.findElement(By.xpath(btnXp)).click();
                } catch (Exception e) {
                    return;
                }
                try {
                    Thread.sleep(6 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
            }
            /*if (failcount > 20) {
                CaptureGUI.textArea_log.append("元素定位出错超过20次，即将自动退出。 " + "\n");
                break;
            }*/
            xp = xpList.get(i);
            try {
                //CaptureGUI.textArea_log.append("点击元素:  " + xp + "\n");
                CaptureGUI.textArea_log.append("点击元素:\n");

                driver.findElement(By.xpath(xp)).click();
                successcount = successcount + 1;
            } catch (Exception e) {
                /*if ("".equals(mainpageXp)){
                    try {
                        driver.findElement(By.xpath(mainpageXp)).click();
                    } catch (Exception e1) {
                        CaptureGUI.textArea_log.append("主xpath已经不适用\n");
                    }
                }else {
                    clickElementReverse(i, driver);
                }*/
                //回点
                clickElementReverse(i, driver);
                //failcount = failcount + 1;
                //e.printStackTrace();
            }
            try {
                Thread.sleep(6 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CaptureGUI.textArea_log.append("点击返回。。。" + "\n");
            driver.pressKeyCode(AndroidKeyCode.BACK);
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handle(AndroidDriver driver) {
        List<String> tmpList = new ArrayList<>();
        xmlStr = driver.getPageSource();
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }
        list = document.selectNodes(XPathConstant.ELE_CLICKABLE_XPATH);
        CaptureGUI.textArea_log.append("PlaneThree 主界面可点击元素个数=" + list.size() + "\n");
        if (list.size() > 0) {
            tmp = list.get(0).getPath();
            tmpList.add(tmp);
        }
        for (int i = 1; i < list.size(); i++) {
            tmpList.add(list.get(i).getPath());
        }
        process(tmpList);
        clickElement(driver);
        CaptureGUI.textArea_log.append("PlaneThree 成功点击元素个数=" + successcount + "\n");

    }

    public void process(List<String> list) {
        xpList = new ArrayList<>();
        String str1;
        String str2;
        Integer no;
        if (list.size() > 0) {
            xpList.add(list.get(0) + "[1]");
            for (int i = 1; i < list.size(); i++) {
                no = 1;
                str1 = list.get(i);
                for (int j = 0; j < i; j++) {
                    str2 = list.get(j);
                    if (str1.equals(str2)) {
                        no = no + 1;
                    } else {
                        no = 1;
                    }
                }
                xpList.add(str1 + "[" + no + "]");
                System.out.println(xpList.get(i));
            }
        }
    }
}

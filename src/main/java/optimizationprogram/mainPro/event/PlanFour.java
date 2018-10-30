package optimizationprogram.mainPro.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.services.XPathConstant;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class PlanFour {
    private String appType;
    private String btnXp;
    private Integer successcount = 0;
    private String marketXml;

    public PlanFour(String appType, String btnXp) {
        this.appType = appType;
        this.btnXp = btnXp;
    }
    /*public static void main(String[] args) {
        String xmlStr = "";
        String rule = "//*[@clickable = 'true']";
        String xpath = "";
        String nodename = "";
        String parentXpath = "";
        String tmp = "";
        List<Node> list = null;
        List<String> xpList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> parentList = new ArrayList<>();
        List<String> tmpList = new ArrayList<>();
        Document document = null;
        Element node = null;
        String[] bounds = new String[2];
        List<String[]> boundsList = new ArrayList<>();
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }
        list = document.selectNodes(rule);
        System.out.println(list.size());
        if (list.size() > 0) {
            tmp = list.get(0).getPath();
            node = (Element) list.get(0);
            bounds = processBounds(node);
            boundsList.add(bounds);
            tmpList.add(tmp);
            //System.out.println(list.get(0).getPath());
        }
        for (int i = 1; i < list.size(); i++) {
            node = (Element) list.get(i);
            bounds = processBounds(node);
            boundsList.add(bounds);
            tmpList.add(list.get(i).getPath());
        }
        xpList = process(tmpList);
        if (xpList.size() == boundsList.size()) {
            for (int j = 0; j < xpList.size(); j++) {
                nameList.add(xpList.get(j) + "," + boundsList.get(j)[0] + "," + boundsList.get(j)[1]);
            }
        }

        parentList = sortXpath(nameList);
        for (int z = 0; z < parentList.size(); z++) {
            System.out.println(parentList.get(z));
        }
    }*/

    public void clickElement(AndroidDriver driver, List<String> list) {
        String xp = "";
        for (int i = 0; i < list.size(); i++) {
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
            xp = list.get(i);
            try {
                CaptureGUI.textArea_log.append("点击元素:\n");
                driver.findElement(By.xpath(xp)).click();
                successcount = successcount + 1;
            } catch (Exception e) {
                CaptureGUI.textArea_log.append("点击出错。。。" + "\n");
                CaptureGUI.textArea_log.append("点击返回。。。" + "\n");
                driver.pressKeyCode(AndroidKeyCode.BACK);
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    driver.findElement(By.xpath(xp)).click();
                    successcount = successcount + 1;
                } catch (Exception e2) {
                    CaptureGUI.textArea_log.append("点击再次出错。。。" + "\n");
                }
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
        String xmlStr = driver.getPageSource();
        String xp = "";
        List<Node> list;
        List<String> xpList;
        List<String> tmpList = new ArrayList<>();
        Document document;
        Element node;
        String[] boundsArray = new String[2];
        List<String[]> boundsList = new ArrayList<>();
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }
        list = document.selectNodes(XPathConstant.ELE_CLICKABLE_XPATH);
        System.out.println(list.size());
        CaptureGUI.textArea_log.append("PlaneFour 可点击元素个数：" + list.size() + "\n");
        if (list.size() > 0) {
            xp = list.get(0).getPath();
            node = (Element) list.get(0);
            boundsArray = processBounds(node);
            boundsList.add(boundsArray);
            tmpList.add(xp);
            //System.out.println(list.get(0).getPath());
        }
        for (int i = 1; i < list.size(); i++) {
            node = (Element) list.get(i);
            boundsArray = processBounds(node);
            boundsList.add(boundsArray);
            tmpList.add(list.get(i).getPath());
        }
        xpList = process(tmpList);
        tmpList = new ArrayList<>();
        if (xpList.size() == boundsList.size()) {
            for (int j = 0; j < xpList.size(); j++) {
                tmpList.add(xpList.get(j) + "," + boundsList.get(j)[0] + "," + boundsList.get(j)[1]);
            }
        }
        xpList = new ArrayList<>();
        xpList = sortXpath(tmpList);
        for (int z = 0; z < xpList.size(); z++) {
            System.out.println(xpList.get(z));
        }
        clickElement(driver, xpList);
        CaptureGUI.textArea_log.append("PlaneFour 成功点击元素个数=" + successcount + "\n");
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给Xparh加序号
     *
     * @param list
     * @return
     */
    public List<String> process(List<String> list) {
        List<String> index = new ArrayList<>();
        String str1;
        String str2;
        Integer no = 1;
        if (list.size() > 0) {
            index.add(list.get(0) + "[1]");
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
                index.add(str1 + "[" + no + "]");
            }
        }
        return index;
    }

    /**
     * 处理可点击元素坐标
     *
     * @param node
     * @return
     */
    public String[] processBounds(Element node) {
        List<Attribute> attr = null;
        String bounds = "";
        String[] location = new String[4];
        String[] boundsArray = new String[2];
        String tmpStr = "";
        attr = node.attributes();
        for (Attribute e : attr) {
            if ("bounds".equals(e.getName())) {
                bounds = e.getValue();
                tmpStr = bounds.replace("[", "")
                        .replace("]", ",");
                location = tmpStr.substring(0, tmpStr.length() - 1)
                        .split(",");
                boundsArray[0] = String.valueOf((Double.valueOf(location[0]) + Double.valueOf(location[2])) / 2);
                boundsArray[1] = String.valueOf((Double.valueOf(location[1]) + Double.valueOf(location[3])) / 2);
                break;
            }
        }
        return boundsArray;
    }

    /**
     * Xpath按坐标位置排序，由最底端到最顶端，由右向左
     *
     * @param list
     * @return
     */
    public List<String> sortXpath(List<String> list) {
        List<String> sortList = new ArrayList<>();
        Double max;
        Double nextData;
        String tmp;
        String y;
        String z;
        for (int i = 0; i < list.size(); i++) {
            y = list.get(i).split(",")[2];
            max = Double.valueOf(y);
            for (int j = i + 1; j < list.size(); j++) {
                z = list.get(j).split(",")[2];
                nextData = Double.valueOf(z);
                if (nextData >= max) {
                    tmp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmp);
                }
            }
        }
        for (int k = 0; k < list.size(); k++) {
            tmp = list.get(k).split(",")[0];
            sortList.add(tmp);
        }
        return sortList;
    }

}

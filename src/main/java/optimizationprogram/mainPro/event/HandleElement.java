package optimizationprogram.mainPro.event;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import optimizationprogram.GUICode.CaptureGUI;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HandleElement {
    private Element node = null;
    private String xmlStr;
    private Document document = null;
    private List<Node> list = null;
    private List<Attribute> attr = null;
    private List<String> rules = null;
    private List<Integer> indexes = null;
    private List<String[]> boundsList = null;
    private Boolean flag = true;
    private String xp1 = null;//参考xpth
    private Integer failcount = 0;
    private String marketXml;
    private String appType;
    private String btnXp;
    private Integer num = 1;

    public HandleElement(String appType, String btnXp) {
        this.appType = appType;
        this.btnXp = btnXp;
        BufferedReader br;
        String rec;
        rules = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream("/xpath/HORIZONTALSCROLLVIEW.txt")));
            while ((rec = br.readLine()) != null) {// 使用readLine方法，一次读一行
                if (StringUtils.isNotEmpty(rec.trim()) && !rec.startsWith("#")) {
                    rules.add(rec);
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

    public void clickNavigationBar(AndroidDriver driver, String xp) {
        flag = true;
        try {
            driver.findElement(By.xpath(xp)).click();
        } catch (Exception e) {
            failcount = failcount + 1;
//            flag = false;
            //e.printStackTrace();
        }

        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            driver.findElement(By.xpath(xp1)).click();
        } catch (Exception e) {
            flag = false;
            //e.printStackTrace();
        }
        if (!flag) {
            marketXml = driver.getPageSource();
            if (marketXml.contains("安装") && marketXml.contains("打开") && marketXml.contains(appType)) {
                try {
                    driver.findElement(By.xpath(btnXp)).click();
                } catch (Exception e) {
                    return;
                }

            } else {
                driver.pressKeyCode(AndroidKeyCode.BACK);
            }
        }
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processData() {
        indexes = new ArrayList<>();
        if (boundsList.size() > 0) {
            indexes.add(1);
            for (int i = 1; i < boundsList.size(); i++) {
                if (boundsList.get(i)[0].equals(boundsList.get(i - 1)[2])) {
                    num++;
                    indexes.add(num);
                } else {
                    num = 1;
                    if (boundsList.get(i)[1].equals(boundsList.get(i - 1)[1])
                            && boundsList.get(i)[3].equals(boundsList.get(i - 1)[3])) {
                        indexes.add(i + 1);
                    } else {
                        num = 1;
                        indexes.add(1);
                    }
                }
            }
        }
    }

    public void handle(AndroidDriver driver) {
        String bounds = "";
        String[] tmp = new String[4];
        String tmpStr = "";
        xmlStr = driver.getPageSource();
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(rules.size());
        String xp = "";
        for (int i = 0; i < rules.size(); i++) {
            list = document.selectNodes(rules.get(i));
            CaptureGUI.textArea_log.append("HandleElement list.size=" + list.size() + "\n");
            boundsList = new ArrayList<>();
            failcount = 0;
            for (int j = 0; j < list.size(); j++) {
                node = (Element) list.get(j);
                attr = node.attributes();
                for (Attribute e : attr) {
                    if ("bounds".equals(e.getName())) {
                        bounds = e.getValue();
                        tmpStr = bounds.replace("[", "")
                                .replace("]", ",");
                        tmp = tmpStr.substring(0, tmpStr.length() - 1)
                                .split(",");

                        //CaptureGUI.textArea_log.append("bounds[" + tmp[0] + "," + tmp[1] + "," + tmp[2] + "," + tmp[3] + "]\n");
                    }
                }
                boundsList.add(tmp);

                //xp = list.get(j).getPath();
                //CaptureGUI.textArea_log.append("HandleElement list.size=" + xp + "\n");
                //clickNavigationBar(driver, xp);
            }
            processData();
            CaptureGUI.textArea_log.append("indexes[" + indexes.size() + "]\n");
            if (list.size() > 0) {
                xp1 = list.get(0).getPath() + "[" + indexes.get(0) + "]";
            }
            for (int k = 0; k < list.size(); k++) {
                xp = list.get(k).getPath() + "[" + indexes.get(k) + "]";
                CaptureGUI.textArea_log.append("HandleElement Xp=" + xp + "\n");
                if (failcount > 10) {
                    break;
                }
                clickNavigationBar(driver, xp);
            }
        }
    }
}

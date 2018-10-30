package optimizationprogram.mainPro.welcomePage;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import optimizationprogram.GUICode.CaptureGUI;
import optimizationprogram.mainPro.services.XPathConstant;
import optimizationprogram.tools.GotoSleep;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.List;

public class HandleWelcomePage {
    private List<Node> privilege_btn_list;
    private List<Node> agreement_btn_list;
    private List<Node> shortcut_hasDescription_list;
    private List<Node> shortcut_noDescription_list;
    private List<Node> cancel_list;
    private List<Node> other_btn_list;
    private Document document;
    private List<Node> ele_clickable_list;
    private Integer swipe_count = 0;
    private Integer click_count = 0;
    private String xmlStr = "";
    private Integer width = 0;
    private Integer height = 0;
    private Integer status = 0;

    private List<Node> linearlayout_list;
    private List<Node> framelayout_list;
    private List<Node> relativelayout_list;
    private List<Node> imageview_list;
    private List<Node> textview_list;
    private List<Node> button_list;
    private List<Node> imagebutton_list;
    private List<Node> viewgroup_list;
    private List<Node> radiobutton_list;
    private List<Node> actionbar_list;
    private List<Node> edittext_list;

    private GotoSleep sleep = new GotoSleep();

    public HandleWelcomePage() {
    }

    /**
     * 滑动窗口
     */
    public void swipWindow(AndroidDriver driver) {
        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
        try {
            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
            swipe_count++;
            CaptureGUI.textArea_log.append("INFO:已向左滑动" + swipe_count + "次！\n");
        } catch (Exception e) {
            CaptureGUI.textArea_log.append("ERROR:滑动屏幕时出错！\n");
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有其他类型的弹窗
     * 例如版本升级
     */
    public Integer handleOtherDialogue(AndroidDriver driver) {
        if (xmlStr.contains("升级")
                || xmlStr.contains("新版本")
                || xmlStr.contains("下载")
                || xmlStr.contains("更新")) {
            cancel_list = document.selectNodes(XPathConstant.CANCEL_BTN_XPATH);
            if (cancel_list.size() > 0) {
                try {
                    driver.findElement(By.xpath(XPathConstant.CANCEL_BTN_XPATH)).click();
                } catch (Exception e1) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e1.printStackTrace();
                }
                return 1;
            }
            cancel_list = document.selectNodes(XPathConstant.CANCEL_TEXTVIEW_XPATH);
            if (cancel_list.size() > 0) {
                try {
                    driver.findElement(By.xpath(XPathConstant.CANCEL_TEXTVIEW_XPATH)).click();
                } catch (Exception e1) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e1.printStackTrace();
                }
                return 1;
            }
            return 0;
        } else {
            Element node;
            List<Attribute> attr;
            other_btn_list = document.selectNodes(XPathConstant.OTHER_DIALOGUE_XPATH);
            //CaptureGUI.textArea_log.append("other_btn_list.size=" + other_btn_list.size() + "\n");
            if (other_btn_list.size() > 0) {
                OUT:
                for (int i = 0; i < other_btn_list.size(); i++) {
                    node = (Element) (other_btn_list.get(i));
                    attr = node.attributes();
                    for (Attribute e : attr) {
                        if ("text".equals(e.getName())) {
                            if (e.getValue().length() < 8) {
                                if ("确定".equals(e.getValue())
                                        || "是".equals(e.getValue())
                                        || "好的".equals(e.getValue())
                                        || "接受".equals(e.getValue())
                                        || e.getValue().contains("知道")
                                        || e.getValue().contains("下一步")
                                        || e.getValue().contains("北京")) {
                                    if (e.getValue().length() < 8) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                            break OUT;
                                        } catch (Exception e1) {
                                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                                if (e.getValue().contains("设置")) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                    }
                                    sleep.startSleep(3 * 1000);
                                    CaptureGUI.textArea_log.append("点击返回。。。" + "\n");
                                    driver.pressKeyCode(AndroidKeyCode.BACK);
                                    break OUT;
                                }
                            }
                        }
                        if ("content-desc".equals(e.getName())) {
                            if ("确定".equals(e.getValue())
                                    || "是".equals(e.getValue())
                                    || "好的".equals(e.getValue())
                                    || "接受".equals(e.getValue())
                                    || e.getValue().contains("知道")
                                    || e.getValue().contains("下一步")
                                    || e.getValue().contains("北京")) {
                                if (e.getValue().length() < 8) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                    }
                                    break OUT;
                                }
                            }
                            if (e.getValue().contains("设置")) {
                                try {
                                    driver.findElement(By.xpath(node.getUniquePath())).click();
                                } catch (Exception e1) {
                                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                    e1.printStackTrace();
                                }
                                sleep.startSleep(3 * 1000);
                                CaptureGUI.textArea_log.append("点击返回。。。" + "\n");
                                driver.pressKeyCode(AndroidKeyCode.BACK);
                                break OUT;
                            }
                        }
                    }
                }
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void clickElements(List<Node> list, AndroidDriver driver) {
        if (list == null) {
            return;
        }
        Element node;
        List<Attribute> attr;
        OUT:
        for (int i = 0; i < list.size(); i++) {
            node = (Element) list.get(i);
            attr = node.attributes();
            for (Attribute e : attr) {
                if ("resource-id".equals(e.getName())) {
                    if (e.getValue().contains("close")
                            || e.getValue().contains("jump")) {
                        try {
                            driver.findElement(By.xpath(node.getUniquePath())).click();
                        } catch (Exception e1) {
                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                            e1.printStackTrace();
                        }
                        break OUT;
                    } else {
                        if (i == list.size() - 1) {
                            try {
                                driver.findElement(By.xpath(list.get(i).getPath())).click();
                            } catch (Exception e1) {
                                CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                e1.printStackTrace();
                            }
                            break OUT;
                        }
                    }
                }
            }
        }
    }

    public Integer handleNoDescriptionElement(AndroidDriver driver) {
        String xpath= "";
        Element node;
        List<Attribute> attr;
        shortcut_noDescription_list = document.selectNodes(XPathConstant.ENTRY_NULL_XPATH);
        if (shortcut_noDescription_list.size() == 1) {
            xpath = shortcut_noDescription_list.get(0).getPath();
            shortcut_noDescription_list = document.selectNodes(xpath);
            if (shortcut_noDescription_list.size() >= 1){
                OUT:
                for (int i=0;i<shortcut_noDescription_list.size();i++){
                    node = (Element) shortcut_noDescription_list.get(i);
                    attr = node.attributes();
                    for (Attribute e : attr) {
                        if ("clickable".equals(e.getName())){
                            if ("true".equals(e.getValue())){
                                try {
                                    driver.findElement(By.xpath(node.getUniquePath())).click();
                                } catch (Exception e1) {
                                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                    e1.printStackTrace();
                                }
                                break OUT;
                            }
                        }
                    }
                }
            }
            return 1;
        } else {
            clickElements(shortcut_hasDescription_list, driver);
            return 1;
        }
        /*shortcut_noDescription_list = document.selectNodes(XPathConstant.ENTRY_NULL_BTN_XPATH);
        if (shortcut_noDescription_list.size() > 0 && shortcut_noDescription_list.size() <= 3) {
            clickElements(shortcut_hasDescription_list, driver);
            return 1;
        }
        shortcut_noDescription_list = document.selectNodes(XPathConstant.ENTRY_NULL_IMAGEVIEW_XPATH);
        if (shortcut_noDescription_list.size() > 0 && shortcut_noDescription_list.size() <= 3) {
            clickElements(shortcut_hasDescription_list, driver);
            return 1;
        }
        shortcut_noDescription_list = document.selectNodes(XPathConstant.ENTRY_NULL_TEXTVIEW_XPATH);
        if (shortcut_noDescription_list.size() > 0 && shortcut_noDescription_list.size() <= 3) {
            clickElements(shortcut_hasDescription_list, driver);
            return 1;
        }
        return 0;*/
    }

    /**
     * 判断是否有快捷键
     * 一般包含"跳过"、"体验"、"开启"、"逛一逛"、"看看"等字段
     */
    public Integer handleShortcut(AndroidDriver driver) {
        //shortcut_hasDescription_list = document.selectNodes(XPathConstant.ENTRY_BTN_XPATH);
        Element node;
        List<Attribute> attr;
        if (ele_clickable_list.size() >= 1) {
            OUT:
            for (int i = 0; i < ele_clickable_list.size(); i++) {
                node = (Element) ele_clickable_list.get(i);
                attr = node.attributes();
                for (Attribute e : attr) {
                    if ("text".equals(e.getName())) {
                        if (e.getValue().length() < 8) {
                            if ("跳过".equals(e.getValue())
                                    || e.getValue().contains("进入")
                                    || e.getValue().contains("体验")
                                    || e.getValue().contains("开启")
                                    || e.getValue().contains("开始")
                                    || e.getValue().contains("随便")
                                    || e.getValue().contains("先")
                                    || e.getValue().contains("逛")
                                    || e.getValue().contains("看看")) {
                                try {
                                    driver.findElement(By.xpath(node.getUniquePath())).click();
                                } catch (Exception e1) {
                                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                    e1.printStackTrace();
                                }
                                break OUT;
                            }
                        }
                    }
                    if ("content-desc".equals(e.getName())) {
                        if ("跳过".equals(e.getValue())
                                || e.getValue().contains("进入")
                                || e.getValue().contains("体验")
                                || e.getValue().contains("开启")
                                || e.getValue().contains("开始")
                                || e.getValue().contains("随便")
                                || e.getValue().contains("先")
                                || e.getValue().contains("逛")
                                || e.getValue().contains("看看")) {
                            if (e.getValue().length() < 8) {
                                try {
                                    driver.findElement(By.xpath(node.getUniquePath())).click();
                                } catch (Exception e1) {
                                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                    e1.printStackTrace();
                                }
                                break OUT;
                            }
                        }
                    }
                    if ("resource-id".equals(e.getName())) {
                        if (e.getValue().contains("button")
                                || e.getValue().contains("Button")) {
                            try {
                                driver.findElement(By.xpath(node.getUniquePath())).click();
                            } catch (Exception e1) {
                                CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                e1.printStackTrace();
                            }
                            break OUT;
                        }
                    }
                }
            }
            return 1;
        }
        return 1;
    }

    /**
     * 判断处理协议弹窗
     * 一些app打开后会弹出使用协议
     * 一般包含"同意""使用"等字段
     */
    public Integer handleAgreement(AndroidDriver driver) {

        if (xmlStr.contains("协议")
                || xmlStr.contains("隐私")
                || xmlStr.contains("政策")) {
            Element node;
            List<Attribute> attr;
            agreement_btn_list = document.selectNodes(XPathConstant.OTHER_DIALOGUE_XPATH);
            //CaptureGUI.textArea_log.append(" agreement_btn_list=" + agreement_btn_list.size() + "\n");
            if (agreement_btn_list.size() > 0) {
                OUT:
                for (int i = 0; i < agreement_btn_list.size(); i++) {
                    node = (Element) (agreement_btn_list.get(i));
                    attr = node.attributes();
                    for (Attribute e : attr) {
                        if ("text".equals(e.getName())) {
                            if (e.getValue().length() < 8) {
                                if (("使用").equals(e.getValue())
                                        || "同意".equals(e.getValue())
                                        || "接受".equals(e.getValue())
                                        || "同意并使用".equals(e.getValue())
                                        || "已阅读并同意".equals(e.getValue())
                                        || "同意并授权".equals(e.getValue())
                                        || "确定".equals(e.getValue())
                                        || e.getValue().contains("下一步")
                                        || e.getValue().contains("知道")) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                    }
                                    break OUT;
                                }
                            }

                            //click_count = click_count + 1;
                        }
                        if ("content-desc".equals(e.getName())) {
                            if (("使用").equals(e.getValue())
                                    || "同意".equals(e.getValue())
                                    || "接受".equals(e.getValue())
                                    || "同意并使用".equals(e.getValue())
                                    || "同意并授权".equals(e.getValue())
                                    || "已阅读并同意".equals(e.getValue())
                                    || "确定".equals(e.getValue())
                                    || e.getValue().contains("下一步")
                                    || e.getValue().contains("知道")) {
                                try {
                                    driver.findElement(By.xpath(node.getUniquePath())).click();
                                } catch (Exception e1) {
                                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                    e1.printStackTrace();
                                }
                                break OUT;
                            }
                        }
                    }
                }
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }


    /**
     * 判断并处理权限弹窗，xml包含"权限"或者"允许"，
     * 情况1：只有一个按钮，按钮一般为"确定"
     * 情况2：有两个按钮"允许"和"取消"
     * 情况3：
     */

    public Integer handlePrivilege(AndroidDriver driver) {
        //if (xmlStr.contains("权限") && ele_clickable_list.size() > 0) {
        if (xmlStr.contains("要允许")
                || xmlStr.contains("定位")
                || xmlStr.contains("地理位置")
                || xmlStr.contains("权限")
                || xmlStr.contains("读取")) {
            //按类别处理
            if (button_list.size() == 1) {
                try {
                    driver.findElement(By.xpath(XPathConstant.BUTTON_PATH)).click();
                } catch (Exception e1) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e1.printStackTrace();
                    //return -1;
                }
            }
            if (textview_list.size() == 1) {
                try {
                    driver.findElement(By.xpath(XPathConstant.TEXTVIEW_PATH)).click();
                } catch (Exception e1) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e1.printStackTrace();
                    //return -1;
                }
            }
            if (button_list.size() == 2) {
                try {
                    driver.findElement(By.xpath(XPathConstant.PRIVILEGE_BTN_XPATH)).click();
                } catch (Exception e1) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e1.printStackTrace();
                    //return -1;
                }
            }
            if (textview_list.size() == 2) {
                try {
                    driver.findElement(By.xpath(XPathConstant.PRIVILEGE_TEXTVIEW_XPATH)).click();
                } catch (Exception e1) {
                    CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                    e1.printStackTrace();
                    //return -1;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    public Boolean jumpToMainPage(AndroidDriver driver) {
        sleep.startSleep(5 * 1000);
        /**
         * 1.获取当前页面xml
         */
        try {
            //System.out.println(driver.getContext());
            xmlStr = driver.getPageSource();
        } catch (Exception e) {
            CaptureGUI.textArea_log.append("ERROR:获取界面元素时出错！\n");
            e.printStackTrace();
            return false;
        }
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            CaptureGUI.textArea_log.append("ERROR:字符串转换文本失败！\n");
            e.printStackTrace();
            return false;
        }
        /**
         * 2.获取界面可点击的元素
         */
        ele_clickable_list = document.selectNodes(XPathConstant.ELE_CLICKABLE_XPATH);

        linearlayout_list = document.selectNodes(XPathConstant.LINEARLAYOUT_PATH);
        framelayout_list = document.selectNodes(XPathConstant.FRAMELAYOUT_PATH);
        relativelayout_list = document.selectNodes(XPathConstant.RELATIVELAYOUT_PATH);
        imageview_list = document.selectNodes(XPathConstant.IMAGEVIEW_PATH);
        textview_list = document.selectNodes(XPathConstant.TEXTVIEW_PATH);
        button_list = document.selectNodes(XPathConstant.BUTTON_PATH);
        imagebutton_list = document.selectNodes(XPathConstant.IMAGEBUTTON_PATH);
        viewgroup_list = document.selectNodes(XPathConstant.VIEWGROUP_PATH);
        radiobutton_list = document.selectNodes(XPathConstant.RADIOBUTTON_PATH);
        actionbar_list = document.selectNodes(XPathConstant.ACTIONBAR_PATH);
        edittext_list = document.selectNodes(XPathConstant.EDITTEXT_PATH);

        //CaptureGUI.textArea_log.append("ele_clickable_list.size" + ele_clickable_list.size() + "\n");
        //if (ele_clickable_list.size() < 6) {
        if (linearlayout_list.size() <= 3
                && framelayout_list.size() <= 3
                && relativelayout_list.size() <= 3
                && imageview_list.size() <= 3
                && textview_list.size() <= 3
                && viewgroup_list.size() <= 1
                && radiobutton_list.size() <= 3
                && actionbar_list.size() <= 3
                && edittext_list.size() == 0) {
            /**
             * 3.界面无可点击元素，向左滑动
             */
            //CaptureGUI.textArea_log.append("imageview_list.size()" + imageview_list.size() + "\n");
            //CaptureGUI.textArea_log.append("textview_list.size()" + textview_list.size() + "\n");
            //CaptureGUI.textArea_log.append("button_list.size()" + button_list.size() + "\n");
            //if (ele_clickable_list.size() == 0) {
            if (imageview_list.size() <= 5
                    && textview_list.size() == 0
                    && button_list.size() == 0
                    && swipe_count <= 6) {
                swipWindow(driver);
            } else {
                /**
                 * 4.处理权限弹窗
                 */
                status = handlePrivilege(driver);
                if (1 == status) {
                    click_count++;
                    CaptureGUI.textArea_log.append("INFO:权限弹窗处理完毕！\n");
                } else {
                    CaptureGUI.textArea_log.append("ERROR:处理权限弹窗出错！\n");
                }

                /**
                 * 5.处理快捷键
                 */
                status = handleShortcut(driver);
                if (1 == status) {
                    click_count++;
                    CaptureGUI.textArea_log.append("INFO:快捷键处理完毕！\n");
                } else {
                    CaptureGUI.textArea_log.append("ERROR:处理快捷键出错！将进行滑动操作...\n");
                    swipWindow(driver);
                }

                /**
                 * 6.处理协议弹窗
                 */
                status = handleAgreement(driver);
                if (1 == status) {
                    click_count++;
                    CaptureGUI.textArea_log.append("INFO:协议弹窗处理完毕！\n");
                } else {
                    CaptureGUI.textArea_log.append("ERROR:处理协议弹窗出错！\n");
                }
                /**
                 * 7.处理其他弹窗
                 */
                status = handleOtherDialogue(driver);
                if (1 == status) {
                    click_count++;
                    CaptureGUI.textArea_log.append("INFO:其他类型弹窗处理完毕！\n");
                } else {
                    CaptureGUI.textArea_log.append("ERROR:处理其他类型弹窗出错！\n");
                }
                /**
                 * 用resource_id处理无明显文字标志的快捷键和弹窗
                 */
                status = handleNoDescriptionElement(driver);
                if (1 == status) {
                    click_count++;
                    CaptureGUI.textArea_log.append("INFO:广告弹窗处理完毕！\n");
                } else {
                    CaptureGUI.textArea_log.append("ERROR:处理广告弹窗出错！\n");
                }
            }
            sleep.startSleep(5 * 1000);
            if (swipe_count > 20 || click_count > 20) {
                //CaptureGUI.textArea_log.append("跳出" + click_count + "\n");
                return false;
            }
            jumpToMainPage(driver);
        }
        return true;
    }
}

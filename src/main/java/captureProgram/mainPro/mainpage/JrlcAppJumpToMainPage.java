package captureProgram.mainPro.mainpage;


import captureProgram.GUICode.CaptureGUI;
import captureProgram.mainPro.services.XPathConstant;
import io.appium.java_client.android.AndroidDriver;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.List;

/**
 * 创建人：郝京
 * <p>
 * 创建日期：2018-03-26
 * <p>
 * 文件描述：金融理财类App
 */
public class JrlcAppJumpToMainPage {
    Document document  ;
    //List<Node> list ;
    Integer flag1 = 0;
    Integer flag2 = 0;
    Integer flag3 = 0;
    String xmlStr = "";
    List<Node> LinearLayout_list;
    List<Node> FrameLayout_list;
    List<Node> RelativeLayout_list;
    List<Node> ImageView_list;
    List<Node> TextView_list;
    List<Node> Button_list;
    List<Node> EDITTEXT_list;
    Integer width = 0;
    Integer height = 0;

    public JrlcAppJumpToMainPage() {
    }

    public Boolean jumpToMainPage(AndroidDriver driver) {
        if (flag3 < 9) {
            try {
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
            //初步判断
            LinearLayout_list = document.selectNodes(XPathConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(XPathConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(XPathConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(XPathConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(XPathConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(XPathConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(XPathConstant.EDITTEXT_PATH);
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 5) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        CaptureGUI.textArea_log.append("ERROR:滑动屏幕时出错！\n");
                        e.printStackTrace();
                    }
                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    jumpToMainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(XPathConstant.BUTTON_PATH);

                    if (Button_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < Button_list.size(); i++) {
                            node = (Element) (Button_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if ("允许".equals(e.getValue())
                                            || "同意".equals(e.getValue())
                                            || "取消".equals(e.getValue())
                                            || "确认".equals(e.getValue())
                                            || e.getValue().contains("使用")
                                            || "同意并继续".equals(e.getValue())
                                            || "暂不升级".equals(e.getValue())
                                            || e.getValue().contains("开始")
                                            || "跳过".equals(e.getValue())
                                            || e.getValue().contains("体验")) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        jumpToMainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        jumpToMainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(XPathConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if (e.getValue().contains("跳过")) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        jumpToMainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 5) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exc) {
                                            CaptureGUI.textArea_log.append("ERROR:滑动界面时出错！\n");
                                            exc.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(5 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        jumpToMainPage(driver);
                                    }
                                }
                            }
                        }
                    }

                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(XPathConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;

                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if ("立即进入".equals(e.getValue())
                                            || "进入".equals(e.getValue())
                                            || "跳过".equals(e.getValue())
                                            || "同意".equals(e.getValue())
                                            || e.getValue().contains("逛逛")
                                            || e.getValue().contains("开启")
                                            || "下次再说".equals(e.getValue())
                                            || "始终允许".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        jumpToMainPage(driver);
                                    }
                                    if (TextView_list.size() == 1 && "".equals(e.getValue())) {

                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        jumpToMainPage(driver);
                                    }
                                }
                            }
                        }


                    }
                    ImageView_list = document.selectNodes(XPathConstant.IMAGEVIEW_PATH);
                    RelativeLayout_list = document.selectNodes(XPathConstant.RELATIVELAYOUT_PATH);
                    LinearLayout_list = document.selectNodes(XPathConstant.LINEARLAYOUT_PATH);
                    if (ImageView_list.size() == 1
                            && RelativeLayout_list.size() == 0
                            && LinearLayout_list.size() == 0) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    jumpToMainPage(driver);
                                }
                            }
                        }
                    }
                    //RelativeLayout_list = document.selectNodes(XPathConstant.RELATIVELAYOUT_PATH);
                    if (ImageView_list.size() == 0
                            && RelativeLayout_list.size() == 1
                            && LinearLayout_list.size() == 0) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (RelativeLayout_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    jumpToMainPage(driver);
                                }
                            }
                        }
                    }
                    //LinearLayout_list = document.selectNodes(XPathConstant.LINEARLAYOUT_PATH);
                    if (ImageView_list.size() < 2
                            && RelativeLayout_list.size() < 2
                            && LinearLayout_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (LinearLayout_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        CaptureGUI.textArea_log.append("ERROR:未找到界面元素！\n");
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    jumpToMainPage(driver);
                                }
                            }
                        }
                    }

                }

            }
        }

        return true;
    }
}

package capture.control;

import capture.constant.CONFIGConstant;
import io.appium.java_client.android.AndroidDriver;
import org.dom4j.*;
import org.openqa.selenium.By;

import java.util.List;

/**
 * 编写人： 郝京
 * 编写日期： 20180201
 * 功能描述：非游戏类App进入主界面操作
 * <p>
 * 修改日期：20180205
 */
public class TurnToMainPage {
    Document document = null;
    Integer flag1 = 0;
    Integer flag2 = 0;
    Integer flag3 = 0;
    String xmlStr = null;
    List<Node> LinearLayout_list;
    List<Node> FrameLayout_list;
    List<Node> RelativeLayout_list;
    List<Node> ImageView_list;
    List<Node> TextView_list;
    List<Node> Button_list;
    List<Node> EDITTEXT_list;
    List<Node> LinearLayout_Textview_list;
    List<Node> ImageButton_list;
    Integer width = 0;
    Integer height = 0;

    /**
     * 影音视听类App
     *
     * @param driver
     * @return
     */
    public Boolean YYST_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    YYST_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("使用")
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YYST_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YYST_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YYST_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    YYST_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            ) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YYST_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 金融理财类App
     *
     * @param driver
     * @return
     */

    public Boolean JRLC_MainPage(AndroidDriver driver) {
        if (flag3 < 9) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);
            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
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
                        e.printStackTrace();
                    }
                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    JRLC_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JRLC_MainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JRLC_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
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
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JRLC_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 5) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exc) {
                                            exc.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(5 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JRLC_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }

                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
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
                                            || "下次再说".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JRLC_MainPage(driver);
                                    }
                                    if (TextView_list.size() == 1 && "".equals(e.getValue())) {

                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JRLC_MainPage(driver);
                                    }
                                }
                            }
                        }


                    }
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
                    LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    JRLC_MainPage(driver);
                                }
                            }
                        }
                    }
                    //RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    JRLC_MainPage(driver);
                                }
                            }
                        }
                    }
                    //LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    JRLC_MainPage(driver);
                                }
                            }
                        }
                    }

                }

            }
        }

        return true;
    }

    /**
     * 时尚购物类App
     *
     * @param driver
     * @return
     */

    public Boolean SSGW_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);
            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    //&& TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度

                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    SSGW_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("使用")
                                            || "取消".equals(e.getValue())
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(5 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SSGW_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(5 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SSGW_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exc) {
                                            exc.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(5 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SSGW_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }

                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
                    LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
                    if (ImageView_list.size() == 1
                            && RelativeLayout_list.size() == 0
                            && LinearLayout_list.size() == 0) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    SSGW_MainPage(driver);
                                }
                            }
                        }
                    }
                    //RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    SSGW_MainPage(driver);
                                }
                            }
                        }
                    }
                    //LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    YYST_MainPage(driver);
                                }
                            }
                        }
                    }

                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("逛逛")
                                            || "跳过".equals(e.getValue())
                                            || "北京市".equals(e.getValue())
                                            || e.getValue().contains("再说")
                                            ) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(5 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SSGW_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return true;
    }

    /**
     * 新闻资讯类App
     */
    public Boolean XWZX_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    XWZX_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("进入")
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())
                                            || "是".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        XWZX_MainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 2 && i == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        XWZX_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        XWZX_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }

                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        XWZX_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    XWZX_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            || e.getValue().contains("再说")) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        XWZX_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 居家生活类app
     *
     * @param driver
     * @return
     */
    public Boolean JJSH_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() <= 4
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    JJSH_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("进入")
                                            || e.getValue().contains("开启")
                                            || "确认".equals(e.getValue())
                                            || e.getValue().contains("继续")
                                            || "是".equals(e.getValue())
                                            || "确定".equals(e.getValue())
                                            || "取消".equals(e.getValue())
                                            || e.getValue().contains("再说")
                                            || "跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JJSH_MainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 2 && i == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JJSH_MainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JJSH_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JJSH_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }

                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JJSH_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    JJSH_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            || e.getValue().contains("再说")
                                            || e.getValue().contains("随便")
                                            || "同意".equals(e.getValue())
                                            || e.getValue().contains("知道")) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        JJSH_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 旅行交通类App
     *
     * @param driver
     * @return
     */

    public Boolean LXJT_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    LXJT_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("进入")
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())
                                            || "是".equals(e.getValue())
                                            || e.getValue().contains("体验")
                                            || e.getValue().contains("知道")
                                            || "确定".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 2 && i == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                    if ("".equals(e.getValue()) && Button_list.size() == 1) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }

                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    } else {
                                        node = (Element) (ImageView_list.get(0));
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    LXJT_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            || e.getValue().contains("再说")
                                            || e.getValue().contains("开启")
                                            || "同意".equals(e.getValue())
                                            || "了解更多".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                    if (TextView_list.size() == 1) {

                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        LXJT_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    LinearLayout_Textview_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_TEXTVIEW_PATH);
                    if (LinearLayout_Textview_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (LinearLayout_Textview_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("text".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("跳过".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getParent().getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    LXJT_MainPage(driver);
                                }
                            }
                        }
                    }

                }
            }
        }

        return true;
    }

    /**
     * 图书阅读类App
     *
     * @param driver
     * @return
     */
    public Boolean TSYD_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() <= 1
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    TSYD_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("使用")
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())
                                            || "跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        TSYD_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        TSYD_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        TSYD_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    TSYD_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            || e.getValue().contains("随便")
                                            || e.getValue().contains("逛逛")
                                            || e.getValue().contains("再说")) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        TSYD_MainPage(driver);
                                    }
                                    if (TextView_list.size() == 1 && !e.getValue().contains("协议")) {

                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(8 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        TSYD_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    ImageButton_list = document.selectNodes(CONFIGConstant.IMAGEBUTTON_PATH);
                    if (ImageButton_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageButton_list.get(ImageButton_list.size() - 1));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("text".equals(e.getName())) {
                                if (!e.getValue().contains("拒绝")
                                        && !e.getValue().contains("不同意")
                                        && !e.getValue().contains("退出")
                                        ) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    TSYD_MainPage(driver);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 医疗健康类App
     *
     * @param driver
     * @return
     */
    public Boolean YLJK_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    YLJK_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("使用")
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())
                                            || "确定".equals(e.getValue())
                                            || "取消".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YLJK_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YLJK_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YLJK_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    YLJK_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            || e.getValue().contains("随便")
                                            || e.getValue().contains("再说")
                                            ) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        YLJK_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }

                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
                    LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    YLJK_MainPage(driver);
                                }
                            }
                        }
                    }
                    //RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    YLJK_MainPage(driver);
                                }
                            }
                        }
                    }
                    //LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(8 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    YLJK_MainPage(driver);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 摄影摄像类App
     *
     * @param driver
     * @return
     */
    public Boolean SYSX_MainPage(AndroidDriver driver) {
        if (flag3 < 8) {
            try {
                xmlStr = driver.getPageSource();
                System.out.println(xmlStr);
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
            //初步判断
            LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
            FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
            RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
            ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
            TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
            Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);
            EDITTEXT_list = document.selectNodes(CONFIGConstant.EDITTEXT_PATH);

            //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
            //非主界面
            if (LinearLayout_list.size() < 3
                    && FrameLayout_list.size() < 3
                    && RelativeLayout_list.size() < 3
                    && ImageView_list.size() < 3
                    && TextView_list.size() < 3
                    && EDITTEXT_list.size() == 0) {
                //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            /*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             */
                if (ImageView_list.size() == 0
                        && TextView_list.size() == 0
                        && Button_list.size() == 0
                        && flag2 <= 4) {
                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                    try {
                        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    flag2 = flag2 + 1;
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    SYSX_MainPage(driver);
                } else {
                    //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                    Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

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
                                            || e.getValue().contains("使用")
                                            || "确认".equals(e.getValue())
                                            || "同意并继续".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SYSX_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //“跳过”快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    //System.out.println("跳过:" + list.size());
                    if (ImageView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < ImageView_list.size(); i++) {
                            node = (Element) (ImageView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("content-desc".equals(e.getName())) {
                                    if ("跳过".equals(e.getValue())) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SYSX_MainPage(driver);
                                    }
                                    //有可点击元素，可点击元素无文字描述，进行滑动处理
                                    if ("".equals(e.getValue()) && flag1 <= 4) {
                                        width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                        height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                        try {
                                            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                        } catch (Exception exe) {
                                            exe.printStackTrace();
                                        }
                                        flag1 = flag1 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SYSX_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                    //快捷键处理
                    ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
                    if (ImageView_list.size() == 1) {
                        Element node;
                        List<Attribute> attr;
                        node = (Element) (ImageView_list.get(0));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("content-desc".equals(e.getName())) {
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    flag3 = flag3 + 1;
                                    try {
                                        Thread.sleep(6 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    SYSX_MainPage(driver);
                                }
                            }
                        }
                    }
                    //非登陆注册按钮处理（一）
                    TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                    if (TextView_list.size() > 0) {
                        Element node;
                        List<Attribute> attr;
                        for (int i = 0; i < TextView_list.size(); i++) {
                            node = (Element) (TextView_list.get(i));
                            attr = node.attributes();
                            for (Attribute e : attr) {
                                if ("text".equals(e.getName())) {
                                    if (e.getValue().contains("体验")
                                            || e.getValue().contains("进入")
                                            || "跳过".equals(e.getValue())
                                            ) {
                                        try {
                                            driver.findElement(By.xpath(node.getUniquePath())).click();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            return false;
                                        }
                                        flag3 = flag3 + 1;
                                        try {
                                            Thread.sleep(6 * 1000);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        SYSX_MainPage(driver);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    /*public Boolean toMainForm(AndroidDriver driver) {
        try {
            xmlStr = driver.getPageSource();
            System.out.println(xmlStr);
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
        //初步判断
        LinearLayout_list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_PATH);
        FrameLayout_list = document.selectNodes(CONFIGConstant.FRAMELAYOUT_PATH);
        RelativeLayout_list = document.selectNodes(CONFIGConstant.RELATIVELAYOUT_PATH);
        ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
        TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
        Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

        //System.out.println(list1.size() + "    " + list2.size() + "    " + list3.size());
        //非主界面
        if (LinearLayout_list.size() < 3
                && FrameLayout_list.size() < 3
                && RelativeLayout_list.size() < 3
                && ImageView_list.size() < 3
                && TextView_list.size() < 3) {
            //System.out.println(ImageView_list.size() + "    " + TextView_list.size() + "    " + Button_list.size());
            *//*
                若界面无任何可点击的元素，则向左滑动。否则进入可点击元素处理。
             *//*
            if (ImageView_list.size() == 0
                    && TextView_list.size() == 0
                    && Button_list.size() == 0
                    && flag2 <= 4) {
                width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                flag2 = flag2 + 1;
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                toMainForm(driver);
            } else {
                //权限弹窗或协议弹窗，一般情况需要点“允许”、“同意”、“同意使用”按钮
                Button_list = document.selectNodes(CONFIGConstant.BUTTON_PATH);

                if (Button_list.size() > 0) {
                    Element node;
                    List<Attribute> attr;
                    for (int i = 0; i < Button_list.size(); i++) {
                        node = (Element) (Button_list.get(i));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("text".equals(e.getName())) {
                                if (e.getValue().contains("允许")
                                        || e.getValue().contains("同意")
                                        && !"不同意".equals(e.getValue())
                                        || "取消".equals(e.getValue())
                                        || "确认".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }

                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                            }
                            //该种情况很少出现
                            if ("content-desc".equals(e.getName())) {
                                if (e.getValue().contains("允许")
                                        || e.getValue().contains("同意")
                                        && !"不同意".equals(e.getValue())
                                        || "取消".equals(e.getValue())
                                        || "确认".equals(e.getValue())) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }

                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                            }
                        }
                    }
                }
                //“跳过”快捷键处理
                ImageView_list = document.selectNodes(CONFIGConstant.IMAGEVIEW_PATH);
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
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                                //有可点击元素，可点击元素无文字描述，进行滑动处理
                                if ("".equals(e.getValue()) && flag1 <= 4) {
                                    width = driver.manage().window().getSize().width;//获取当前屏幕的宽度
                                    height = driver.manage().window().getSize().height;//获取当前屏幕的高度
                                    driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2, 200);
                                    flag1 = flag1 + 1;
                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                                //- -
                                if ("".equals(e.getValue()) && ImageView_list.size() == 1 && flag3 <= 4) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                        flag3 = flag3 + 1;
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                            }
                        }
                    }
                }
                //非登陆注册按钮处理（一）
                TextView_list = document.selectNodes(CONFIGConstant.TEXTVIEW_PATH);
                if (TextView_list.size() > 0) {
                    Element node;
                    List<Attribute> attr;
                    for (int i = 0; i < TextView_list.size(); i++) {
                        node = (Element) (TextView_list.get(i));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("text".equals(e.getName())) {
                                if (!e.getValue().contains("登陆") && !e.getValue().contains("注册")
                                        || e.getValue().contains("立即体验") || e.getValue().contains("逛逛")
                                        ) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                            }
                        }
                    }
                }
                //非登陆注册按钮处理（二）
                list = document.selectNodes(CONFIGConstant.LINEARLAYOUT_TEXTVIEW_PATH);
                if (list.size() > 0) {
                    Element node;
                    List<Attribute> attr;
                    for (int i = 0; i < list.size(); i++) {
                        node = (Element) (list.get(i));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("text".equals(e.getName())) {
                                if (e.getValue().contains("逛逛") || e.getValue().contains("我知道了")) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                            }
                        }
                    }
                }
                //非登陆注册按钮处理（三）
                list = document.selectNodes(CONFIGConstant.ELATIVELAYOUT_TEXTVIEW_PATH);
                if (list.size() > 0) {
                    Element node;
                    List<Attribute> attr;
                    for (int i = 0; i < list.size(); i++) {
                        node = (Element) (list.get(i));
                        attr = node.attributes();
                        for (Attribute e : attr) {
                            if ("text".equals(e.getName())) {
                                if (e.getValue().contains("进入") || e.getValue().contains("进入首页")) {
                                    try {
                                        driver.findElement(By.xpath(node.getUniquePath())).click();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                        return false;
                                    }
                                    try {
                                        Thread.sleep(5 * 1000);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                    toMainForm(driver);
                                }
                            }
                        }
                    }
                }
            }

        }
        return true;
    }*/
}

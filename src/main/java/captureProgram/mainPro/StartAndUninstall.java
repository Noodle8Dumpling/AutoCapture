package captureProgram.mainPro;

import captureProgram.GUICode.CaptureGUI;
import captureProgram.mainPro.mainpage.*;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StartAndUninstall extends Thread {

    AndroidDriver driver;
    String btnXp;
    String appName;
    String appType;
    String appSize;
    String appMarket;
    List<String> pkgList;
    YystAppJumpToMainPage yyst_main;
    JrlcAppJumpToMainPage jrlc_main;
    SsgwAppJumpToMainPage ssgw_main;
    XwzxAppJumpToMainPage xwzx_main;
    JjshAppJumpToMainPage jjsh_main;
    LxjtAppJumpToMainPage lxjt_main;
    TsydAppJumpToMainPage tsyd_main;
    YljkAppJumpToMainPage yljk_main;
    SysxAppJumpToMainPage sysx_main;

    public StartAndUninstall(AndroidDriver driver, SelectedAppInfo sai) {
        this.driver = driver;
        this.btnXp = sai.getOpenXPath();
        this.appName = sai.getAppName();
        this.appType = sai.getAppType();
        this.appSize = sai.getAppSize();
    }

    @Override
    public void run() {
        if (!CaptureGUI.stopFlag) {
            driver.quit();
            return;
        }
        //启动应用 点击“打开按钮”
        if (StringUtils.isNotEmpty(btnXp)) {
            try {
                CaptureGUI.textArea_log.append("INFO:正在打开应用···\n");
                driver.findElementByXPath(btnXp).click();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (appType.contains("，")) {
            appType = appType.split("，")[0];
            System.out.println(appType);
            appMarket = "华为";
        }
        if (appSize.contains(".")) {
            appSize = appSize.substring(0, appSize.indexOf("."));
        }
        appMarket = "华为";
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (appMarket) {
            case "华为":
                jumpToMainPage_Huawei(appType);
                break;
            case "小米":
                jumpToMainPage_Xiaomi(appType);
                break;
            default:
                //ssssss
        }
        //根据文件类别和文件大小确定应用运行时长
        if (appType.contains("游戏")) {
            if (200 > Integer.valueOf(appSize)) {
                try {
                    Thread.sleep(240 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(360 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Thread.sleep(8 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        //卸载App
        pkgList = getAppPackage();
        for (String pkg : pkgList) {
            if (StringUtils.isNotEmpty(pkg)) {
                int ustStatus = uninstallApp(pkg);
                if (ustStatus == 1) {
                    CaptureGUI.textArea_log.append("INFO:应用已卸载。\n");
                    break;
                }
            } else {
                CaptureGUI.textArea_log.append("INFO:未发现安装包\n");
            }
        }

        /**
         * 因为抓取数据包并打印出包的信息有时间延迟，为确保该app的数据包信息
         * 打印完成后终止线程，故设置等待3秒
         */
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 华为应用市场
     *
     * @param appType
     */
    public void jumpToMainPage_Huawei(String appType) {
        switch (appType) {
            /*
                游戏
             */
            case "休闲益智":
            case "经营策略":
            case "体育竞技":
            case "棋牌桌游":
            case "动作射击":
            case "角色扮演":
                if (200 > Integer.valueOf(appSize)) {
                    try {
                        Thread.sleep(240 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(360 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "影音娱乐":
            case "实用工具":
            case "社交通讯":
            case "教育":
            case "商务":
                yyst_main = new YystAppJumpToMainPage();
                if (yyst_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "新闻阅读":
                xwzx_main = new XwzxAppJumpToMainPage();
                if (xwzx_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "拍摄美化":
            case "主题个性":
                sysx_main = new SysxAppJumpToMainPage();
                if (sysx_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "出行导航":
            case "旅游住宿":
                lxjt_main = new LxjtAppJumpToMainPage();
                if (lxjt_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "购物比价":
                ssgw_main = new SsgwAppJumpToMainPage();
                if (ssgw_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "金融理财":
                jrlc_main = new JrlcAppJumpToMainPage();
                if (jrlc_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "便捷生活":
            case "美食":
            case "汽车":
            case "儿童":
                jjsh_main = new JjshAppJumpToMainPage();
                if (jjsh_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "运动健康":
                yljk_main = new YljkAppJumpToMainPage();
                if (yljk_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                yyst_main = new YystAppJumpToMainPage();
                if (yyst_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    /**
     * 小米应用商店
     *
     * @param appType
     */
    public void jumpToMainPage_Xiaomi(String appType) {
        switch (appType) {
            case "游戏":
                if (200 > Integer.valueOf(appSize)) {
                    try {
                        Thread.sleep(240 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(360 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "影音视听":
            case "实用工具":
            case "娱乐消遣":
            case "体育运动":
            case "聊天社交":
            case "学习教育":
            case "效率办公":
                yyst_main = new YystAppJumpToMainPage();
                if (yyst_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "图书阅读":
                tsyd_main = new TsydAppJumpToMainPage();
                if (tsyd_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "时尚购物":
                ssgw_main = new SsgwAppJumpToMainPage();
                if (ssgw_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "摄影摄像":
                sysx_main = new SysxAppJumpToMainPage();
                if (sysx_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "旅行交通":
                lxjt_main = new LxjtAppJumpToMainPage();
                if (lxjt_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "金融理财":
                jrlc_main = new JrlcAppJumpToMainPage();
                if (jrlc_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "新闻资讯":
                xwzx_main = new XwzxAppJumpToMainPage();
                if (xwzx_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "居家生活":
                jjsh_main = new JjshAppJumpToMainPage();
                if (jjsh_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "医疗健康":
                yljk_main = new YljkAppJumpToMainPage();
                if (yljk_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                yyst_main = new YystAppJumpToMainPage();
                if (yyst_main.jumpToMainPage(driver)) {
                    CaptureGUI.textArea_log.append("INFO:尝试跳转至应用主界面操作完成。\n");
                    try {
                        Thread.sleep(120 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CaptureGUI.textArea_log.append("ERROR:尝试跳转至应用主界面操作失败，请手动操作App以产生更多数据包。\n");
                    try {
                        Thread.sleep(180 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public List<String> getAppPackage() {
        pkgList = new ArrayList<>();
        BufferedReader br = null;
        try {
            //将命令行放到cmd中运行
            Process p = Runtime.getRuntime().exec("adb shell pm list packages -3");
            //读取执行命令后的输出结果
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
                //读取包含packagename的行
                if (!line.contains("io.appium") && line.contains("package:")) {
                        pkgList.add(line.substring(8, line.length()));
                }
            }

        } catch (Exception e) {
            CaptureGUI.textArea_log.append("ERROR:获取apk包名失败。\n");
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return pkgList;
    }

    public int uninstallApp(String pkg) {
        BufferedReader installBr = null;
        try {
            CaptureGUI.textArea_log.append("INFO:应用卸载中···\n");
            //将命令行放到cmd中运行
            Runtime.getRuntime().exec("adb uninstall " + pkg);

            /*//读取执行命令后的输出结果
            installBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = installBr.readLine()) != null) {
                System.out.println(line.toString());
            }*/
            return 1;
        } catch (Exception e) {
            CaptureGUI.textArea_log.append("ERROR:应用卸载失败...\n");
            e.printStackTrace();
            return -1;
        } finally {
            if (installBr != null) {
                try {
                    installBr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

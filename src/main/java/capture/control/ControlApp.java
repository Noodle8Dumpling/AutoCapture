package capture.control;

import capture.entity.appInfo;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 20171221
 * <p>
 * 修改人：郝京
 * <p>
 * 修改日期：20180201
 * <p>
 * 修改内容：增加非游戏类App进入应用主界面处理
 */
public class ControlApp extends Thread {
    AndroidDriver driver;
    String btnXp;
    String appName;
    String appType;
    Integer appSize;
    TurnToMainPage ttmf = null;

    public ControlApp(appInfo appInfo, AndroidDriver driver) {
        this.driver = driver;
        this.btnXp = appInfo.getBtnXp();
        this.appType = appInfo.getAppType();
        this.appSize = appInfo.getAppSize();
        this.appName = appInfo.getAppName();
    }

    @Override
    public void run() {
        //启动应用 点击“打开按钮”
        if (StringUtils.isNotEmpty(btnXp)) {
            try {
                driver.findElementByXPath(btnXp).click();
                System.out.println("应用正在启动");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(appName + "文件大小：" + appSize);
        //根据文件类别和文件大小确定应用运行时长
        if (appType.contains("游戏")) {

            if (200 > appSize) {
                try {
                    System.out.println("3分钟后卸载应用...");
                    Thread.sleep(180 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println("6分钟后卸载应用...");
                    Thread.sleep(360 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {

                ttmf = new TurnToMainPage();
                if (appType.contains("影音视听")) {
                    Thread.sleep(8 * 1000);
                    if (ttmf.YYST_MainPage(driver)) {
                        Thread.sleep(3 * 1000);
                    } else {
                        Thread.sleep(5 * 1000);
                    }
                } else if (appType.contains("时尚购物")) {
                    Thread.sleep(8 * 1000);
                    if (ttmf.SSGW_MainPage(driver)) {
                        Thread.sleep(3 * 1000);
                    } else {
                        Thread.sleep(5 * 1000);
                    }
                } else if (appType.contains("金融理财")) {
                    Thread.sleep(10 * 1000);
                    if (ttmf.JRLC_MainPage(driver)) {
                        Thread.sleep(3 * 1000);
                    } else {
                        Thread.sleep(5 * 1000);
                    }
                } else if(appType.contains("新闻资讯")){
                    Thread.sleep(8 * 1000);
                    if (ttmf.XWZX_MainPage(driver)) {
                        Thread.sleep(3 * 1000);
                    } else {
                        Thread.sleep(5 * 1000);
                    }
                }else if (appType.contains("居家生活")){
                    Thread.sleep(8 * 1000);
                    if (ttmf.JJSH_MainPage(driver)) {
                        Thread.sleep(3 * 1000);
                    } else {
                        Thread.sleep(5 * 1000);
                    }
                }else if (appType.contains("旅行交通")){
                    Thread.sleep(8 * 1000);
                    if (ttmf.LXJT_MainPage(driver)) {
                        Thread.sleep(3 * 1000);
                    } else {
                        Thread.sleep(5 * 1000);
                    }
                }else if(appType.contains("图书阅读")){
                    Thread.sleep(8 * 1000);
                    if (ttmf.TSYD_MainPage(driver)) {
                        Thread.sleep(5 * 1000);
                    } else {
                        Thread.sleep(3 * 1000);
                    }
                }else if (appType.contains("医疗健康")){
                    Thread.sleep(8 * 1000);
                    if (ttmf.YLJK_MainPage(driver)) {
                        Thread.sleep(5 * 1000);
                    } else {
                        Thread.sleep(3 * 1000);
                    }
                } else if (appType.contains("摄影摄像")){
                    Thread.sleep(8 * 1000);
                    if (ttmf.SYSX_MainPage(driver)) {
                        Thread.sleep(5 * 1000);
                    } else {
                        Thread.sleep(3 * 1000);
                    }
                }else {
                    Thread.sleep(180 * 1000);
                }


                //true代表已进入主界面，false代表异常
               /* if (ttmf.toMainForm(driver)){
                    Thread.sleep(60 * 1000);
                }else {
                    Thread.sleep(120 * 1000);
                }*/

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //卸载App
        String pkg = getAppPackage();
        if (StringUtils.isNotEmpty(pkg)) {
            int ustStatus = uninstallApp(pkg);
            if (ustStatus == 1) {
                System.out.println("App已成功卸载！");
            }
        } else {
            System.out.println("未发现安装包...");
        }
        /**
         * 因为抓取数据包并打印出包的信息有时间延迟，为确保该app的数据包信息
         * 打印完成后终止线程，故设置等待5秒
         */
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String getAppPackage() {
        String pkg = "";
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
                    pkg = line.substring(8, line.length());
                }
            }

        } catch (Exception e) {
            System.out.println("获取apk包信息时异常！");
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
        return pkg;
    }

    /**
     * @param pkg
     * @return
     */
    public int uninstallApp(String pkg) {
        System.out.println("App正在卸载中...");
        BufferedReader installBr = null;
        try {
            //将命令行放到cmd中运行
            Process p = Runtime.getRuntime().exec("adb uninstall " + pkg);

            //读取执行命令后的输出结果
            installBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = installBr.readLine()) != null) {
                System.out.println(line.toString());
            }
            return 1;
        } catch (Exception e) {
            System.out.println("App卸载失败！");
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

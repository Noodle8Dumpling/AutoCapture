package cn.com.bonc.mobile;

import org.openqa.selenium.remote.DesiredCapabilities;
import cn.com.bonc.entity.AppInfo;
import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ThreadFactory;

/**
 * 文件名：ControlApp.java
 * 编写人：郝 京
 * 编写日期：2017/11/06
 * 文件描述：自动安装启动、卸载手机app,删除电脑本地安装包。
 * 传入参数：app安装包路径
 * ----------------------------------------------------------------------------
 * 修改人：郝 京
 * 修改日期：2017/11/25
 * 修改内容：注释掉了安装app的capabilities
 * 修改原因：1.经测试通过Appium安装到手机的部分app内部被篡改，启动异常；
 * 2.改前抓包线程贯穿App安装、启动到卸载，会抓取到安装过程中手机产生的噪音，
 * 改后抓包线程只贯穿App启动、卸载，减少了抓到的噪音。
 */
public class ControlApp extends Thread {
    public AppInfo appInfo;

    public ControlApp(AppInfo bean) {
        this.appInfo = bean;
    }

    public DesiredCapabilities capabilities = new DesiredCapabilities();

    /*public void run() {
        try {
            capabilities.setCapability("deviceName", "3410c13d7d43");//可获得
            capabilities.setCapability("automationName", "Appium");
            //capabilities.setCapability("app", appInfo.getFilepath());
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", "8.0");//可获得？
            capabilities.setCapability("newCommandTimeout", "50000");
            capabilities.setCapability("appPackage", appInfo.getAppPackage());
            capabilities.setCapability("appActivity", appInfo.getAppActivity());
            capabilities.setCapability("enablePerformanceLogging", "true");

            //连接手机，安装并启动app

            AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            //应用启动后停60秒，进行网络访问
            Thread.sleep(60 * 1000);
            //10秒后卸载安装包并退出
            driver.removeApp(appInfo.getAppPackage());
            int status = uninstallApp(appInfo.getAppPackage());
            if (status == 1){
                System.out.println("App已成功卸载！");
            }
            driver.quit();
            */

    /**
     * 因为抓取数据包并打印出包的信息有时间延迟，为确保该app的数据包信息
     * 打印完成后终止线程，故设置等待5秒
     *//*
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            System.out.println("操作手机App时出现异常！");
            e.printStackTrace();
        }*/
    @Override
    public void run() {
        try {
            //启动app
            int srtStatus = startApp(appInfo.getAppPackage(), appInfo.getAppActivity());
            if (srtStatus == 1) {
                System.out.println("App启动成功！");
            }
            Thread.sleep(10 * 1000);
            //再次启动App
            srtStatus = startApp(appInfo.getAppPackage(), appInfo.getAppActivity());
            if (srtStatus == 1) {
                System.out.println("App再次启动成功！");
            }
            //应用启动后停60秒，进行网络访问
            Thread.sleep(60 * 1000);
            //10秒后卸载安装包并退出
            int ustStatus = uninstallApp(appInfo.getAppPackage());
            if (ustStatus == 1) {
                System.out.println("App已成功卸载！");
            }
            /**
             * 因为抓取数据包并打印出包的信息有时间延迟，为确保该app的数据包信息
             * 打印完成后终止线程，故设置等待5秒
             */
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            System.out.println("操作手机App时出现异常！");
            e.printStackTrace();
        }

    }

    public int startApp(String pkg,String avt) {
        System.out.println("App正在启动中...");
        BufferedReader installBr = null;
        try {
            //将命令行放到cmd中运行
            Process p = Runtime.getRuntime().exec("adb shell am start -n " + pkg + "/" + avt);

            //读取执行命令后的输出结果
            installBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = installBr.readLine()) != null) {
                System.out.println(line.toString());
            }
            return 1;
        } catch (Exception e) {
            System.out.println("App启动失败！");
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

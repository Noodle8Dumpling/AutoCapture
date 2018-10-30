package cn.com.bonc.main;

import cn.com.bonc.capture.CapturePackage;
import cn.com.bonc.entity.AppInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 文件名：Automation_V1.java
 * 编写者：郝京
 * 编写日期：2017/11/06
 * 文件描述：自动化测试主类。
 * 传入参数：app安装包路径，数据包日志存放路径
 *--------------------------------------------------
 * 修改人：郝京
 * 修改日期：2017/11/25
 * 修改内容：1.将Capture线程改为类，Control线程放到了Capture类中；
 *         2.增加cmd安装App到手机的方法；
 *         3.增加过程信息输出和App个数统计
 * 修改原因：1.改前main方法有两个线程类，Capture和Control,Capture线程有死循环，
 *           递归时，Capture的状态一直是Running，Capture中输出的后一个文件内
 *           容不停追加到之前输出的文档内容中。
 *         2.具体看ControlApp.java修改原因
 *         3.为方便统计
 *
 */


public class Automation_V1 {
    public static AppInfo appInfo = new AppInfo();//app实体

    public long count = 0l;
    public long successCount = 0l;
    public long failCount = 0l;

    /**
     *
     * @param dir
     * @return
     */
    public String getFilepath(File dir) {
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        String strFileName = null;
        String fileName = null;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                fileName = files[i].getName();
                if (fileName.endsWith(".apk")) { // 判断文件名是否以.apk结尾
                    strFileName = files[i].getAbsolutePath();
                    fileName = fileName.substring(0, fileName.length() - 4);
                    if (fileName.contains("(") || fileName.contains(")_")){
                        fileName.replace(")_", "_");
                    }
                    break;
                } else {
                    continue;
                }
            }
        }
        if (StringUtils.isNotEmpty(strFileName) && StringUtils.isNotEmpty(fileName)) {
            return strFileName + ";" + fileName;
        }
        return null;
    }

    /**
     *
     * @param filepath
     * @return
     */
    public AppInfo getAppInfo(String filepath) {

        BufferedReader br = null;
        AppInfo bean = new AppInfo();
        try {
            //将命令行放到cmd中运行
            Process p = Runtime.getRuntime().exec("aapt dump badging " + filepath);
            //读取执行命令后的输出结果
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            String pkg = null;
            String avt = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
                //读取包含packagename的行
                if (line.contains("package:")) {
                    pkg = line.split("\'")[1];
                    bean.setAppPackage(pkg);
                }
                //读取包含activity的行
                if (line.contains("launchable") && line.contains("activity")) {
                    i = i + 1;
                    if (i == 1){
                        avt = line.split("\'")[1];
                        bean.setAppActivity(avt);
                    }
                }
            }
            if (StringUtils.isNotEmpty(pkg) && StringUtils.isNotEmpty(avt)){
                System.out.println("成功获取apk包信息！");
                System.out.println("安装包信息：");
                System.out.println("package：" + pkg);
                System.out.println("activity：" + avt);
                return bean;
            }else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("获取apk包信息时异常！");
            e.printStackTrace();
            return bean;

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *
     * @param filepath
     * @return
     */
    public int installApp(String filepath){
        System.out.println("App正在安装中...");
        BufferedReader installBr = null;
        boolean flag = true;
        try{
            //将命令行放到cmd中运行
            Process p = Runtime.getRuntime().exec("adb install " + filepath);

            //读取执行命令后的输出结果
            installBr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = installBr.readLine()) != null) {
                System.out.println(line.toString());
                if (line.toString().contains("Success")){
                    flag = true;
                }
            }
            if (flag){
                System.out.println("App安装成功！");
                return 1;
            }else {
                System.out.println("App安装失败！");
                return -1;
            }

        }catch (Exception e){
            System.out.println("App安装失败！");
            e.printStackTrace();
            return -1;
        }
        finally {
            if (installBr != null)
            {
                try {
                    installBr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param dir
     * @param outpath
     * @throws InterruptedException
     */
    public void captureNext(File dir, String outpath) throws InterruptedException {
        String filepath = null;
        String filename = null;
        int installed = 0;
        if (StringUtils.isNotEmpty(getFilepath(dir))) {
            filepath = getFilepath(dir).split(";")[0];
            filename = getFilepath(dir).split(";")[1];
        }
        if (StringUtils.isNotEmpty(filepath)) {
            System.out.println("第一个App安装开始时间：");
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date()));

            installed = installApp(filepath);
            /*
                app安装成功则进行抓包操作
             */
            if (installed == 1){
                appInfo = getAppInfo(filepath);
                appInfo.setFilepath(filepath);
                appInfo.setAppName(filename);

                CapturePackage capture = new CapturePackage(appInfo, outpath);
                capture.capture();

                successCount = successCount + 1;
            }else {
                failCount = failCount + 1;
            }

            count = count + 1;

            boolean deleteApp = (new File(appInfo.getFilepath())).delete();
            System.out.println("本地安装包删除成功！开始安装下一个app...");

            this.captureNext(dir, outpath);

        } else {
            System.out.println("本次抓包过程结束！");
            System.out.println("结束时间：" +
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date()));
            System.out.println("本次要抓取的对象App共有" + count + "个。");
            System.out.println("安装成功并进行抓包操作的App共有" + successCount + "个。");
            System.out.println("安装失败的App共有" + failCount + "个。");
            System.exit(0);
        }
    }


    /**
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        File dir = new File(args[0]);
        String outPath = args[1];
        Automation_V1 auto = new Automation_V1();
        auto.captureNext(dir, outPath);
    }
}

package optimizationprogram.mainPro.services;

import java.util.List;

/**
 * 创建人：郝京
 *
 * 日期：2018-03-15
 *
 * 文件描述：配置手机App自动化需要用到的参数
 */
public interface ParaConfigService {
    /**
     * 启动Appium所需的参数
     */
    String AUTOMATIONNAME = "Appium";
    String PLATFORMNAME = "Android";
    String PLATFORMVERSION = "8.0";
    String NEWCOMMANDTIMEOUT = "54000";
    //String PKGNAME = "com.xiaomi.market";
    //String ACTIVITYNAME = ".ui.MarketTabActivity";

    /*
     * 搜索应用时用到的常量
     */
    //String SEARCH_BOX = "com.xiaomi.market:id/search_text_switcher";//手机自带应用市场主界面搜索框ID
    //String INPUT_BOX = "android:id/input";
    //Integer SUBMIT_CODE = 66;


    /**
     *  定义启动入口应用，搜索、下载、打开需要抓包应用过程需要的一些参数。
     * 参数包括：入口应用的包名，入口应用的调起活动名，入口应用主界面搜索框的id,
     *         点击搜索后输入框的id，提交搜索内容的键码
     * 注意：如果应用商店（或应用市场）的界面结构改变了，某些元素的id可能会改变
     * @return paraList
     */
    List<String> defAppPara();

    /*
     * 定义 选取、下载、打开符合条件App过程中需要的用到的XPath。
     * 注意：如果应用商店（或应用市场）的界面结构改变了，某些元素的XPath可能会改变
     * @return paraList
     */
    //public List<Object> defCommonXPath();


}

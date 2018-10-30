package capture.constant;

/**
 * 日 期： 20171221
 * <p>
 * 编写者： 郝 京
 * <p>
 * 文件名： CONFIGConstant.java
 * <p>
 * 文件描述：程序中用到的常量
 * <p>
 * 修改日期： 20180129
 * <p>
 * 修改人： 郝京
 * <p>
 * 修改内容：新增 NORESULT_View 常量
 */
public interface CONFIGConstant {
    /**
     * DesiredCapabilities capabilities
     */
    String DEVICENAME = "3410c13d7d43";
    String AUTOMATIONNAME = "Appium";
    String PLATFORMNAME = "Android";
    String PLATFORMVERSION = "8.0";
    String NEWCOMMANDTIMEOUT = "54000";
    String APPPACKAGE = "com.xiaomi.market";
    String APPACTIVITY = ".ui.MarketTabActivity";

    /**
     * 搜索应用时用到的常量
     */
    String MARKET_SEARCH_BOX = "com.xiaomi.market:id/search_text_switcher";//手机自带应用市场主界面搜索框ID
    String INPUT_BOX = "android:id/input";
    Integer SUBMIT_CODE = 66;

    /**
     * 程序用到的XPATH，若xml改变，XPATH需要做出调整
     */
    //网络状况不佳，搜索app后，未加载出搜索结果界面
    String NET_REQ_FAIL_XPATH = "//android.widget.FrameLayout[2]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.webkit.WebView[1]" +
            "/android.webkit.WebView[1]" +
            "/android.view.View[1]" +
            "/android.view.View[@content-desc = '加载失败，请检查网络后重试~']";
    //重试按钮,和 NET_REQ_FAIL_XPATH 一起出现
    String RETRY_BTN_XPATH = "//android.widget.FrameLayout[2]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.webkit.WebView[1]" +
            "/android.webkit.WebView[1]" +
            "/android.view.View[1]" +
            "/android.view.View[@content-desc = '重试']";

    //搜索应用时需要用到的XPATH，这些XPATH都是根据搜索的结果界面（xml文件）总结出来的。比较麻烦。
    String XPATH_ListView = "//android.widget.FrameLayout[2]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.webkit.WebView[1]" +
            "/android.webkit.WebView[1]" +
            "/android.widget.ListView";
    String XPATH_View = "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View";
    String BTN_View = "/android.view.View[1]" +
            "/android.view.View[2]" +
            "/android.view.View[1]" +
            "/android.view.View[2]";
    String BTN_Button = "/android.view.View[1]" +
            "/android.view.View[2]" +
            "/android.widget.Button[1]";
    String DETAIL_View = "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[2]";
    String NORESULT_View = "//android.widget.FrameLayout[2]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.webkit.WebView[1]" +
            "/android.webkit.WebView[1]" +
            "/android.view.View[@content-desc = '含应用商店属性的应用分发类应用暂不收录']";
    /**
     * 弹窗及快捷按钮相关Xpath
     */
    String LINEARLAYOUT_PATH = "//android.widget.LinearLayout[@clickable = 'true']";
    String FRAMELAYOUT_PATH = "//android.widget.FrameLayout[@clickable = 'true']";
    String RELATIVELAYOUT_PATH = "//android.widget.RelativeLayout[@clickable = 'true']";
    String IMAGEVIEW_PATH = "//android.widget.ImageView[@clickable = 'true']";
    String TEXTVIEW_PATH = "//android.widget.TextView[@clickable = 'true']";
    String BUTTON_PATH = "//android.widget.Button[@clickable = 'true']";
    String LINEARLAYOUT_TEXTVIEW_PATH = "//android.widget.LinearLayout[@clickable = 'true']/android.widget.TextView";
    String RELATIVELAYOUT_TEXTVIEW_PATH = "//android.widget.RelativeLayout[@clickable = 'true']/android.widget.TextView";
    String RELATIVELAYOUT_IMAGEVIEW_PATH = "//android.widget.RelativeLayout[@clickable = 'true']//android.widget.ImageView";
    String IMAGEBUTTON_PATH = "//android.widget.ImageButton[@clickable = 'true']";
    /**
     * 输入框
     */
    String EDITTEXT_PATH = "//android.widget.EditText[@clickable = 'true']";

    /**
     * APPTYPE
     */
    String APPTYPE1 = "影音视听";
    String APPTYPE2 = "时尚购物";
    String APPTYPE3 = "金融理财";
}

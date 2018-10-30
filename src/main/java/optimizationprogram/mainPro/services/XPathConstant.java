package optimizationprogram.mainPro.services;

/**
 * 创建人： 郝京
 * <p>
 * 创建日期： 2018-03-16
 * <p>
 * 文件描述：XPath常量
 */
public interface XPathConstant {

    /**
     * 可点击元素XPATH
     */
    String ELE_CLICKABLE_XPATH = "//*[@clickable = 'true']";

    /**
     * 权限弹窗按钮
     */
    String PRIVILEGE_BTN_XPATH = "//android.widget.Button[@text = '允许' or @text = '始终允许' or @text = '确定' or @text = '同意' or @text = '同意并继续' or @text = '继续' or @text = '下一步' or @text = '好的']";
    String PRIVILEGE_TEXTVIEW_XPATH = "//android.widget.TextView[@text = '允许' or @text = '始终允许' or @text = '确定' or @text = '同意' or @text = '同意并继续' or @text = '继续' or @text = '下一步' or @text = '好的']";
    /**
     * 协议弹窗
     */
    String OTHER_DIALOGUE_XPATH = "//*[@clickable = 'true' and @text != '']";
    /**
     * 协议弹窗按钮
     */
    String AGREEMENT_BTN_XPATH = "//android.widget.Button[@text = '允许' or @text = '始终允许' or @text = '确定' or @text = '同意' or @text = '同意并使用' or @text = '继续' or @text = '下一步' or @text = '接受' or @text = '已阅读并同意']";
    String AGREEMENT_TEXTVIEW_XPATH = "//android.widget.TextView[@text = '允许' or @text = '始终允许' or @text = '确定' or @text = '同意' or @text = '同意并使用' or @text = '继续' or @text = '下一步' or @text = '接受' or @text = '已阅读并同意']";
    /**
     * 快捷按钮,无文字
     */
    String ENTRY_NULL_XPATH = "//*[@clickable = 'true' and @content-desc = '' and @text = '']";
    String ENTRY_BTN_DESCENDANT_XPATH1 = "//*[@clickable = 'true']/descendant::*[@text != '']";


    /**
     * 取消按钮
     */
    String CANCEL_BTN_XPATH = "//android.widget.Button[@text = '取消' or @text = '稍后再说' or @text = '下次再说']";
    String CANCEL_TEXTVIEW_XPATH = "//android.widget.TextView[@text = '取消' or @text = '稍后再说' or @text = '下次再说']";

    /**
     * 华为删除弹窗
     */
    String DELETE_BTN_XPATH = "//android.widget.Button[@text = '立即删除']";
    /**
     * 程序用到的XPATH，若xml改变，XPATH需要做出调整
     */
    //网络状况不佳，搜索app后，未加载出搜索结果界面
    String NET_REQ_FAIL_WORDS = "加载失败，请检查网络后重试~";
    String NET_REQ_FAIL_XPATH = "//*[@content-desc = '加载失败，请检查网络后重试~']";
    //重试按钮,和 NET_REQ_FAIL_XPATH 一起出现
    String RETRY_BTN_XPATH = "//*[@content-desc = '重试']";

    //搜索应用时需要用到的XPATH，这些XPATH都是根据搜索的结果界面（xml文件）总结出来的。比较麻烦。
    String XPATH_ListView = "//android.widget.FrameLayout[2]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.webkit.WebView[1]" +
            "/android.webkit.WebView[1]" +
            "/android.view.View[1]" +
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
            "/android.view.View[2]"; /*+
            "/android.widget.Button[1]";*/
    String DETAIL_View = "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[2]";
    String NORESULT_View = "//*[@content-desc = '含应用商店属性的应用分发类应用暂不收录']";


    /**
     * 弹窗及快捷按钮相关Xpath
     */
    String LINEARLAYOUT_PATH = "//android.widget.LinearLayout[@clickable = 'true']";
    String FRAMELAYOUT_PATH = "//android.widget.FrameLayout[@clickable = 'true']";
    String RELATIVELAYOUT_PATH = "//android.widget.RelativeLayout[@clickable = 'true']";
    String IMAGEVIEW_PATH = "//android.widget.ImageView[@clickable = 'true']";
    String TEXTVIEW_PATH = "//android.widget.TextView[@clickable = 'true']";
    String BUTTON_PATH = "//android.widget.Button[@clickable = 'true']";
    String IMAGEBUTTON_PATH = "//android.widget.ImageButton[@clickable = 'true']";
    String VIEWGROUP_PATH = "//android.view.ViewGroup[@clickable = 'true']";
    String RADIOBUTTON_PATH = "//android.widget.RadioButton[@clickable = 'true']";
    String ACTIONBAR_PATH = "//android.support.v7.app.ActionBar.Tab[@clickable = 'true']";

    /**
     * 输入框
     */
    String EDITTEXT_PATH = "//android.widget.EditText[@clickable = 'true']";

    //点击安装按钮出现的弹窗
    String BEFOREINSTALLDIALOGBOX_PATH = "//android.widget.Button[@text = '确定']";

    //广告应用
    String ADVERTISEMENT_PATH = "//android.widget.FrameLayout[2]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.webkit.WebView[1]" +
            "/android.webkit.WebView[1]" +
            "/android.view.View[1]" +
            "/android.widget.ListView[1]" +
            "/android.widget.ListView[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[1]" +
            "/android.view.View[2]" +
            "/android.view.View[@content-desc = '广告']";

}

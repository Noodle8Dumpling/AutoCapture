package captureProgram.mainPro.services;

public interface HuaWeiMarketXPath {
    //网络状况不佳，搜索app后，未加载出搜索结果界面
    String NET_REQ_FAIL_PATH = "//android.support.v4.view.ViewPager[1]"+
            "/android.widget.FrameLayout[1]"+
            "/android.widget.FrameLayout[1]"+
            "/android.widget.RelativeLayout[1]"+
            "/android.widget.TextView[@content-desc = '网络未连接，请点击重试']";

    String LISTVIEW_LINEARLAYOUT_PATH = "//android.support.v4.view.ViewPager[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.ListView[1]" +
            "/android.widget.LinearLayout";
    //应用详情中的标签信息
    String APPLABEL_LINEARLAYOUT_PATH1 = "//android.support.v4.view.ViewPager[1]" +
            "/android.widget.ScrollView[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.LinearLayout";

    String APPLABEL_PATH = "//android.widget.TextView[@text = '应用标签']" ;
    String APPLABEL_LINEARLAYOUT_PATH2 = "/android.widget.FrameLayout[1]" +
            "/android.widget.LinearLayout";
    //安装按钮
    String BUTTON_PATH = "//android.widget.RelativeLayout[1]" +
            "/android.widget.RelativeLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.LinearLayout[2]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.RelativeLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.view.View[@clickable = 'true']";
    //点击安装按钮出现的弹窗
    String BEFOREINSTALLDIALOGBOX_PATH = "//android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.FrameLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.Button[@text = '接受']";
    //App名称部分路径
    String APPNAME_TEXTVIEW = "/android.widget.RelativeLayout[1]" +
            "/android.widget.RelativeLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.RelativeLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.TextView[1]";
    String APPSIZE_TEXTVIEW = "/android.widget.RelativeLayout[1]" +
            "/android.widget.RelativeLayout[1]" +
            "/android.widget.LinearLayout[1]" +
            "/android.widget.RelativeLayout[1]" +
            "/android.widget.LinearLayout[2]" +
            "/android.widget.TextView";
}

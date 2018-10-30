package optimizationprogram.mainPro.welcomePage;

import io.appium.java_client.android.AndroidDriver;

/**
 * Date:2018-07-22
 * Author:郝京
 * Description:处理界面按键
 * 按键类型：元素带有标志性文字描述、元素没有文字描述
 */
public class HandleShortcut {
    private AndroidDriver driver;

    public HandleShortcut(AndroidDriver driver){
        this.driver = driver;
    }

    /**
     * 处理带有标志性文字描述的按键
     * @return
     */
    public Integer handleHasText(){
        return 0;
    }

    /**
     * 处理没文字描述的按键
     * @return
     */
    public Integer handleNoText(){
        return 0;
    }

}

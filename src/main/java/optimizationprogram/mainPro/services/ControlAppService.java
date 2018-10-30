package optimizationprogram.mainPro.services;

import io.appium.java_client.android.AndroidDriver;

/**
 * 创建人：郝京
 *
 * 创建日期：2018-03-15
 *
 * 文件描述：控制App进入主界面，不同种类的App进入主界面的方式不同
 */
public interface ControlAppService {
    /**
     * 进入主界面
     * @param driver
     * @return
     */
    Boolean turnToMainPage(AndroidDriver driver,String appType);
}

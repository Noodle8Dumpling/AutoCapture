package optimizationprogram.mainPro.welcomePage;

import io.appium.java_client.android.AndroidDriver;

/**
 * Date：2018-07-22
 * Author:郝京
 * Description:处理弹窗
 * 弹窗类型：权限弹窗、协议弹窗、更新弹窗、其他弹窗
 */
public class HandlePopup {
    private AndroidDriver driver;
    public HandlePopup(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * 处理权限弹窗
     * @return
     */
    public Integer handlePrivilegePopup() {

        return 0;
    }

    /**
     * 处理协议弹窗
     * @return
     */
    public Integer handleAgreementPopup(){
        return  0;
    }

    /**
     * 处理软件升级弹窗
     * @return
     */
    public Integer handleUpdatePopup(){
        return 0;
    }

    /**
     * 处理其他类型弹窗
     * @return
     */
    public Integer handleOtherPopup(){
        return 0;
    }
}

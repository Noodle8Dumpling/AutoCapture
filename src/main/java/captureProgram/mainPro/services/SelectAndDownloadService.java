package captureProgram.mainPro.services;

import io.appium.java_client.android.AndroidDriver;
import org.omg.CORBA.OBJ_ADAPTER;

/**
 * 创建人：郝京
 * <p>
 * 创建日期：2018-03-15
 * <p>
 * 文件描述：挑选符合搜索条件的App并下载
 */
public interface SelectAndDownloadService {
    /**
     * 挑选前检查网络连接状况是否良好
     *
     * @param driver
     * @param count
     * @return
     */
    Boolean beforeSelect(AndroidDriver driver, int count);

    /**
     * 开始挑选结果列表中符合搜索条件的应用。
     *
     * @param driver
     * @param keyword
     * @return
     */
    void startSelect(AndroidDriver driver, String keyword);

    /**
     * 挑选完成后下载应用
     *
     * @param driver
     * @return
     */
    Boolean afterSelect(AndroidDriver driver);

    /**
     * 主方法
     */
    Object selectAndDownload(AndroidDriver driver, String appName);

}

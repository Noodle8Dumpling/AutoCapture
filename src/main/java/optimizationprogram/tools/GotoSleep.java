package optimizationprogram.tools;

import static java.lang.Thread.sleep;

/**
 * 设置程序休眠的时间
 */
public class GotoSleep {
    public void startSleep(long interval){
        try {
            sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

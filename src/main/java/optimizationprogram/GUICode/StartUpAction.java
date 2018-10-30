package optimizationprogram.GUICode;

import optimizationprogram.mainPro.Capture_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartUpAction extends Thread {
    String deviceName;
    String networkName;
    String dataSource;
    String dataDestination;
    String appMarket;

    public StartUpAction(String deviceName, String appMarket,String networkName, String dataSource, String dataDestination) {
        this.deviceName = deviceName;
        this.appMarket = appMarket;
        this.networkName = networkName;
        this.dataSource = dataSource;
        this.dataDestination = dataDestination;

    }

    @Override
    public void run() {
        if (CaptureGUI.stopFlag){
            new Capture_main(deviceName,
                    appMarket,
                    networkName,
                    dataSource,
                    dataDestination).StartUpCapture();
        }

    }
}

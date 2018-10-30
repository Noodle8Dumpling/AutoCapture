package optimizationprogram.mainPro;

import java.util.List;

/**
 * 创建人：郝京
 *
 * 创建日期：2018-03-15
 *
 * 文件描述：外部参数实体
 */
public class GUIInfo {
    /**
     * 输入参数
     */
    public String deviceName;
    public String networkName;
    public String appMarket;
    public String dataSource;
    public String dataDestination;

    /**
     * 输出参数
     */
    public String logRecord;
    public String havePkgAppCount;
    public String noPkgAppCount;
    public String capturedPkgsNum;

    /*
     * 待扩展参数
     */
//    public Integer capInterval;
//    public List<String> filterList;
//    public String appGroup;


    public String getAppMarket() {
        return appMarket;
    }

    public void setAppMarket(String appMarket) {
        this.appMarket = appMarket;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataDestination() {
        return dataDestination;
    }

    public void setDataDestination(String dataDestination) {
        this.dataDestination = dataDestination;
    }

    public String getLogRecord() {
        return logRecord;
    }

    public void setLogRecord(String logRecord) {
        this.logRecord = logRecord;
    }

    public String getHavePkgAppCount() {
        return havePkgAppCount;
    }

    public void setHavePkgAppCount(String havePkgAppCount) {
        this.havePkgAppCount = havePkgAppCount;
    }

    public String getNoPkgAppCount() {
        return noPkgAppCount;
    }

    public void setNoPkgAppCount(String noPkgAppCount) {
        this.noPkgAppCount = noPkgAppCount;
    }

    public String getCapturedPkgsNum() {
        return capturedPkgsNum;
    }

    public void setCapturedPkgsNum(String capturedPkgsNum) {
        this.capturedPkgsNum = capturedPkgsNum;
    }
}

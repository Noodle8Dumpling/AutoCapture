package optimizationprogram.mainPro;

/**
 * 创建人：郝京
 *
 * 创建日期：2018-03-15
 *
 * 文件描述：搜索后选取的符合条件的App信息实体
 */
public class SelectedAppInfo {
    public String appName;
    public String pkgName;
    public String appType;
    public String appSize;
    public String openXPath;
    public String descriptionXPath;
    public String flag;//安装成功标志
    public String failInfo;//没搜到其他


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getOpenXPath() {
        return openXPath;
    }

    public void setOpenXPath(String openXPath) {
        this.openXPath = openXPath;
    }

    public String getDescriptionXPath() {
        return descriptionXPath;
    }

    public void setDescriptionXPath(String descriptionXPath) {
        this.descriptionXPath = descriptionXPath;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }
}

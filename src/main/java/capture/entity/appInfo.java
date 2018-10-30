package capture.entity;

/**
 * 日 期： 20171221
 * <p>
 * 编写者： 郝 京
 * <p>
 * 文件名： appInfo.java
 * <p>
 * 文件描述：实体类
 */
public class appInfo {

    public String pkg_outPath;//抓到的数据包输出路径
    public String btnXp;//app 打开 按钮的xpath
    public String appName;//app的名称 用来命名输出文档
    public String appType;//app类别
    public Integer appSize;//app大小

    public String getBtnXp() {
        return btnXp;
    }

    public void setBtnXp(String btnXp) {
        this.btnXp = btnXp;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkg_outPath() {
        return pkg_outPath;
    }

    public void setPkg_outPath(String pkg_outPath) {
        this.pkg_outPath = pkg_outPath;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public Integer getAppSize() {
        return appSize;
    }

    public void setAppSize(Integer appSize) {
        this.appSize = appSize;
    }
}

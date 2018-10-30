package captureProgram.mainPro;

/**
 * 创建人：郝京
 *
 * 创建日期：2018-03-15
 *
 * 文件描述：入口App（应用市场/应用商店）信息实体
 */
public class AppMarketInfo {
    public String pkgName;
    public String activityName;
    public String waitActivityName;
    public String searchBox;
    public String inputBox;


    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getWaitActivityName() {
        return waitActivityName;
    }

    public void setWaitActivityName(String waitActivityName) {
        this.waitActivityName = waitActivityName;
    }

    public String getSearchBox() {
        return searchBox;
    }

    public void setSearchBox(String searchBox) {
        this.searchBox = searchBox;
    }

    public String getInputBox() {
        return inputBox;
    }

    public void setInputBox(String inputBox) {
        this.inputBox = inputBox;
    }

}

package cn.com.bonc.entity;

public class AppInfo {
    /**
     * 文件名：AutomationMain.AppMarketInfo.java
     * 文件描述：app的信息
     */

        private String filepath;//app安装包路径
        private String appPackage;//app包名称
        private String appActivity;//app启动活动
        private String appName;//app标签名

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        public String getAppActivity() {
            return appActivity;
        }

        public void setAppActivity(String appActivity) {
            this.appActivity = appActivity;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

}

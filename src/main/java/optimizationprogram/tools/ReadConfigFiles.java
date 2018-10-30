package optimizationprogram.tools;

import optimizationprogram.GUICode.CaptureGUI;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Date：20180-07-21
 * Author:郝 京
 * Description:读取配置文件
 */
public class ReadConfigFiles {
    private String filepath;

    public ReadConfigFiles() {
    }

    public ReadConfigFiles(String filepath) {
        this.filepath = filepath;
    }

    public List<String> getFileContent() {
        List<String> list = new ArrayList<>();
        BufferedReader br;
        String rec;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(filepath)), "UTF-8"));
            while ((rec = br.readLine()) != null) {
                if (StringUtils.isNotEmpty(rec.trim())) {
                    list.add(rec);
                }
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            CaptureGUI.textArea_log.append("ERROR:" +
                    filepath + " 转码出错。\n");
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            CaptureGUI.textArea_log.append("ERROR:未找到配置文件 " +
                    filepath + " ，请检查该路径是否正确！\n");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return list;
    }
}

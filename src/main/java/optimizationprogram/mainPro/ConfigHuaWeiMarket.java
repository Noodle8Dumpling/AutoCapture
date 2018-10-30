package optimizationprogram.mainPro;

import optimizationprogram.mainPro.services.ParaConfigService;

import java.util.ArrayList;
import java.util.List;

public class ConfigHuaWeiMarket implements ParaConfigService {
    @Override
    public List<String> defAppPara() {
        List<String> paraList = new ArrayList<>();
        paraList.add("com.huawei.appmarket");
        paraList.add(".MainActivity");
        paraList.add(".MarketActivity");
        paraList.add("com.huawei.appmarket:id/search_edit_text");
        paraList.add("com.huawei.appmarket:id/searchText");
        return paraList;
    }
}

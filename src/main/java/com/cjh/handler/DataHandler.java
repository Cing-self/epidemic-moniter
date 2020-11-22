package com.cjh.handler;

import com.cjh.bean.DataBean;
import com.cjh.service.DataService;
import com.cjh.util.HttpConnectUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DataHandler {

    private static String urlStr = "https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";
    @Autowired
    private DataService dataService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @PostConstruct
    public void saveData(){
        System.out.println("初始化数据的 存储");
        List<DataBean> dataBeans = getDataList();
        //先清空，再存储
        dataService.remove(null);
        dataService.saveBatch(dataBeans);
    }

    @Scheduled(cron = "0 0/1 * * * ? *")
    public void updateData(){
        System.out.println("更新数据，当前时间：" + dateFormat.format(new Date()));
        List<DataBean> dataBeans = getDataList();
        //先清空，再存储
        dataService.remove(null);
        dataService.saveBatch(dataBeans);
    }
    private static List<DataBean> getDataList() {
        String s = HttpConnectUtil.doGet(urlStr);

        Gson gson = new Gson();
        Map map = gson.fromJson(s, Map.class);

        //获取key为data的字符串 value
        String subStr = (String)map.get("data");
        //将subStr解析成一个map
        Map subMap = gson.fromJson(subStr, Map.class);

        //从submap中获取所有地区的疫情信息areaTree
        ArrayList areaList = (ArrayList) subMap.get("areaTree");
        //将所有地区的疫情信息获取出来，包装成一个对象，此时这个areaTree的name是中国
        Map dataMap = (Map) areaList.get(0);

        //从dataMap中获取中国下所有的子地区
        ArrayList childrenList = (ArrayList) dataMap.get("children");

        //遍历子地区，并将每个地区包装成一个对象
        ArrayList<DataBean> result = new ArrayList();
        //遍历children数组
        for (Object o : childrenList) {
            Map tmp = (Map) o;
            String name = (String) tmp.get("name");
            Map totalMap = (Map) tmp.get("total");
            double nowConfirm = (Double) totalMap.get("nowConfirm");
            double confirm = (Double) totalMap.get("confirm");
            double dead = (Double) totalMap.get("dead");
            double heal = (Double) totalMap.get("heal");

            DataBean dataBean = new DataBean(null, name, (int) nowConfirm, (int) confirm, (int) dead, (int) heal);
            result.add(dataBean);
        }

        return result;
    }
}

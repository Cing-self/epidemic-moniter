package com.cjh.util;

import com.cjh.bean.DataBean;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class JsoupHandler {

    public static ArrayList<DataBean> getDataList(String url) throws IOException {
        String urlStr = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        url = urlStr;
//        第一种方式：
//        String str = HttpConnectUtil.doGet(url);
//
//        Document doc = Jsoup.parse(str);

//        第二种方式：
        Document doc = Jsoup.connect(url).get();
        Element script = doc.getElementById("getAreaStat");

        String data = script.data();

        String subData = data.substring(data.indexOf("["), data.lastIndexOf("]") + 1);
        Gson gson = new Gson();
        //获取所有数据
        ArrayList list = gson.fromJson(subData, ArrayList.class);
        //遍历数据
        ArrayList<DataBean> result = new ArrayList<>();
        for (Object o : list) {
            Map map = (Map) o;
            String name = (String) map.get("provinceName");
            double nowConfirm = (Double) map.get("currentConfirmedCount");
            double confirm = (Double) map.get("confirmedCount");
            double dead = (Double) map.get("deadCount");
            double heal = (Double) map.get("curedCount");
            DataBean dataBean = new DataBean(null, name, (int) nowConfirm, (int) confirm, (int) dead, (int) heal);
            result.add(dataBean);
        }

        return result;
    }
}

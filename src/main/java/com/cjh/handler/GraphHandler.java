package com.cjh.handler;

import com.cjh.bean.DataBean;
import com.cjh.bean.GraphBarBean;
import com.cjh.bean.GraphBean;
import com.cjh.util.HttpConnectUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphHandler {

    private static String urlStr = "https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?" +
            "modules=chinaDayList,chinaDayAddList,cityStatis,nowConfirmStatis,provinceCompare";

    private static String urlStrAll = "https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5";

    public static List getData(){
        String s = HttpConnectUtil.doGet(urlStrAll);

        Gson gson = new Gson();
        Map map = gson.fromJson(s, Map.class);

        System.out.println(map);
        //获取key为data的字符串 value
        String subStr = (String)map.get("data");
        //将subStr解析成一个map
        Map subMap = gson.fromJson(subStr, Map.class);

        //从submap中获取所有地区的疫情信息areaTree
        ArrayList areaList = (ArrayList) subMap.get("areaTree");
        //将所有地区的疫情信息获取出来，包装成一个对象，此时这个areaTree的name是中国
        Map dataMap = (Map) areaList.get(0);

        //从dataMap中获取中国下所有的省
        ArrayList childrenList = (ArrayList) dataMap.get("children");

        //遍历省，获取省的每个地区
        ArrayList<GraphBarBean> result = new ArrayList();
        //遍历children数组
        for (Object o : childrenList) {
            Map tmp = (Map) o;
            String name = (String) tmp.get("name");
            ArrayList totalMap = (ArrayList) tmp.get("children");
            for (Object o1 : totalMap) {
                Map subTmp = (Map) o1;
                if ("境外输入".equals((String)subTmp.get("name"))){
                    Map total = (Map) subTmp.get("total");
                    double fromAbroad = (Double) total.get("confirm");
                    GraphBarBean graphBarBean = new GraphBarBean(name, (int)fromAbroad);
                    result.add(graphBarBean);
                }
            }

        }

        return result;
    }

    public static ArrayList<GraphBean> getGraphList(){
        String str = HttpConnectUtil.doGet(urlStr);
        Gson gson = new Gson();
        Map map = gson.fromJson(str, Map.class);

        Map dataMap = (Map) map.get("data");
        ArrayList chinaDayList = (ArrayList) dataMap.get("chinaDayList");

        //遍历chinaDayList数据
        ArrayList<GraphBean> graphList = new ArrayList<>();
        for (Object o : chinaDayList) {
            Map tmpMap = (Map) o;
            String date = (String) tmpMap.get("date");
            double confirm = (double) tmpMap.get("confirm");
            double heal = (double) tmpMap.get("heal");
            double dead = (double) tmpMap.get("dead");
            GraphBean graphBean = new GraphBean(date, (int)confirm, (int)heal, (int)dead);
            graphList.add(graphBean);
        }

        return graphList;
    }

}

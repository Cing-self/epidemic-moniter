package com.cjh.controller;

import com.cjh.bean.DataBean;
import com.cjh.bean.GraphBarBean;
import com.cjh.bean.GraphBean;
import com.cjh.bean.MapBean;
import com.cjh.handler.GraphHandler;
import com.cjh.service.DataService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class GraphController {

    @Autowired
    private DataService dataService;

    @GetMapping("/map")
    public String map(Model model){
        List<DataBean> dataBeans = dataService.list();
        List<MapBean> mapBeans1 = new ArrayList<>();
        List<MapBean> mapBeans2 = new ArrayList<>();
        for (DataBean dataBean : dataBeans) {
            mapBeans1.add(new MapBean(dataBean.getName(), dataBean.getNowConfirm()));
            mapBeans2.add(new MapBean(dataBean.getName(), dataBean.getConfirm()));

        }

        model.addAttribute("mapData1", new Gson().toJson(mapBeans1));
        model.addAttribute("mapData2", new Gson().toJson(mapBeans2));
        return "map";
    }
    @GetMapping("/graphBar")
    public String graphBar(Model model){
        List<GraphBarBean> list = GraphHandler.getData();
        Collections.sort(list);
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> fromAbroadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GraphBarBean barBean = list.get(i);
            nameList.add(barBean.getName());
            fromAbroadList.add(barBean.getFromAbroad());
        }

        model.addAttribute("nameList", new Gson().toJson(nameList));
        model.addAttribute("fromAbroadList", new Gson().toJson(fromAbroadList));

        return "graphBar";
    }
    @GetMapping("/graph")
    public String graph(Model model){
        List<GraphBean> list = GraphHandler.getGraphList();

        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<Integer> confirmList = new ArrayList<>();
        ArrayList<Integer> healList = new ArrayList<>();
        ArrayList<Integer> deadList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            GraphBean graphBean = list.get(i);
            dateList.add(graphBean.getDate());
            confirmList.add(graphBean.getConfirm());
            healList.add(graphBean.getHeal());
            deadList.add(graphBean.getDead());
        }

        Gson gson = new Gson();
        model.addAttribute("dateList", gson.toJson(dateList));
        model.addAttribute("confirmList", gson.toJson(confirmList));
        model.addAttribute("healList", gson.toJson(healList));
        model.addAttribute("deadList", gson.toJson(deadList));
        return "graph";
    }
}

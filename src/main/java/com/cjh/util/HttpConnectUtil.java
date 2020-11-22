package com.cjh.util;

import jdk.internal.util.xml.impl.Input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnectUtil {

    public static String doGet(String urlStr){
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //连接时间：发送请求的点到目标主机的时间，受距离影响
            conn.setConnectTimeout(15000);
            //读取时间：连接成功之后，获取数据的时间，受数据大小影响
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            if (200 == conn.getResponseCode()){
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = null;
                while ((line = br.readLine()) != null){
                    builder.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null) is.close();
                if (br != null) br.close();
                conn.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}

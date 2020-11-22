package com.cjh.util;

import jdk.internal.util.xml.impl.Input;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtil {

    public static String doGet(String urlStr){
        //提供了闭合的httpClient对象
        CloseableHttpClient httpClient = null;
        //提供了闭合的响应对象
        CloseableHttpResponse response = null;

        String result = null;

        try {
            //使用默认创建方式
            httpClient = HttpClients.createDefault();
            //创建一个get请求，传入Url
            HttpGet httpGet = new HttpGet(urlStr);
            //设置请求头的方式
            httpGet.addHeader("Accept", "application/json");

            //设置请求参数：连接时间、数据读取时间（socketTimeOut）等，单位是ms
            //  ConnectionRequestTimeout 指从共享连接池中取出连接的超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(35000)
                    .setConnectionRequestTimeout(35000)
                    .setSocketTimeout(60000)
                    .build();
            //设置配置参数
            httpGet.setConfig(requestConfig);
            //执行请求
            response = httpClient.execute(httpGet);
            //从返回对象中获取数据
            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (httpClient != null) httpClient.close();
                if (httpClient != null) response.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return result;
    }

    public String doPost(String urlStr, Map parameterMap) throws Exception {
        //拼装url携带的参数
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null){
            Iterator iterator = parameterMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String value = "";
                if (parameterMap.get(key) != null){
                    value = (String) parameterMap.get(key);
                }
                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()){
                    parameterBuffer.append("&");
                }
            }
        }

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));

        OutputStream os = null;
        OutputStreamWriter ow = null;
        InputStream is = null;
        InputStreamReader ir = null;
        BufferedReader reader = null;

        StringBuffer resultBuffer = new StringBuffer();
        try {
            os = connection.getOutputStream();
            ow = new OutputStreamWriter(os);

            ow.write(parameterBuffer.toString());
            ow.flush();

            if (connection.getResponseCode() != 200){
                throw new Exception("HTTP Request is not success, " + "Response code is " + connection.getResponseCode());
            }
            is = connection.getInputStream();
            ir = new InputStreamReader(is);
            reader = new BufferedReader(ir);
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null){
                resultBuffer.append(tempLine);
            }
        }finally {
            if (reader != null) reader.close();
            if (ir != null) ir.close();
            if (is != null) is.close();
            if (ow != null) ow.close();
            if (os != null) os.close();
        }
        return resultBuffer.toString();
    }
}

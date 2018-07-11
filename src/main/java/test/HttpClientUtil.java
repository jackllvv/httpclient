package test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {



    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 200; i++) {
            executorService.execute(new Mythread());
        }
    }

    static class Mythread implements Runnable {

        @Override
        public void run() {
            try {
                int i = 0;
                for(;;) {
                    String liveId = "15500000105345";
                    get1(liveId);
    //            System.out.println("===============================================================");
                    Header[] headers = get2(liveId);
                    if (headers != null && headers.length > 0) {
                        BufferedHeader bh = (BufferedHeader) headers[0];
                        String value = bh.getValue();
                        String[] split = value.split(";");
                        String cookie = split[0];

    //                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        post(liveId, cookie);

                        post2(liveId);
                    }
                    System.out.println(i++);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public  void get1(String liveId) throws Exception{
            String url = "http://h5-zb.leju.com/live.html?liveID="+liveId+"&from=timeline&isappinstalled=0";
            Map<String, String> param = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            String resultString = "";
            CloseableHttpResponse response = null;
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//            System.out.println(resultString);
            }
        }

        public  Header[] get2(String liveId) throws Exception{
            String url =  "http://h5-zb.leju.com/api/live/getLiveDetailInfo?liveId=" + liveId;
            Map<String, String> param = null;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            String resultString = "";
            CloseableHttpResponse response = null;
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            httpGet.addHeader("Accept", "*/*");
            httpGet.addHeader("Accept-Language", "zh-cn");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
            httpGet.addHeader("Connection", "Keep-Alive");
            httpGet.addHeader("z-source", "3");
            httpGet.addHeader("X-Requested-With", "XMLHttpRequest");
            httpGet.addHeader("Host", "h5-zb.leju.com");

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
//            System.out.println(resultString);
            }
            return response.getHeaders("Set-Cookie");
        }


        public  void post(String liveId, String cookied) throws Exception {
            String url = "http://h5-zb.leju.com/api/v1/plugins/fuelrod/h5/thumbsUp";
            Map<String, String> param = new HashMap<>();
            param.put("liveId", liveId);

            // 创建Httpclient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            String resultString = "";
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Accept-Language", "zh-cn");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
            httpPost.addHeader("Connection", "Keep-Alive");
            httpPost.addHeader("z-source", "3");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Host", "h5-zb.leju.com");
            httpPost.addHeader("Cookie", cookied);

            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(resultString);
        }

        public  void post2(String liveId) throws Exception {
            String url = "http://api-zb.leju.com/v1/mixture/statistic/videoPlayCounter";
            Map<String, String> param = new HashMap<>();
            param.put("liveId", liveId);

            // 创建Httpclient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            String resultString = "";
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Accept-Language", "zh-cn");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
            httpPost.addHeader("Connection", "Keep-Alive");
            httpPost.addHeader("z-source", "3");
            httpPost.addHeader("Host", "api-zb.leju.com");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(resultString);
        }
    }


}
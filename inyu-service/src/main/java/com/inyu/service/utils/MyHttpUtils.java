package com.inyu.service.utils;

import com.inyu.common.BasicResult;
import com.inyu.entity.QuartzProxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义浏览器信息 2getPageInfo
 */
public class MyHttpUtils {

    public static final String TAG = "MyHttpUtils";
    public static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static HttpClientContext context = new HttpClientContext();
    private static Logger logger = LoggerFactory.getLogger(MyHttpUtils.class);



    private MyHttpUtils() {

    }

    public static String sendGet(String url, Map<String,String> headers,QuartzProxy quartzProxy) {
        CloseableHttpResponse response = null;
        String content = null;
        HttpHost proxy=null;
            // 依次是代理地址，代理端口号，协议类型
            if (quartzProxy!=null){
                proxy = new HttpHost(quartzProxy.getIp(), Integer.parseInt(quartzProxy.getPort()));
            }
            RequestConfig config = RequestConfig.custom().setProxy(proxy)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(3000)
                    .build();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(config);
            // nvps是包装请求参数的list
            if (!CollectionUtils.isEmpty(headers)) {
                for (String key : headers.keySet()) {
                    httpGet.setHeader(key,headers.get(key));
                }
            }
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } catch (Exception e) {
                return "EXP";
            }

        return content;
    }

    public static BasicResult sendPost(String url, List<NameValuePair> nvps) {
        CloseableHttpResponse response = null;
        BasicResult bs = new BasicResult();
        String content = null;
        try {
            // 　HttpClient中的post请求包装类
            HttpPost post = new HttpPost(url);
            // nvps是包装请求参数的list
            if (nvps != null) {
                post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            }
            // 执行请求用execute方法，content用来帮我们附带上额外信息
            response = httpClient.execute(post, context);
            // 得到相应实体、包括响应头以及相应内容
            HttpEntity entity = response.getEntity();
            // 得到response的内容
            content = EntityUtils.toString(entity);
            bs.setData(content);
            // System.out.println(TAG + "POST:" + content);
            // 　关闭输入流
            EntityUtils.consume(entity);
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bs;
    }



    /**
     * 构造header
     * @return
     */
    public static HashMap<String, String> initHeaders() {
        ArrayList<String> USER_AGENTS = new ArrayList();
        USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
        USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)");
        USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
        USER_AGENTS.add("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)");
        USER_AGENTS.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)");
        USER_AGENTS.add("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)");
        USER_AGENTS.add("Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; .NET CLR 3.0.04506.30)");
        USER_AGENTS.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) Arora/0.3 (Change: 287 c9dfb30)");
        USER_AGENTS.add("Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6");
        USER_AGENTS.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1");
        USER_AGENTS.add("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0");
        USER_AGENTS.add("Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5)");
        USER_AGENTS.add("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6)");
        USER_AGENTS.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
        USER_AGENTS.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20");
        USER_AGENTS.add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52");

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "www.11467.com");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("Cookie", "mediav=%7B%22eid%22%3A%22260320%22%2C%22ep%22%3A%22%22%2C%22vid%22%3A%22fPa%3AZ%23iXLk%3AoZ2%3C0Wm3%3A%22%2C%22ctn%22%3A%22%22%7D;"
                + " mediav=%7B%22eid%22%3A%22260320%22%2C%22ep%22%3A%22%22%2C%22vid%22%3A%22fPa%3AZ%23iXLk%3AoZ2%3C0Wm3%3A%22%2C%22ctn%22%3A%22%22%7D; "
                + "channelid=0; sid=1526290266591573; _ga=GA1.2.811598351.1526290267; _gid=GA1.2.101351125.1526290267; Qs_lvt_161068=1526290267%2C1526291088; "
                + "Hm_lvt_7ed65b1cc4b810e9fd37959c9bb51b31=1526290267,1526291089; _gat=1; mediav=%7B%22eid%22%3A%22260320%22%2C%22ep%22%3A%22%22%2C%22vid%22%3A%22fPa%3AZ%23iXLk%3AoZ2%3C0Wm3%3A%22%2C%22ctn%22%3A%22%22%7D; "
                + "Qs_pv_161068=4091851921230243000%2C4203865782801775600%2C2477124948762491000%2C2922827072946573300%2C4238660453164286500; Hm_lpvt_7ed65b1cc4b810e9fd37959c9bb51b31=1526291500");
        headers.put("User-Agent", USER_AGENTS.get((int) (Math.random() * 16)));
        return headers;
    }
}

package com.qinbin.p2p.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by teacher on 2016/4/23.
 */
public class OkHttpWrapper implements HttpWrapper {

    private OkHttpClient client;

    private OkHttpWrapper(){
        client = new OkHttpClient();
    }

   static OkHttpWrapper instance = new OkHttpWrapper();

    public static OkHttpWrapper getInstance() {
        return instance;
    }

    @Override
    public InputStream getInputStreamResponse(String url, HttpMethod method, Map<String, String> params) throws IOException{
        return getBody(url,method,params).byteStream();
    }
    @Override
    public Reader getReaderResponse(String url, HttpMethod method, Map<String, String> params) throws IOException{
        return getBody(url,method,params).charStream();
    }
    @Override
    public  String getStringResponse(String url, HttpMethod method, Map<String, String> params) throws IOException{
        return getBody(url,method,params).string();
    }

    private ResponseBody getBody(String url, HttpMethod method, Map<String, String> params) throws IOException {
        return client.newCall(getRequest(url, method, params)).execute().body();
    }

    private Request getRequest(String url, HttpMethod method, Map<String, String> params) {
        Request.Builder requestBuilder = new Request.Builder();
        if(method == HttpMethod.GET){
            requestBuilder.get();
            StringBuilder urlBuilder = new StringBuilder(url);
            // 判断参数是否为空，如果不为空，往urlBuilder中拼接字符串
            if(params!=null && !params.isEmpty()){
                // url = http://www.baidu.com
                // params = user:itcast , password: 123456
                // 结果 http://www.baidu.com?user=itcast&password=123456
                //  params = user:itcast , password: 123&abc=xyz
                // 错误结果 结果 http://www.baidu.com?user=itcast&password=123&abc=xyz
                urlBuilder.append('?');
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    urlBuilder
//                            .append(key)
                            .append(URLEncoder.encode(key))
                            .append('=')
//                            .append(params.get(key))
                            .append(URLEncoder.encode(params.get(key)))
                            .append('&');
                }
                urlBuilder.deleteCharAt(urlBuilder.length()-1);
            }
            
            requestBuilder.url(urlBuilder.toString());
        }else if(method == HttpMethod.POST){
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            if(params!=null && !params.isEmpty()){
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    bodyBuilder.add(key,params.get(key));
                }
            }
            requestBuilder.post(bodyBuilder.build());
            requestBuilder.url(url);
        }else{
            return null;
        }

        return requestBuilder.build();
    }
}

package com.qinbin.p2p.constant;

/**
 * Created by teacher on 2016/4/25.
 */
public class NetConstant {
    public static final String HOST = "http://123.57.42.161:8080/p2p";
//    public static final String HOST = "http://10.0.2.2:8080/p2p";
    public static final String HOME_URL = formatUrl("/HomePageServlet");
    public static final String ALL_PRODUCT_URL = formatUrl("/products.json");
    public static final String LOGIN_URL = formatUrl("/LoginServlet");
    public static final String ACCOUNT_URL = formatUrl("/AccountQueryServlet");

    public static String formatUrl (String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return HOST + url;
        }
    }

}

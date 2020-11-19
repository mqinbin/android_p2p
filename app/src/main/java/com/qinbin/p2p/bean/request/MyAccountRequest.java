package com.qinbin.p2p.bean.request;

import java.util.HashMap;
import java.util.Map;

import com.qinbin.p2p.constant.NetConstant;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.HttpMethod;

/**
 * Created by teacher on 2016/5/7.
 */
public class MyAccountRequest extends BaseRequest {
    public static final String TYPE_ASSET = "assets";
    public static final String TYPE_PROFIT = "profit";
    public static final String TYPE_RATE = "rate";
    String type ;

    public MyAccountRequest(String type) {
        this.type = type;
    }

    @Override
    public String getUrl() {
        return NetConstant.ACCOUNT_URL;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> result= new HashMap<>();
        result.put("type",type);
        return result;
    }
}

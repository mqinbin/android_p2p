package com.qinbin.p2p.bean.request;

import java.util.HashMap;
import java.util.Map;

import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.HttpMethod;

/**
 * Created by teacher on 2016/4/23.
 */
public class TestRequest extends BaseRequest {
    @Override
    // 这个地址是变的
    public String getUrl() {
        return "http://192.168.199.149:8080/p2p/test.json";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("username", "com/qinbin");
        result.put("password","123456&qqh=niqusi");
        return result;
    }
}

package com.qinbin.p2p.bean.request;

import java.util.Map;

import com.qinbin.p2p.constant.NetConstant;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.HttpMethod;

/**
 * Created by teacher on 2016/4/25.
 */
public class HomeRequest extends BaseRequest {
    @Override
    public String getUrl() {
        return NetConstant.HOME_URL;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}

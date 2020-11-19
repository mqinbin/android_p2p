package com.qinbin.p2p.bean.request;

import java.util.HashMap;
import java.util.Map;

import com.qinbin.p2p.constant.NetConstant;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.HttpMethod;

/**
 * Created by teacher on 2016/5/5.
 */
public class LoginRequest extends BaseRequest {

    String userName;
    String passWord;

    public LoginRequest( String userName ,String passWord) {
        this.passWord = passWord;
        this.userName = userName;
    }

    @Override
    public String getUrl() {
        return NetConstant.LOGIN_URL;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> result = new HashMap<>();
        result.put("passWord",passWord);
        result.put("userName",userName);
        return result;
    }
}

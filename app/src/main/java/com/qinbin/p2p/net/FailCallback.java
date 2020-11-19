package com.qinbin.p2p.net;

/**
 * Created by teacher on 2016/4/23.
 */
public interface FailCallback{
    void onFail(BaseRequest request, Exception e);
}

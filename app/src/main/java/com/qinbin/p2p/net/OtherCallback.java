package com.qinbin.p2p.net;

/**
 * Created by teacher on 2016/4/23.
 */
public interface OtherCallback<T extends BaseResponse>{
    void onOther(BaseRequest request, T resp);
}

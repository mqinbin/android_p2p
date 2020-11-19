package com.qinbin.p2p.net;


/**
 * Created by teacher on 2016/4/23.
 */
public interface Callbacks <T extends BaseResponse>{
    void onSuccess(BaseRequest request, T resp);
    void onOther(BaseRequest request, T resp);
    void onFail(BaseRequest request, Exception e);
}

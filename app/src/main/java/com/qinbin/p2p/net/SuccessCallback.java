package com.qinbin.p2p.net;

/**
 * Created by teacher on 2016/4/23.
 */
public interface SuccessCallback  <T extends BaseResponse>{
    void onSuccess(BaseRequest request, T resp);
}

package com.qinbin.p2p.net;

/**
 * Created by teacher on 2016/4/23.
 */
public class CallbacksAdapter<T extends BaseResponse> implements Callbacks<T> {
    SuccessCallback<T> successCallback;
    OtherCallback<T> otherCallback;
    FailCallback failCallback;

    public CallbacksAdapter(SuccessCallback<T> successCallback, OtherCallback<T> otherCallback, FailCallback failCallback) {
        this.successCallback = successCallback;
        this.otherCallback = otherCallback;
        this.failCallback = failCallback;
    }

    @Override
    public void onSuccess(BaseRequest request, T resp) {
        successCallback.onSuccess(request, resp);
    }

    @Override
    public void onOther(BaseRequest request, T resp) {
        otherCallback.onOther(request, resp);
    }

    @Override
    public void onFail(BaseRequest request, Exception e) {
        failCallback.onFail(request, e);
    }
}

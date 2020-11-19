package com.qinbin.p2p.net;

/**
 * Created by teacher on 2016/4/23.
 */
public  class BaseResponse <T> {
    public  int code;
    public  T data;

    public int getCode() {
        return code;
    }
    public T getData() {
        return data;
    }

    public static boolean isSuccess(BaseResponse resp){
        return resp.getCode() >= 200 && resp.getCode() < 300;
    }
}

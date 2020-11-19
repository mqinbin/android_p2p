package com.qinbin.p2p.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.Random;

/**
  Caller->NetUtil: 发送请求
  NetUtil->NetUtil: 检查网络连接
  NetUtil->Caller: 无网络
  NetUtil->NetTask: 开启子线程
  NetTask->NetTask: 从请求中拆出来Url+方式+参数
  NetTask->HttpWrapper:发送请求
  HttpWrapper->NetTask:出现异常
  NetTask->NetTask: 到主线程
  NetTask->Caller:出现异常了
  HttpWrapper->NetTask:得到了结果
  NetTask->NetTask:把结果（流）->对象
  NetTask->NetTask: 成功的结果
  NetTask->NetTask: 非成功结果
  NetTask->NetTask: 到主线程
  NetTask->Caller: 成功了
  NetTask->Caller: 非成功
 */
public class NetUtil {
    static  ConnectivityManager cm;
    public static void init(ConnectivityManager cm){
        NetUtil.cm = cm;
    }
    public static void sendRequest(BaseRequest request, Class<? extends BaseResponse> responseClass, Callbacks callbacks) {
        boolean isNetworkConnect = checkNetwork(cm);
        if (!isNetworkConnect) {
            // 通知调用方，无网络
            callbacks.onFail(request, new IllegalStateException("请检查网络连接"));
            return;
        }

        new NetTask().execute(new NetBean(request, responseClass, callbacks));
    }

    private static boolean checkNetwork(ConnectivityManager cm) {
//        Context context = null;
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] allNetworkInfo = cm.getAllNetworkInfo();
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if(networkInfo.isConnected()){
                return true;
            }
        }

        return false;
    }

    private static class NetBean {
        // Input
        BaseRequest request;
        Class<? extends BaseResponse> responseClass;
        // 桥梁
        Callbacks callbacks;
        // Output
        Exception exception;
        BaseResponse resp;

        public NetBean(BaseRequest request, Class<? extends BaseResponse> responseClass, Callbacks callbacks) {
            this.request = request;
            this.responseClass = responseClass;
            this.callbacks = callbacks;
        }
    }



    private static class NetTask extends AsyncTask<NetBean, Void, NetBean> {
        static Random random = new Random();
        @Override
        protected NetBean doInBackground(NetBean... params) {
            NetBean netBean = params[0];
            BaseRequest request = netBean.request;
            // 可插拔的网络类库
            HttpWrapper httpWrapper = OkHttpWrapper.getInstance();
            try {
                SystemClock.sleep(1000+ random.nextInt(1000));
                Reader reader = httpWrapper.getReaderResponse(request.getUrl(), request.getMethod(), request.getParams());
                Gson gson = new Gson();

                BaseResponse resp = gson.fromJson(reader, netBean.responseClass);
                netBean.resp = resp;
            } catch (IOException   e1) {
                e1.printStackTrace();
                netBean.exception = e1;
            }catch (JsonParseException e2) {
                e2.printStackTrace();
                netBean.exception = e2;
            }
            return netBean;
        }

        @Override
        protected void onPostExecute(NetBean netBean) {
            Callbacks callbacks = netBean.callbacks;
            if (netBean.exception != null) {
                callbacks.onFail(netBean.request, netBean.exception);
            }else{
                BaseResponse resp = netBean.resp;
                if(BaseResponse.isSuccess(resp)){
                    callbacks.onSuccess(netBean.request,resp);
                }else{
                    callbacks.onOther(netBean.request, resp);
                }
            }

        }
    }
}

package com.qinbin.p2p;

import android.app.Application;

import com.qinbin.p2p.utils.AppManager;
import com.qinbin.p2p.utils.UiUtil;


/**
 * Created by teacher on 2016/3/20.
 */
public class MyApp extends Application {


    private Thread.UncaughtExceptionHandler defaultHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init(this);
        UiUtil.init(this);

    }


}

package com.qinbin.p2p;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.qinbin.p2p.utils.AppManager;


/**
 * Created by Q on 2016/4/1.
 */
public class BaseActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        FrameLayout frameLayout = new FrameLayout(this);
        LayoutInflater.from(this).inflate(layoutResID, frameLayout, true);
        super.setContentView(frameLayout);
    }

    @Override
    public void setContentView(View view) {
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(view);
        super.setContentView(frameLayout);

    }
}

package com.qinbin.arl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private AutoRollLayout arl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arl_activity_main);

        arl = (AutoRollLayout) findViewById(R.id.arl_main_arl);
        List<String> items = new ArrayList<>();
//        items.add(new RollItem("吃饭", "http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg"));
//        items.add(new RollItem("喝汤", "http://10.0.2.2:8080/zhbj/10007/1452327318UU92.jpg"));
//        items.add(new RollItem("睡觉", "http://10.0.2.2:8080/zhbj/10007/1452327318UU93.jpg"));
//        items.add(new RollItem("打屁", "http://10.0.2.2:8080/zhbj/10007/1452327318UU94.png"));


        arl.setOnClickListener(this);
        arl.setItems(items);
    }

    public void show4(View v) {
        List<String> items = new ArrayList<>();
//        items.add(new RollItem("吃饭", "http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg"));
//        items.add(new RollItem("喝汤", "http://10.0.2.2:8080/zhbj/10007/1452327318UU92.jpg"));
//        items.add(new RollItem("睡觉", "http://10.0.2.2:8080/zhbj/10007/1452327318UU93.jpg"));
//        items.add(new RollItem("打屁", "http://10.0.2.2:8080/zhbj/10007/1452327318UU94.png"));

        arl.setItems(items);
    }


    public void show2(View v) {
        List<String> items = new ArrayList<>();
//        items.add(new RollItem("喝汤", "http://10.0.2.2:8080/zhbj/10007/1452327318UU92.jpg"));
//        items.add(new RollItem("睡觉", "http://10.0.2.2:8080/zhbj/10007/1452327318UU93.jpg"));

        arl.setItems(items);
    }


    public void show1(View v) {
        List<String> items = new ArrayList<>();
//        items.add(new RollItem("睡觉", "http://10.0.2.2:8080/zhbj/10007/1452327318UU93.jpg"));

        arl.setItems(items);
    }


    public void show0(View v) {
        arl.setItems(new ArrayList<String>());
    }

    public void on(View v) {
        arl.setAutoRoll(true);
    }

    public void off(View v) {
        arl.setAutoRoll(false);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, ""+arl.getCurrentItem(), Toast.LENGTH_SHORT).show();
    }
}

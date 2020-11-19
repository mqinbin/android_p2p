package com.qinbin.p2p;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.qinbin.p2p.adapter.ImagePagerAdapter;

/**
 * Created by teacher on 2016/4/20.
 */
public class Guide2Activity extends BaseActivity {

    private ViewPager vp;
    private ArgbEvaluator argbEvaluator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide2);
        vp = (ViewPager) findViewById(R.id.guide2_vp);
        vp.setAdapter(new ImagePagerAdapter(new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3}, ImageView.ScaleType.CENTER_INSIDE));
        vp.setOnPageChangeListener(pageListener);
        argbEvaluator = new ArgbEvaluator();
        vp.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Guide2Activity.this, MainActivity.class));
            }
        }, 5000);
    }

    int[] bgColors = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.BLUE};
    private ViewPager.OnPageChangeListener pageListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("onPageScrolled", "position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
            int color = (int) argbEvaluator.evaluate(positionOffset, bgColors[position], bgColors[position + 1]);
            vp.setBackgroundColor(color);
        }
    };
}

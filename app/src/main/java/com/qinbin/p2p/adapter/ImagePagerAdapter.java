package com.qinbin.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teacher on 2016/4/20.
 */
public class ImagePagerAdapter extends PagerAdapter {
    int[] picRes;
    ImageView.ScaleType scaleType;
    public ImagePagerAdapter(int[] picRes,ImageView.ScaleType scaleType) {
        this.picRes = picRes;
        this.scaleType = scaleType;
    }

    @Override
    public int getCount() {
        return picRes == null ? 0 : picRes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    List<ImageView> cache = new ArrayList<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (cache.isEmpty()) {
            ImageView iv = new ImageView(container.getContext());
            iv.setScaleType(scaleType);
            cache.add(iv);
        }
        ImageView iv = cache.remove(0);
        iv.setImageResource(picRes[position]);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView iv = (ImageView) object;
        container.removeView(iv);
        iv.setImageResource(0);
        cache.add(iv);
    }
}

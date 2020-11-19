package com.qinbin.arl;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class AutoRollLayout extends FrameLayout implements ViewPager.OnPageChangeListener {
    private List<String> items;
    private LinearLayout dotContainer;
    private ViewPager vp;
    private GestureDetector gestureDetector;


    public AutoRollLayout(Context context) {
        this(context, null);
//        init();
    }

    public AutoRollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        init();
    }

    public AutoRollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.arl_arl_layout, this);
        dotContainer = (LinearLayout) findViewById(R.id.arl_arl_dot_container);
        vp = (ViewPager) findViewById(R.id.arl_arl_vp);

        vp.setOnPageChangeListener(this);
        vp.setOnTouchListener(touchListener);
        gestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    public void setItems(List<String> items) {
        this.items = items;
        // Viewpager
        vp.setAdapter(pagerAdapter);
        // textview
        // dots
        dotContainer.removeAllViews();
        addDots();
        // 初始状态
        onPageSelected(0);
    }

    static Handler handler = new Handler();

    boolean roll;

    public void setAutoRoll(boolean roll) {
        if (this.roll == roll) {
            return;
        }

        this.roll = roll;
        Log.w("arl", "setAutoRoll " + roll);
        if (roll) {
            handler.postDelayed(rollRunnable, 1000);
        } else {
            handler.removeCallbacks(rollRunnable);
        }
    }

    public int getCurrentItem() {
        return vp.getCurrentItem();
    }


    private Runnable rollRunnable = new Runnable() {
        @Override
        public void run() {
            // 避免重复执行
            handler.removeCallbacks(this);
            if (!isTouching) {
                goNextPage();
            }
            handler.postDelayed(this, 1000);
        }
    };


    boolean toRight = true;

    private void goNextPage() {
        //  当只有1张的时候，其实有问题，但viewpager处理了，假装处理一下
        if (pagerAdapter.getCount() == 1) {
            return;
        }

        int currentItem = vp.getCurrentItem();

        if (currentItem == 0) {
            toRight = true;
        }
        if (currentItem == pagerAdapter.getCount() - 1) {
            toRight = false;
        }
        int nextIndex = toRight ? currentItem + 1 : currentItem - 1;


        Log.w("arl", "goNextPage " + items.get(nextIndex));
        vp.setCurrentItem(nextIndex);
    }


    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (cache.isEmpty()) {
                ImageView iv = new ImageView(container.getContext());
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                cache.add(iv);
            }

            ImageView iv = cache.remove(0);
            Picasso.with(getContext())
                    .load(items.get(position))
                    .fit() // 适应imageview的大小
                    .into(iv);
            container.addView(iv);
            return iv;
        }

        List<ImageView> cache = new ArrayList<>();


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView iv = (ImageView) object;
            iv.setImageBitmap(null);
            cache.add(iv);
            container.removeView(iv);
        }
    };


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
//    }

    private void addDots() {
        if (items == null) {
            return;
        }


        int pxFor10Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        for (String item : items) {

            View dot = new View(getContext());
            dot.setBackgroundResource(R.drawable.arl_dot_selector);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pxFor10Dp, pxFor10Dp);
            lp.rightMargin = pxFor10Dp;
//            dot.setLayoutParams(lp);
            dotContainer.addView(dot, lp);

            dot.setOnClickListener(goThisPositionOcl);

        }
    }

    @Override
    public void onPageSelected(int position) {
        if (items == null || items.isEmpty()) {
            return;
        }


        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            dotContainer.getChildAt(i).setEnabled(i != position);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    boolean isTouching = false;

    private OnTouchListener touchListener = new OnTouchListener() {
        // v 设置了监听的view
        // event 收到的触摸事件
        // 返回值：是否消费触摸事件
        // 如果返回true， v的onTouchEvent方法就不会被调用了
        // 如果返回false，v的onTouchEvent方法就  会被调用了
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 停止滚动
//                    handler.removeCallbacks(rollRunnable);
                    isTouching = true;
                    // 如果子控件不想让父控件（包含祖先控件）抢夺触摸事件,
//                    ,就调用.getParent().requestDisallowInterceptTouchEvent(true)
//                    但，这仅仅是“要求”，父控件或祖先控件可以不认同这个“要求”
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e("onTouchEvent", "CANCEL");
                case MotionEvent.ACTION_UP:
                    isTouching = false;
                    // 恢复滚动
//                    handler.postDelayed(rollRunnable,1000);
                    break;
            }

//            boolean match = gestureDetector.onTouchEvent(event);
//            Log.d("ontouch"," " + match);
//            if(event.getAction() == MotionEvent.ACTION_UP){
//                AutoRollLayout.this.performClick();
//            }
//            v.onTouchEvent(event);
//            return true;
            return false;
        }
    };

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        // 下面很多方法的返回值类型是boolean，
        // true 代表命中了
        // 但一般返回false，因为gestureDetector.onTouchEvent(event) 不知道到底是哪种情况返回了true
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            AutoRollLayout.this.performClick();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };


    private OnClickListener goThisPositionOcl = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(v.getContext(), "go this", Toast.LENGTH_SHORT).show();
            int index = dotContainer.indexOfChild(v);
            vp.setCurrentItem(index);
        }
    };
}

package com.qinbin.p2p;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.qinbin.p2p.adapter.ImagePagerAdapter;

/**
 * 1 触摸事件的位置知识、
 * 触摸事件是由用户点击屏幕产生的
 * event.getRawX();                      raw（原始），就是指相对于 **屏幕左上角**的位置
 * event.getX();                         相对于**控件左上角**的位置，是由ViewGroup进行事件分发的时候转化的
 * event.offsetLocation(deltaX,deltaY);  相对偏移:在ViewGroup分发的时候，调用了进行了触摸事件的偏移，孩子分发完了，再偏移回来
 * ViewGroup :  dispatchTouchEvent  --> while (target != null) -->  dispatchTransformedTouchEvent
 * -->  event.offsetLocation(offsetX, offsetY);/handled = child.dispatchTouchEvent(event);event.offsetLocation(-offsetX, -offsetY);
 * * event.setLocation(x,y);            绝对移动: 其实调用了offsetLocation，而offsetLocation调用了native方法
 *
 * 2 View的过度滚动
 * 过度滚动会产出黑色的影子。如何去掉呢？
 *
 */
public class Guide1Activity  extends BaseActivity {

    private ViewPager outerVp;
    private ViewPager innerVp;
    private View touchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide1);
        innerVp = (ViewPager) findViewById(R.id.guide1_inner_vp);
        outerVp = (ViewPager) findViewById(R.id.guide1_outer_vp);
        touchView = findViewById(R.id.guide1_touch_view);
//        touchView.setOnTouchListener(touchListener);
        touchView.setVisibility(View.GONE);

//        innerVp.setScaleX(0.7f);
//        innerVp.setScaleY(0.7f);
        innerVp.setAdapter(new ImagePagerAdapter(innerPics, ImageView.ScaleType.FIT_XY));
        outerVp.setAdapter(new ImagePagerAdapter(outerPics, ImageView.ScaleType.FIT_XY));
        // 去掉过度滚动的黑影
        innerVp.setOverScrollMode(View.OVER_SCROLL_NEVER);
        outerVp.setOverScrollMode(View.OVER_SCROLL_NEVER);

        innerVp.setOnPageChangeListener(innerPageListener);
        outerVp.setOnPageChangeListener(outerPageListener);
    }

    int[] outerPics = new int[]{R.drawable.guiding_img_1, R.drawable.guiding_img_2, R.drawable.guiding_img_3, R.drawable.guiding_img_4};
    int[] innerPics = new int[]{R.drawable.g1, R.drawable.g2, R.drawable.g3, R.drawable.g4};

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // 尝试1 把触摸事件发给两个ViewPager
            // ！不行：innerVp和outerVp的百分比不一样，移动的绝对位置是一样的
//            outerVp.onTouchEvent(event);
//            innerVp.onTouchEvent(event);
            //
            //尝试2 把touchView得到的触摸事件原本的分发给outerVp
            // ！不行：因为两个ViewPager都有一样大小的touchSlop，但做了触摸事件的转换后，对于outer来说，可以动了，但对于inner来说，还没有满足条件
//            outerVp.onTouchEvent(event);
            // 位置偏移
//            float outerInnerWidthRate = 1.0f * outerVp.getWidth() / innerVp.getWidth();
//            event.setLocation(event.getX() / outerInnerWidthRate , event.getY());
            //把偏移后的触摸事件分发给innerVp
//            innerVp.onTouchEvent(event);

            //尝试3 innerVp原来是填充父窗体的，然后对他进行了缩放，这样就不会产生touchSlop的问题了
            // ! 不行： 手指在innerVp上滑动的时候，感觉滑得快，但动的慢
//            outerVp.onTouchEvent(event);
//            innerVp.onTouchEvent(event);
            return true;
        }
    };

    private ViewPager.OnPageChangeListener innerPageListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            outerVp.setCurrentItem(position,false);
        }

        // 当页面发生滚动后会调用此方法
        // position 是左侧页面的角标，
        // positionOffset 是右侧页面宽度露出的百分比
        // positionOffsetPixels 是右侧页面宽度露出的像素绝对值
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            outerVp.scrollTo((int) (outerVp.getWidth() * (position + positionOffset)),0);
        }
    };
    private ViewPager.OnPageChangeListener outerPageListener= new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            innerVp.setCurrentItem(position,false);
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            innerVp.scrollTo((int) (innerVp.getWidth() * (position + positionOffset)), 0);
        }
    };
}

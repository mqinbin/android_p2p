package com.qinbin.p2p.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

/**

 在View播放完补间动画后，如果直接再次播放是不行的
 必须要清除View上的动画,用View. clearAnimation()
 但clearAnimation()会导致动画上的监听的onAnimationEnd方法调用

 */
public class FlyLayout extends FrameLayout {


    private GestureDetector gestureDetector;


    public FlyLayout(Context context) {
        super(context);
        init();
    }

    public FlyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    float criticalVel;
    private void init() {
        gestureDetector = new GestureDetector(getContext(),gestureListener);
        criticalVel= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320,getResources().getDisplayMetrics());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 如果手势探测器在分析触摸事件的时候，没有给过它down事件，就会在日志中大警告
        // 我们需要单独地把down事件交给它
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            gestureDetector.onTouchEvent(ev);
        }
        // 只有移动的时候才拦截
        return ev.getAction() == MotionEvent.ACTION_MOVE;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector.OnGestureListener gestureListener =new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 只有滑动超过一定的速度（320dp/s ==  2英寸/秒），才进行动画
            float vel = (float) Math.hypot(velocityX,velocityY);
            if(vel > criticalVel && !isInAnimation){
                flyTop();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    };

    private void flyTop() {
        Log.d("flyTop","flyTop");
        AnimationSet animationSet  = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1,1.3f,1,1.3f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        scaleAnimation.setDuration(1000);
        animationSet.addAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(500);
//        alphaAnimation动画有延迟
        alphaAnimation.setStartOffset(500);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(animationListener);

        View topView = getChildAt(getChildCount() -1);
        topView.startAnimation(animationSet);

        Log.d("flyTop", "startAnimation");
    }
    boolean isInAnimation = false;
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isInAnimation = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isInAnimation = false;
            Log.d("onAnimationEnd" ,"onAnimationEnd");
            View topView = getChildAt(getChildCount() -1);
            Log.d("onAnimationEnd", "topView tag" + topView.getTag());
//            topView.setVisibility(View.GONE);
            //把topView从此容器中移除掉，在添加到0的位置，0的位置在最下面，就被挡住了
            animation.setAnimationListener(null);
            // 要放在移除view和添加View的前面，因为他会刷新父控件
            topView.clearAnimation();
            Log.d("onAnimationEnd", "clearAnimation");
            removeView(topView);
            addView(topView, 0);
            Log.d("onAnimationEnd", "addView");
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}

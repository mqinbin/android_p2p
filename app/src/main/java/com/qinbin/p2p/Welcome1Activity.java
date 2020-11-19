package com.qinbin.p2p;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * 1 应用打开是会有短暂的白屏， 给欢迎界面的主题上配置上属性 windowBackground ,并指定图片资源作为背景就可以了
 *
 *
 */
public class Welcome1Activity  extends BaseActivity {

    private ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);
        iv = (ImageView)findViewById(R.id.welcome1_iv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimation();
    }



    private void startAnimation() {
        // 缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1,1.1f,1,1.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        scaleAnimation.setDuration(2000);
        // 延迟开始
        scaleAnimation.setStartTime(500);
        // 在动画结束之后，固定下来
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(animationListener);
        iv.startAnimation(scaleAnimation);
        // 动画结束后，过一小会，进入下一个界面
    }

    private Animation.AnimationListener animationListener= new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            handler.postDelayed(goNextUiRunnable,1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    static Handler handler = new Handler();
    Runnable goNextUiRunnable = new Runnable() {
        @Override
        public void run() {
            goNextUi();
        }
    };

    private void goNextUi() {
        startActivity(new Intent(this,Guide1Activity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(goNextUiRunnable);
    }
}

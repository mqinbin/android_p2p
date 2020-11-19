package com.qinbin.p2p;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * 模糊的效果的基本步骤
 *
 * 1 获取清晰的图片（以ImageView显示的为准）
 *   获取View的截图
 *   问题： 什么时候去获取， 怎么获取
 * 2 获得模糊的图片
 *   模糊的过程是耗时的，所以在子线程去完成，我们用AsyncTask
 *
 * 3 模糊的图片逐渐从完全透明到完全不透明
 *
 *
 * 知识点：
 *  ViewTreeObserver
 *
 */
public class Welcome2Activity  extends BaseActivity {

    private ImageView iv;
    private ImageView blurIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        iv = (ImageView) findViewById(R.id.welcome2_iv);
        blurIv = (ImageView) findViewById(R.id.welcome2_blur_iv);

        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // 只调用一次就够了，不需要多次调用，提高效率
                iv.getViewTreeObserver().removeOnPreDrawListener(this);

                Log.d("onPreDraw", "onPreDraw");
                Log.d("onPreDraw","" + iv.getWidth() + "  " + iv.getHeight());

                // 先画一下，不然getDrawingCache获得的是null
                iv.buildDrawingCache();
                // 获取view的截图，在获取之前，应该已经绘制过1编了，或者是需要调用buildDrawingCache，进行强制绘制
                Bitmap drawingCache = iv.getDrawingCache();

                Log.d("drawingCache","" + drawingCache.getWidth() + "  " + drawingCache.getHeight());
                new BlurTask().execute(drawingCache);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimation();
    }

    private void startAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1,1.1f,
                1,1.1f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setAnimationListener(animationListener);
        iv.startAnimation(scaleAnimation);
    }


    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            blurIv.setAlpha(0f);
            blurIv.setScaleX(1.1f);
            blurIv.setScaleY(1.1f);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // blurIv，进行透明都的变化
            ObjectAnimator alpha = ObjectAnimator.ofFloat(blurIv, "alpha", 0, 1).setDuration(2000);
            alpha.addListener(animatorListener);
            alpha.start();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    private Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
           handler.postDelayed(goNextUiRunnable,500);
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
        startActivity(new Intent(this, Guide2Activity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(goNextUiRunnable);
    }


    @SuppressLint("NewApi")
    private Bitmap blur(Bitmap bkg, View view) {

        // 高斯模糊的半径
        float radius = 2;
        // 图片的缩放因子
        float scaleFactor = 2;

        // 创建需要模糊的Bitmap
        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        /**
         * 用RenderScript来实现模糊效果
         */
        // 初始化RenderScript对象
        RenderScript rs = RenderScript.create(Welcome2Activity.this);

        // 为要模糊Bitmap分配内存
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);

        // 创建系统提供的模糊类
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,
                Element.U8_4(rs));

        // 设置模糊半径
        blur.setRadius(radius);

        // 执行渲染
        /**
         * 这里可以创建两个Bitmap一个用于输入一个用于输出，
         * 现在合成一个既是输入也是输出的Bitmap也算是节省的内存
         */
        blur.setInput(overlayAlloc);
        blur.forEach(overlayAlloc);

        // 将Bitmap复制给overlay
        overlayAlloc.copyTo(overlay);

        // 销毁RenderScript
        rs.destroy();

        bkg.recycle();
        return overlay;
    }
    private class BlurTask extends AsyncTask<Bitmap,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            return blur(params[0],blurIv);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            blurIv.setImageBitmap(bitmap);
        }
    }
}

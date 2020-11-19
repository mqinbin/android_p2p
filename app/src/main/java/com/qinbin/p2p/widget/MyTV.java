package com.qinbin.p2p.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 一
 */
public class MyTV extends androidx.appcompat.widget.AppCompatTextView {
    public MyTV(Context context) {
        super(context);
    }

    public MyTV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("MyTV","1 onMeasure");
        Log.d("onMeasure",widthMeasureSpec + " " + heightMeasureSpec);
        Log.d("onMeasure",MeasureSpec.toString(widthMeasureSpec) +" "+ MeasureSpec.toString(heightMeasureSpec) );
        Log.d("onMeasure1",getLayoutParams().height +" "+ getMeasuredHeight() +" "+ getHeight()+" ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("onMeasure2", getLayoutParams().height + " " + getMeasuredHeight() + " " + getHeight() + " ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("MyTV","2 onLayout");
        Log.d("onLayout",getLayoutParams().height +" "+ getMeasuredHeight() +" "+ getHeight()+" ");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("MyTV","3 onDraw");
        Log.d("onDraw",getLayoutParams().height +" "+ getMeasuredHeight() +" "+ getHeight()+" ");
        super.onDraw(canvas);
    }
}

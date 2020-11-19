package com.qinbin.p2p.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 以下假设都没有满足
 * 假设：
 * 1 自己没有padding
 * 2 孩子没有margin
 * 3 孩子都是显示的，没有gone
 * 4 孩子的宽度不会超过此控件的宽度
 * 5 所有孩子的高度都一致，宽度可能不一致
 * 6 此控件具有运行时确定的高度（固定或是match_parent）
 */
public class FlowLayout1 extends ViewGroup {
    public FlowLayout1(Context context) {
        super(context);
    }

    public FlowLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//      super.onMeasure是view.onMeasure，因为ViewGroup没有实现此方法
//      自定义ViewGroup实现onMeasure要做两件事情： 1测量孩子 2设置自己的测量大小
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 此控件的测量宽高
    int myMeasureWidth, myMeasureHeight;

    // 孩子控件的测量宽高
    int childMeasureWidth, childMeasureHeight;

    // 遍历摆放孩子的角的位置
    int startX, startY;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        myMeasureWidth = getMeasuredWidth();
        myMeasureHeight = getMeasuredHeight();
        Log.d("onLayout", "my w" + myMeasureWidth + "  h" + myMeasureHeight);
        startX = 0;
        startY = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();
            Log.d("onLayout", "child w" + childMeasureWidth + "  h" + childMeasureHeight);
//            if(startX + childMeasureWidth <= myMeasureWidth){
//                // 同一行能摆下
//                child.layout(startX,startY,startX +childMeasureWidth,startY+childMeasureHeight);
//                startX = startX + childMeasureWidth;
//            }else{
//                // 同一行摆不下，换行摆
//                startX = 0;
//                startY =startY + childMeasureHeight;
//                child.layout(startX,startY,startX +childMeasureWidth,startY+childMeasureHeight);
//                startX = startX + childMeasureWidth;
//            }
            // 与上方的if else 等价的，增强了代码的复用
            if (startX + childMeasureWidth > myMeasureWidth) {
                // 同一行摆不下，换行摆
                startX = 0;
                startY = startY + childMeasureHeight;
            }
            child.layout(startX, startY, startX + childMeasureWidth, startY + childMeasureHeight);
            startX = startX + childMeasureWidth;

        }


    }
}

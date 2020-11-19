package com.qinbin.p2p.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 假设：
 * [x] 自己没有padding   引出了startX 和 startY的初始值的变化，还引出了判断条件
 * [x] 孩子没有margin    引出孩子的占据的总位置 ； 引出了 摆放位置的变化，应该还偏移margin值； 还引出了startX和startY的计算 ； 自己类中需要一个LayoutParams，还需要实现两个方法 generateLayoutParams generateDefaultLayoutParams
 * 3 孩子都是显示的，没有gone
 * 4 孩子的宽度不会超过此控件的宽度
 * 5 所有孩子的高度都一致，宽度可能不一致
 * 6 此控件具有运行时确定的高度（固定或是match_parent）
 */
public class FlowLayout2 extends ViewGroup {
    public FlowLayout2(Context context) {
        super(context);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
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
    // 孩子控件的占据的宽高  = Measure +margin
    int childTotalWidth, chilTotalHeight;

    // 遍历摆放孩子的角的位置
    int startX, startY;

    // 孩子的上下左右位置
    int childL, childR, childT, childB;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        myMeasureWidth = getMeasuredWidth();
        myMeasureHeight = getMeasuredHeight();
        Log.d("onLayout", "my w" + myMeasureWidth + "  h" + myMeasureHeight);
        startX = getPaddingLeft();
        startY = getPaddingTop();


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {


            View child = getChildAt(i);


            MarginLayoutParams childLp = (MarginLayoutParams) child.getLayoutParams();

            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();

            childTotalWidth = childMeasureWidth + childLp.leftMargin + childLp.rightMargin;
            chilTotalHeight = childMeasureHeight +childLp.topMargin + childLp.bottomMargin;

            Log.d("onLayout", "child w" + childMeasureWidth + "  h" + childMeasureHeight);

            if (startX + childTotalWidth > myMeasureWidth - getPaddingRight()) {
                // 同一行摆不下，换行摆
                startX = getPaddingLeft();
                startY = startY + chilTotalHeight;
            }
            childL = startX + childLp.leftMargin;

            childR = childL + childMeasureWidth; //  == startX +  childTotalWidth - childLp.rightMargin;
            childT = startY + childLp.topMargin;
            childB = childT + childMeasureHeight; // == startY + chilTotalHeight - childLp.bottomMargin；
            child.layout(childL,childT,childR,childB);

            startX = startX +childTotalWidth;

        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(),attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

}

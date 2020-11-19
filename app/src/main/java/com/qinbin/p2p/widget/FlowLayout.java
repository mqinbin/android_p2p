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
 * [x] 孩子都是显示的，没有gone   如果gone，就跳过摆放
 * [x] 孩子的宽度不会超过此控件的宽度   可处理，也可不处理， 处理的话，就用 childR = math.min(childR,myMeasureWidth - getPadingleft-getPaddingRight)属性
 * [x] 所有孩子的高度都一致，宽度可能不一致    计算startY的时候，不能拿换行后的View的高度去处理了， 应该记录一下一行中最高的View的高度，引出变量 lineHeight
 * [x] 此控件具有运行时确定的高度（固定或是match_parent） ，测量中，相当于把摆放的过程回顾一遍，但不摆放，只去记录孩子的宽高的累价值 ,引出了myTotalMeasureHeight,myTotalMeasureWidth 和 heightParentGiveMe.widthParentGiveMe.
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // onMeasure 的自己的宽高的累加值
    int myTotalMeasureHeight, myTotalMeasureWidth;
    // 父控件提供给我们的最大宽高
    int heightParentGiveMe, widthParentGiveMe;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("onMeasure", MeasureSpec.toString(widthMeasureSpec) + "  " + MeasureSpec.toString(heightMeasureSpec));

//      super.onMeasure是view.onMeasure，因为ViewGroup没有实现此方法
//      自定义ViewGroup实现onMeasure要做两件事情：
        // 1测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        //2设置自己的测量大小
//        要考虑3种模式 ，而且要考虑 宽和高
//        最简单是EXACTLY ,  给多大，就用多大
//        次简单的是UNSPECIFIED ，想要多大就要多大
//        最麻烦的是AT_MOST ，需要累加孩子的宽高，如果发现比给的少，就返回累加值，如果发现比给的多，只能返回给的值

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        widthParentGiveMe =  widthMode== MeasureSpec.UNSPECIFIED ?  Integer.MAX_VALUE:MeasureSpec.getSize(widthMeasureSpec);
        heightParentGiveMe = heightMode== MeasureSpec.UNSPECIFIED ?  Integer.MAX_VALUE: MeasureSpec.getSize(heightMeasureSpec);

        myTotalMeasureWidth = 0;
        myTotalMeasureHeight = 0;

        startX = getPaddingLeft();
        startY = getPaddingTop();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams childLp = (MarginLayoutParams) child.getLayoutParams();
            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();
            childTotalWidth = childMeasureWidth + childLp.leftMargin + childLp.rightMargin;
            childTotalHeight = childMeasureHeight + childLp.topMargin + childLp.bottomMargin;

            if (startX + childTotalWidth > widthParentGiveMe - getPaddingRight()) {
                // 同一行摆不下，换行摆
                startX = getPaddingLeft();
                startY = startY + lineHeight;
                lineHeight = 0;
            }
//            childL = startX + childLp.leftMargin;
//
//            childR = childL + childMeasureWidth; //  == startX +  childTotalWidth - childLp.rightMargin;
//            childT = startY + childLp.topMargin;
//            childB = childT + childMeasureHeight; // == startY + childTotalHeight - childLp.bottomMargin；
//            child.layout(childL, childT, childR, childB);
            lineHeight = Math.max(childTotalHeight, lineHeight);
            startX = startX + childTotalWidth;
            // 还需要考虑padding属性
            myTotalMeasureWidth = Math.max(startX + getPaddingRight(), myTotalMeasureWidth);
            myTotalMeasureHeight = Math.max(myTotalMeasureHeight, startY + lineHeight +getPaddingBottom());
        }

        setMeasuredDimension(
                      widthMode == MeasureSpec.EXACTLY? widthParentGiveMe
                    : widthMode == MeasureSpec.UNSPECIFIED  ?myTotalMeasureWidth
                    : widthMode == MeasureSpec.AT_MOST  ?Math.min(widthParentGiveMe,myTotalMeasureWidth)  : 0
                ,
                      heightMode == MeasureSpec.EXACTLY? heightParentGiveMe
                    : heightMode == MeasureSpec.UNSPECIFIED  ?myTotalMeasureHeight
                    : heightMode == MeasureSpec.AT_MOST  ?Math.min(heightParentGiveMe,myTotalMeasureHeight)  : 0
        );

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 此控件的测量宽高
    int myMeasureWidth, myMeasureHeight;

    // 孩子控件的测量宽高
    int childMeasureWidth, childMeasureHeight;
    // 孩子控件的占据的宽高  = Measure +margin
    int childTotalWidth, childTotalHeight;

    // 遍历摆放孩子的角的位置
    int startX, startY;

    // 孩子的上下左右位置
    int childL, childR, childT, childB;

    // “一行”的高度
    int lineHeight;

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
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams childLp = (MarginLayoutParams) child.getLayoutParams();

            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();

            childTotalWidth = childMeasureWidth + childLp.leftMargin + childLp.rightMargin;
            childTotalHeight = childMeasureHeight + childLp.topMargin + childLp.bottomMargin;

            Log.d("onLayout", "child w" + childMeasureWidth + "  h" + childMeasureHeight);

            if (startX + childTotalWidth > myMeasureWidth - getPaddingRight()) {
                // 同一行摆不下，换行摆
                startX = getPaddingLeft();
                startY = startY + lineHeight;
                lineHeight = 0;
            }
            childL = startX + childLp.leftMargin;

            childR = childL + childMeasureWidth; //  == startX +  childTotalWidth - childLp.rightMargin;
            childT = startY + childLp.topMargin;
            childB = childT + childMeasureHeight; // == startY + childTotalHeight - childLp.bottomMargin；
            child.layout(childL, childT, childR, childB);
            lineHeight = Math.max(childTotalHeight, lineHeight);
            startX = startX + childTotalWidth;

        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
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

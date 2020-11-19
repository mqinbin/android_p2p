package com.qinbin.p2p.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 随机布局的特点：子View的摆放位置随机的。
 * <p/>
 * 高度/宽度
 * view.layout() --> setFrame() -> 赋值 mLeft\mTop\mRight\mBottom
 * 也就是说摆放之后能获得宽高
 * 在播放之前，要获得宽高 getMeasuredHeight /getMeasuredWidth
 */
public class RandomLayout extends FrameLayout {
    public RandomLayout(Context context) {
        super(context);
    }

    public RandomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RandomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 随机数生成器
    static Random random = new Random();

    // 多次使用，避免垃圾
    int maxTop;
    int maxLeft;
    int randomTop;
    int randomLeft;


    // 第一版摆放
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//            maxTop = getMeasuredHeight() - child.getMeasuredHeight();
//            maxLeft = getMeasuredWidth() - child.getMeasuredWidth();
//            randomTop = random.nextInt(maxTop);
//            randomLeft = random.nextInt(maxLeft);
//            child.layout(randomLeft, randomTop, randomLeft + child.getMeasuredWidth(), randomTop + child.getMeasuredHeight());
//        }
//    }

    Set<View> layoutedViews = new HashSet<>();

//     第二版
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        layoutedViews.clear();
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = getChildAt(i);
//
//            int tryTime = 0;
//            while (true) {
//                tryTime++;
//                if (tryLayout(child, layoutedViews)) {
//                    layoutedViews.add(child);
//                    break;
//                }
//                if (tryTime >= 10) {
//                    Log.w("onLayout" ,"child " + i + " not layout");
//                    child.layout(-1,-1,-1,-1);
//                    break;
//                }
//            }
//        }
//    }

    List<ViewIndexAndArea> viewIndexAndAreas = new ArrayList<>();
    // 第三版
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutedViews.clear();
        viewIndexAndAreas.clear();
        // 先摆放面积大的，再摆放便面积小的，而不是按照顺序摆放
        // 会提高摆放的成功率
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            viewIndexAndAreas.add(new ViewIndexAndArea(getChildAt(i),i));
        }

        // 按面积大小排序  小->大
        Collections.sort(viewIndexAndAreas);

        // 倒序遍历集合，就可以从大到小了
        for (int i = viewIndexAndAreas.size() - 1; i >= 0; i--) {
            // 获得ViewIndexAndArea中封装的角标
            View child = getChildAt(viewIndexAndAreas.get(i).index);

            int tryTime = 0;
            while (true) {
                tryTime++;
                if (tryLayout(child, layoutedViews)) {
                    layoutedViews.add(child);
                    break;
                }
                if (tryTime >= 10) {
                    Log.w("onLayout", "child " + i + " not layout");
                    // 设置可见性会导致requestLayout方法调用，但requestLayout 会导致父控件的requestLayout调用
                    // 要想让原来摆的"隐藏"，只有把它放在不可见的位置
//                    child.setVisibility(View.GONE);
                    child.layout(-1,-1,-1,-1);
                    break;
                }
            }
        }
    }

    private static class ViewIndexAndArea implements Comparable<ViewIndexAndArea>{
        int index;
        int area;

        public ViewIndexAndArea(View v, int index) {
            this.area = v.getMeasuredHeight() * v.getMeasuredWidth();
            this.index = index;
        }

        @Override
        public int compareTo(ViewIndexAndArea another) {
            // 如果可能产生溢出的话，应该用< > = 去返回
            return area - another.area;
        }
    }

    Rect childRect = new Rect();
    Rect otherChildRect = new Rect();

    private boolean tryLayout(View child, Set<View> layoutedViews) {
        maxLeft = getMeasuredWidth() - child.getMeasuredWidth();
        maxTop = getMeasuredHeight() - child.getMeasuredHeight();
        randomLeft = random.nextInt(maxLeft );
        randomTop = random.nextInt(maxTop);
        childRect.set(randomLeft, randomTop, randomLeft + child.getMeasuredWidth(), randomTop + child.getMeasuredHeight());
        for (View otherChild : layoutedViews) {
            otherChildRect.set(otherChild.getLeft(),otherChild.getTop(),otherChild.getRight(),otherChild.getBottom());
            if(childRect.intersect(otherChildRect)){
                return false;
            }
        }

        child.layout(randomLeft, randomTop, randomLeft + child.getMeasuredWidth(), randomTop + child.getMeasuredHeight());
        return true;
    }


}

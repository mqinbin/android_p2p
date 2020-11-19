package com.qinbin.p2p.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.qinbin.p2p.R;

/**
 * 状态布局，内部维护了状态 和状态对应的View
 * 我们切换状态就可以显示对应的view，这样比较方便
 * 又名LoadingPager
 * <p/>
 * 它有四种状态 ：  正在加载、加载成功、加载出错、无数据
 * <p/>
 * 它应该对外提供什么功能？
 * <p/>
 * 1 外界可以 指定 状态所对应的View(代码执行layout 或view，通过xml指定布局,自定义属性)
 * 2 外界可以 获取 状态所对应的View
 * 3 外界可以 指定 状态
 * 4 外界可以 获得 状态
 * <p/>
 * <p/>
 * 技巧、技能
 * 用数组的数据结构来提高代码的复用性
 * 本质 ： 把代码转化为数据
 *
 *
 * 知识：
 * 安卓注解支持库
 * nullness :  Nullable  NonNull
 *
 * res  XxxRes
 *
 * 常量 intDef StringDef
 *
 *
 */
public class StateLayout extends FrameLayout {
    public static final int STATE_LOADING = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;

    @IntDef({STATE_LOADING,STATE_SUCCESS,STATE_ERROR,STATE_EMPTY})
    public static @interface StateLayoutState{}

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    int[] layoutsRes = new int[4];

    private void init(AttributeSet attrs) {
        // 如果有自定义属性，就填充布局，加入到此控件中
        if (attrs == null) {
            for (int i = 0; i < layoutsRes.length; i++) {
                setStateView(i, 0);
            }
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StateLayout);

//        int loadingLayoutRes = typedArray.getResourceId(R.styleable.StateLayout_loading_layout, 0);
//        int successLayoutRes = typedArray.getResourceId(R.styleable.StateLayout_success_layout, 0);
//        int errorLayoutRes = typedArray.getResourceId(R.styleable.StateLayout_empty_layout, 0);
//        int emptyLayoutRes = typedArray.getResourceId(R.styleable.StateLayout_empty_layout ,0);
        int[] keys = new int[]{
                R.styleable.StateLayout_loading_layout,
                R.styleable.StateLayout_success_layout,
                R.styleable.StateLayout_error_layout,
                R.styleable.StateLayout_empty_layout};
        for (int i = 0; i < keys.length; i++) {
            layoutsRes[i] = typedArray.getResourceId(keys[i], 0);
            setStateView(i, layoutsRes[i]);
        }
        // TypedArray 根本上是从Resources中的mTypedArrayPool去获取的，大小只有5，所以用完要释放
        typedArray.recycle();

    }

    String[] defaultTexts = new String[]{"正在玩命加载中....", "恭喜你!", "请检查网络后重试", "购物车是空的呢"};

    public void setStateView(@StateLayoutState int state, @LayoutRes int layoutRes) {
        // 去判断layoutRes 是否为0，如果为0 就能textview；不为0就填充布局
        View view = null;
        if (layoutRes == 0) {
            TextView tv = new TextView(getContext());
            tv.setText(defaultTexts[state]);
            tv.setGravity(Gravity.CENTER);
            view = tv;
        } else {
            view = View.inflate(getContext(), layoutRes, null);
        }
        setStateView(state, view);
    }

    View[] views = new View[4];

    public void setStateView(@StateLayoutState int state,@NonNull View view) {
        // 添加view 控制显示与否
        if (views[state] != null) {
            removeView(views[state]);
        }
        views[state] = view;
        addView(views[state]);
        //如果状态与当前状态一致，就显示，不一致就隐藏
        view.setVisibility(state == currentState ? View.VISIBLE : View.GONE);
    }

    public View getStateView(@StateLayoutState int state) {
        return views[state];
    }

    int currentState = -1;

    public void setState(@StateLayoutState int state) {
        // 如果currentState 与 state 不一样，隐藏如果currentState对应的布局，显示state的布局
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(i == state ? View.VISIBLE : View.GONE);
        }
        currentState = state;
    }

    public int getState() {
        return currentState;
    }
}

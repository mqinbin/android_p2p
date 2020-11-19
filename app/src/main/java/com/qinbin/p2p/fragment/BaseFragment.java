package com.qinbin.p2p.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qinbin.p2p.R;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.BaseResponse;
import com.qinbin.p2p.net.CallbacksAdapter;
import com.qinbin.p2p.net.FailCallback;
import com.qinbin.p2p.net.NetUtil;
import com.qinbin.p2p.net.OtherCallback;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.StateLayout;

/**
 * 抽取BaseFragment的原因：
 * 一 共同的问题
 * 顶部区域
 * 标题样式
 * 大体布局
 * <p/>
 * 二 不同的问题
 * 顶部区域是否显示
 * 标题文字
 * 标题下方内容
 * <p/>
 * 加载一个StateLayout
 * 把子类的布局给StateLayout作为成功的布局
 * 调用initData，告诉子类去加载数据
 * 子类要借助于BaseFragment的方法去加载数据（因为子类多数时候只想处理成功的响应，失败的给BaseFragment处理）
 * 如果加载失败了，我们给用户一个机会去重新加载（点击错误页面）
 * 时序图
 * participant NetUtil
 * participant StateLayout
 * participant BaseFragment as base
 * participant ChildFragment as child
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * child->child: 用户切换\nonCreateView()
 * child->base: onCreateView()
 * base->base: inflate()\n findViewById()\n得到了顶部容器\n得到了标题TV \n得到了StateLayout
 * base->child: needTitle()
 * base->base: 根据返回值\n显示或隐藏顶部容器
 * base->child: getContentLayoutRes()
 * base->StateLayout: 设置成功状态对应的布局
 * base->child: initView(成功的布局，标题TV)
 * child->child: 设置标题文字\nfindViewById()...
 * base->StateLayout: 设置为正在加载状态
 * base->child: initData()
 * child->base : sendRequest(req,class,successCallback)
 * base->NetUtil: sendRequest(req,class,callbacks);
 * NetUtil->NetUtil: 下载数据\n反序列化json
 * NetUtil->NetUtil: ① 成功
 * NetUtil->child: callbacks.onSuccess()
 * child->StateLayout: 设置状态为成功
 * NetUtil->NetUtil: ② 出错
 * NetUtil->base: callbacks.onFail()
 * base->base: 判断是第一次失败
 * base->StateLayout: 设置状态为错误
 * base->base: 等待用户点击错误页面
 * base->StateLayout: 设置状态为正在加载
 * base->child: initData()
 * base->base: 判断是其他失败\n做提醒
 * NetUtil->NetUtil: ③ 非成功
 * NetUtil->base: callbacks.onOther()
 * base->base: 做提醒
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected static Handler handler = new Handler();

    private TextView titleTv;
    private ViewGroup titleContainer;
    private StateLayout stateLayout;
    private View rootView;
    private View childView;
    private ViewGroup container;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        mChildFragmentManager = getChildFragmentManager();


        // 注意replace的时候的生命周期

        if (rootView == null || !needReuse()) {
            rootView = inflater.inflate(R.layout.fragment_base, container, false);
            titleTv = (TextView) rootView.findViewById(R.id.fragment_base_title_tv);
            titleContainer = (ViewGroup) rootView.findViewById(R.id.fragment_base_title_container);
            stateLayout = (StateLayout) rootView.findViewById(R.id.fragment_base_state_layout);

            //检查是否需要顶部容器
            titleContainer.setVisibility(needTitle() ? View.VISIBLE : View.GONE);
            // 填充子类提供的布局到childContainer中
//            View childView = inflater.inflate(getContentLayoutRes(),childContainer,true);
            // 把子类提供的布局添加到stateLayout做为成功的布局
            stateLayout.setStateView(StateLayout.STATE_SUCCESS, getContentLayoutRes());
            childView = stateLayout.getStateView(StateLayout.STATE_SUCCESS);
            this.container = container;
            initView(childView, titleTv);

            stateLayout.setState(StateLayout.STATE_LOADING);
            initData();
        }
//        container.removeView(rootView);
        return rootView;
    }


    protected StateLayout getStateLayout() {
        return stateLayout;
    }

    // 子类可以告诉BaseFragment 是否需要标题区域
    protected boolean needTitle() {
        return true;
    }

    protected boolean needReuse() {
        return true;
    }

    // 子类可以告诉BaseFragment 下方的内容布局是什么
    @LayoutRes
    protected abstract int getContentLayoutRes();

    // 子类可以去修改顶部标题文字、等操作
    protected abstract void initView(View childView, TextView titleTv);

    protected abstract void initData();

    protected <T extends BaseResponse> void sendRequest(BaseRequest request, Class<T> respClass, SuccessCallback<T> successCallback) {
        NetUtil.init((ConnectivityManager) rootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetUtil.sendRequest(request, respClass, new CallbacksAdapter(successCallback, otherCallback, failCallback));
    }

    private OtherCallback otherCallback = new OtherCallback() {
        @Override
        public void onOther(BaseRequest request, BaseResponse resp) {
            Toast.makeText(rootView.getContext(), "" + resp.getCode(), Toast.LENGTH_SHORT).show();
        }
    };

    private FailCallback failCallback = new FailCallback() {
        @Override
        public void onFail(BaseRequest request, Exception e) {
            //2种情况：
            // 1 第一加载数据失败了，
            if (stateLayout.getState() == StateLayout.STATE_LOADING) {
                // 用户点击失败界面重试
                stateLayout.setState(StateLayout.STATE_ERROR);
                stateLayout.getStateView(StateLayout.STATE_ERROR).setOnClickListener(BaseFragment.this);
            }

            // 2 界面数据有了，在做下拉刷新或是其他操作的时候出错了
            else {
                e.printStackTrace();
                Toast.makeText(rootView.getContext(), "数据获取失败，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View v) {
        stateLayout.setState(StateLayout.STATE_LOADING);
        initData();
    }


}

package com.qinbin.p2p.fragment;

import android.view.View;
import android.widget.TextView;

import com.qinbin.p2p.R;
import com.qinbin.p2p.utils.UiUtil;
import com.qinbin.p2p.widget.FlowLayout;
import com.qinbin.p2p.widget.StateLayout;

/**
一 三个方法的调用时机
1 onMeasure
2 onLayout
3 onDraw

 二 认识onMeasure
 1 干什么的      ：必须要调用setMeasuredDimension。传入测量好的宽高, 不然调用onMeasure的measure方法会抛异常
 2 参数是什么意思 ：int width/heightMeasureSpec:
 width/heightMeasureSpec 是父控件结合自身宽高、以及LayoutParams的属性算出的值
    其中封装了2个信息，模式 和 尺寸
    模式占用int的前2位， 后30位存储size信息
    模式有3种：
    ① UNSPECIFIED   未指定，你想要多大，就多大。这种情况一般发生在可以滚动的布局中，
    ② EXACTLY       如果在xml中，指定了具体的宽度比如160dp，就会得到这个模式，或者在父控件有确定的宽度的时候，此控件是march_parent的，也会得到得到这个模式
    ③ AT_MOST       一般是此控件是wrap_content的，但父控件有确定的宽高、那么子控件不应该超过父控件，就会指定最大的大小
 3 可能会多次调用

 三 宽高相关
  1 getLayoutParams.height      构造结束之后（xml的）就有 ,如果是wrap_content或match_parent 就是负值，如果指定了具体指，就会转化成px
  2 getMeasuredHeight           只有在调用了setMeasuredDimension之后才有
  3 getHeight                   只有在摆放完成之后才有，
 */
public class ProductRecommendFragment extends BaseFragment {

    private FlowLayout flowLayout;
    String[] tags1 = new String[]{"21世纪经济报道", "华尔街见闻", "中国黄金交易网", "CCTV证券资讯网",
            "中国发展研究基金会", "证券日报", "中国民族证券", "新财富杂志", "环球企业家", "中国证券报",
            "证券时报网", "易三板", "中国金融网", "易三板", "未央网", "商学院", "016腾讯博鳌午餐会 2016博鳌三日谈",
            "2016博鳌·谁来赴宴 2016全国两会·财经报道", "2016中国发展高层论坛 2015腾讯财经年会",
            "大国蓄势 革弊立新—2016腾讯财经两会报道", "2016亚布力中国企业家论坛第十六届年会", "2016冬季达沃斯-掌控第四次工业革命",
            "2015年夏季达沃斯论坛-新形势 新挑战", "2014年中央经济工作会议 2014首次降息", "聚焦巴菲特股东大会 全球视野下的A股",
            "《A股大趋势》：下半年A股会怎样？"};
    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_product_recommend;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        flowLayout = (FlowLayout) childView.findViewById(R.id.fragment_recommend_flow_layout);
    }

    @Override
    protected void initData() {
        for (String tag : tags1) {
            TextView textView = UiUtil.createTextView(tag, flowLayout.getContext());
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(4,4,4,4);
            textView.setTextSize(16);
            flowLayout.addView(textView,lp);
        }
        getStateLayout().setState(StateLayout.STATE_SUCCESS);
    }

    @Override
    protected boolean needTitle() {
        return false;
    }
}

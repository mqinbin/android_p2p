package com.qinbin.p2p.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qinbin.p2p.R;
import com.qinbin.p2p.utils.UiUtil;
import com.qinbin.p2p.widget.RandomLayout;
import com.qinbin.p2p.widget.StateLayout;

/**
 * Created by teacher on 2016/4/28.
 */
public class HotProductFragment extends BaseFragment {

    private RandomLayout rl1;
    private RandomLayout rl2;
    private int randomColor;


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_hot_product;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        rl1 = (RandomLayout) childView.findViewById(R.id.hot_rl1);
        rl2 = (RandomLayout) childView.findViewById(R.id.hot_rl2);

        rl1.setClickable(true);
        rl2.setClickable(true);
    }

    String[] tags1 = new String[]{"21世纪经济报道", "华尔街见闻", "中国黄金交易网", "CCTV证券资讯网",
            "中国发展研究基金会", "证券日报", "中国民族证券", "新财富杂志", "环球企业家", "中国证券报",
            "证券时报网", "易三板", "中国金融网", "易三板", "未央网", "商学院", "016腾讯博鳌午餐会 2016博鳌三日谈",
            "2016博鳌·谁来赴宴 2016全国两会·财经报道", "2016中国发展高层论坛 2015腾讯财经年会",
            "大国蓄势 革弊立新—2016腾讯财经两会报道", "2016亚布力中国企业家论坛第十六届年会", "2016冬季达沃斯-掌控第四次工业革命",
            "2015年夏季达沃斯论坛-新形势 新挑战", "2014年中央经济工作会议 2014首次降息", "聚焦巴菲特股东大会 全球视野下的A股",
            "《A股大趋势》：下半年A股会怎样？"};
    String[] tags2 = new String[]{"博鳌亚洲论坛", "哈佛商业评论", "财经国家周刊", "每日经济新闻", "中国企业家",
            "路透中文网", "国际金融报", "中国证券网", "中国经营报", "经济观察报", "中国经济网", "证券市场周刊",
            "财新网", "华夏时报", "第一财经", "FT中文网", "财经网", "创业家", "福布斯", "美通社",
            "2015陆家嘴金融论坛 腾讯投资家陆家嘴晚宴", "2015亚太城市峰会 第4届全球智库峰会",
            "2015博鳌亚洲论坛 生态文明贵阳国际论坛", "2015年315消费新势力 晚会消费在阳光下",
            "2015冬季达沃斯论坛 俄罗斯卢布汇率暴跌", "2014年腾讯财经年终策划——新国富论", "聚焦沪港通 图解沪港通背后的投资机会",};

    @Override
    protected void initData() {
        // 给2个随机布局添加上很多textView
        for (String tag : tags1) {
            TextView textView = UiUtil.createTextView(tag, rl1.getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            textView.setOnClickListener(showToastOcl);
            rl1.addView(textView,lp);
        }
        for (String tag : tags2) {
            TextView textView = UiUtil.createTextView(tag, rl2.getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            textView.setOnClickListener(showToastOcl);
            rl2.addView(textView,lp);
        }
        getStateLayout().setState(StateLayout.STATE_SUCCESS);
    }


    @Override
    protected boolean needTitle() {
        return false;
    }

    private View.OnClickListener showToastOcl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),((TextView) v).getText(),Toast.LENGTH_SHORT).show();
        }
    };
}

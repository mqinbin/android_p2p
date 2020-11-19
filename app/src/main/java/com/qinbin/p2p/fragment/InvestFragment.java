package com.qinbin.p2p.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.p2p.R;
import com.qinbin.p2p.widget.StateLayout;

/**
  ViewPager 和 Fragment一起使用：
 注意：只能使v4中的Fragment

 1 FragmentPagerAdapter
   ① 只会调用一次adapter的getItem方法， 不会反复调用
   ② 在viewpager切换的时候，当前页面1张之外的页面会调用销毁向的方法（除了onDestroy和onDetach） ，再次加载的时候，会调用创建向的生命周期方法，（除了onAttach 和 onCreate）
   ③ 在销毁向的生命周期方法前，！！不！！ 会调用onSaveInstanceState方法
2 FragmentStatePagerAdapter ，相比FragmentPagerAdapter 会记录Fragment的状态
   ① 会反复调用adapter的getItem方法
   ② 在viewpager切换的时候，当前页面1张之外的页面会调用  !所有的 ! 销毁向的方法 ，再次加载的时候，会调用 !所有的 ! 创建向的生命周期方法
   ③ 在销毁向的生命周期方法前，会调用onSaveInstanceState方法，
    我们可以实现此方法去保存一些状态信息 要想获取保存的状态，在 onCreate和onCreateView中都可以获取到

 在开发中如何使用这两个adapter
    当你的ViewPager页面数量比较大的时候用FragmentStatePagerAdapter，反之用FragmentPagerAdapter

 Fragment的嵌套使用
 1 如果在Fragment中又去使用了Fragment，应该使用childFragmentManager去处理
 2 用了childFragmentManager 切换出去会有生命周期方法，切换回来可能有（外层的Fragment的onCreateView没有复用了View）
            ，也可能没有（外层的Fragment的onCreateView复用了View），没有的时候就出错了
 3 如果不在外层的Fragment的onCreateView复用了View复用View 和数据，会造成不好的用户体验

 4 ! 在fragment切换回来的时候会导致ViewPager的onRestoreInstanceState方法调用，ViewPager从中拿到了原来现实的位置，调用了setCurrentItemInternal()
    （创建Fragment的时候）状态的恢复
  v4.Fragment.restoreViewState --> View.restoreHierarchyState -> view.dispatchRestoreInstanceState ...   -> ViewPager.onRestoreInstanceState()
     (销毁Fragment的时候)状态的保存
   ! FragmentManagerImpl.saveFragmentViewState -> View.saveHierarchyState ->view.dispatchSaveInstanceState ... --> ViewPager.onSaveInstanceState()

   ! 保存的时候，传入了一个SparseArray<Parcelable>,我们View会调用onSaveInstanceState()获得自己需要保存的状态，

   ！ 并用自己的ID作为键，把 Parcelable 塞到容器中
      到时候恢复的时候再用ID去取，就可以拿到了
 dispatchRestoreInstanceState / dispatchSaveInstanceState 这两个方法名中有dispatch 就会从外向内遍历所有的View，让它保存自己的状态，
  所以所有的View都有机会保存自己的状态，如果你实现了onSaveInstanceState()方法
 */
public class InvestFragment extends BaseFragment {

    private TabPageIndicator tpi;
    private ViewPager vp;



    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_invest;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        Log.d("InvestFragment","initView");
        vp = (ViewPager)childView.findViewById(R.id.invest_vp);
        Log.d("InvestFragment","vp" + vp);
        Log.d("InvestFragment","vp" + vp.getCurrentItem());
        vp.addOnPageChangeListener(pageListener);
        tpi = (TabPageIndicator ) childView.findViewById(R.id.invest_tpi);
        titleTv.setText("投资");
    }

    private ViewPager.OnPageChangeListener pageListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            Log.d("onPageSelected","position " + position);
        }
    };

    List<Fragment> fragments = new ArrayList<>();
    {
        fragments.add(new AllProductFragment());
        fragments.add(new HotProductFragment());
        fragments.add(new ProductRecommendFragment());
    }
    @Override
    protected void initData() {


        vp.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),fragments));
        tpi.setViewPager(vp);
        getStateLayout().setState(StateLayout.STATE_SUCCESS);
    }

    @Override
    protected boolean needReuse() {
        return false;
    }

    static  String[] titles = new String[]{"产品列表","热门产品","产品推荐"};


    private static class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return titles == null ? 0 : titles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "{" + titles[position] + "}";
        }
        @SuppressLint("LongLogTag")
        @Override
        public Fragment getItem(int position) {
            Log.d("FragmentStatePagerAdapter", "getItem " + position);
            return fragments.get(position);
        }
    }

    private static  class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public int getCount() {
            return titles == null ? 0 : titles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "[" + titles[position] + "]";
        }
        @Override
        public Fragment getItem(int position) {
            Log.d("FragmentPagerAdapter", "getItem " + position);
            return fragments.get(position);
        }


    }

    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return titles == null ? 0 : titles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView result = new TextView(container.getContext());
            result.setText(titles[position]);
            result.setTextColor(Color.GRAY);
            result.setTextSize(80);
            container.addView(result);
            return result;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    };
}

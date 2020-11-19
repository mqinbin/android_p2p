package com.qinbin.p2p;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.p2p.fragment.HomeFragment;
import com.qinbin.p2p.fragment.InvestFragment;
import com.qinbin.p2p.fragment.MeFragment;
import com.qinbin.p2p.fragment.SimpleFragment;

/**
 * 分为上下两部分
 *
 *
 * 下面：
 * 不使用RadioButton，因为drawableTop属性指定的图片，不会自动缩放，不好做适配
 * 而是使用相对布局 + ImageView +TextView去处理
 */
public class MainActivity  extends BaseActivity implements View.OnClickListener {


    private LinearLayout bottom;

    List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = (LinearLayout) findViewById(R.id.main_bottom_container);



//        fragments.add(SimpleFragment.create("首页"));
        fragments.add(new HomeFragment());
//        fragments.add(SimpleFragment.create("投资"));
        fragments.add(new InvestFragment());
//        fragments.add(SimpleFragment.create("我的"));
        fragments.add(new MeFragment());
        fragments.add(SimpleFragment.create("更多"));

        // 遍历bottom的所有的孩子，设置点击事件
        int childCount = bottom.getChildCount();
        for (int i = 0; i < childCount; i++) {
            bottom.getChildAt(i).setOnClickListener(this);
        }

        onClick(bottom.getChildAt(0));
//        bottom.getChildAt(0).performClick();
    }


    @Override
    public void onClick(View v) {
        // 找到点击的index
        int index = bottom.indexOfChild(v);
//        Toast.makeText(MainActivity.this, "" + index, Toast.LENGTH_SHORT).show();
        // 更新底部的Ui
        updateBottomUi(index);
        // 切换Fragment
        switchFragment(index);
    }



    private void updateBottomUi(int index) {
        // 遍历bottom所有的孩子，检查角标，如果和index一样，就enable =false，如果不一样，就true
        int childCount = bottom.getChildCount();
        for (int i = 0; i < childCount; i++) {
            setEnable(bottom.getChildAt(i),i !=index);
        }
    }

    private void switchFragment(int index) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container,fragments.get(index))
                .commit();

    }

    public static void setEnable(View v ,boolean enable){
        v.setEnabled(enable);
        // 如果传入的是ViewGroup，我们还把孩子也递归地设置为同样的enable
        if(v instanceof ViewGroup){
            int childCount = ((ViewGroup) v).getChildCount();
            for (int i = 0; i < childCount; i++) {
                setEnable(((ViewGroup) v).getChildAt(i),enable);
            }
        }
    }
}

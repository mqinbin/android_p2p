package com.qinbin.p2p.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import com.qinbin.p2p.AssetsActivity;
import com.qinbin.p2p.LoginActivity;
import com.qinbin.p2p.PayActivity;
import com.qinbin.p2p.ProfitActivity;
import com.qinbin.p2p.R;
import com.qinbin.p2p.RateActivity;
import com.qinbin.p2p.constant.UserConstant;
import com.qinbin.p2p.widget.StateLayout;

/**
 * Created by teacher on 2016/4/21.
 */
public class MeFragment extends BaseFragment {

    private TextView nameTv;
    private ImageView hederIv;


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        hederIv = (ImageView) childView.findViewById(R.id.me_header);
        nameTv = (TextView) childView.findViewById(R.id.me_name);
        nameTv.setOnClickListener(goLoginOcl);

        childView.findViewById(R.id.me_deposit).setOnClickListener(actionOcl);
        childView.findViewById(R.id.me_withdraw_deposit).setOnClickListener(actionOcl);
        childView.findViewById(R.id.me_my_assets).setOnClickListener(actionOcl);
        childView.findViewById(R.id.me_my_profit).setOnClickListener(actionOcl);
        childView.findViewById(R.id.me_my_yield).setOnClickListener(actionOcl);
    }

    @Override
    protected void initData() {
        getStateLayout().setState(StateLayout.STATE_SUCCESS);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Picasso", "onResume");
        // 修改头像信息、修改用户名称信息
        if (TextUtils.isEmpty(UserConstant.token)) { // 没登陆
            hederIv.setImageResource(R.drawable.user_default);
            nameTv.setText("请登录");
        } else {// 登陆了
            Picasso.with(hederIv.getContext()).load(UserConstant.header).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    Log.e("Picasso", "transform");
                    int size = Math.min(source.getWidth(), source.getHeight());
                    // 创建了方形图片
                    Bitmap result = Bitmap.createBitmap(size, size, source.getConfig());
                    // 利用canvas在bitmap上画东西
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    // 指定画笔的遮罩，而不是图片
                    BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    paint.setShader(shader);
//                    paint.setColor(Color.RED);
                    // 画一个圆
                    canvas.drawCircle(size / 2, size / 2, size / 2, paint);
                    // 释放占用的内存
                    source.recycle();
                    return result;
                }

                @Override
                public String key() {
                    Log.e("Picasso", "key");
                    return "com/qinbin";
                }
            }).into(hederIv);
            nameTv.setText(UserConstant.name);
        }
    }

    @Override
    protected boolean needTitle() {

        return false;
    }

    private View.OnClickListener goLoginOcl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(UserConstant.token)) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        }
    };

    private View.OnClickListener actionOcl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(UserConstant.token)) {
                Toast.makeText(v.getContext(), "请登陆后再做操作", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()) {
                case R.id.me_deposit:
                    startActivity(new Intent(v.getContext(), PayActivity.class));
//                    Toast.makeText(v.getContext(), "去充值", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.me_withdraw_deposit:
                    Toast.makeText(v.getContext(), "去提现", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.me_my_assets:
                    startActivity(new Intent(v.getContext(),AssetsActivity.class));
                    Toast.makeText(v.getContext(), "查看资产", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.me_my_profit:
                    startActivity(new Intent(v.getContext(),ProfitActivity.class));
                    Toast.makeText(v.getContext(), "查看收益", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.me_my_yield:
                    startActivity(new Intent(v.getContext(),RateActivity.class));
                    Toast.makeText(v.getContext(), "查看利率", Toast.LENGTH_SHORT).show();
                    break;


            }

        }
    };
}

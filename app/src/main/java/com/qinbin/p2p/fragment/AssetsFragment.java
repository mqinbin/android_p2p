package com.qinbin.p2p.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.p2p.R;
import com.qinbin.p2p.bean.MyMoney;
import com.qinbin.p2p.bean.request.MyAccountRequest;
import com.qinbin.p2p.bean.response.MyAccountResponse;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.StateLayout;

/**
 * Created by teacher on 2016/5/7.
 */
public class AssetsFragment extends BaseFragment {

    private PieChart chart;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_assets;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        titleTv.setText("我的资产");
        chart = (PieChart) childView.findViewById(R.id.chart);
    }

    @Override
    protected void initData() {
        sendRequest(new MyAccountRequest(MyAccountRequest.TYPE_ASSET),MyAccountResponse.class,successCallback);


    }
        SuccessCallback<MyAccountResponse> successCallback = new SuccessCallback<MyAccountResponse>() {
            @Override
            public void onSuccess(BaseRequest request, MyAccountResponse resp) {
                bindData(resp.getData());
                getStateLayout().setState(StateLayout.STATE_SUCCESS);
            }
        };

    List<String> xVal =new ArrayList<>();{
        xVal.add("理财");
        xVal.add("投资");
        xVal.add("基金");
    }
    private void bindData(MyMoney myMoney) {


        List<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(myMoney.finance,0));
        yVals.add(new Entry(myMoney.invest,1));
        yVals.add(new Entry(myMoney.fund, 2));
        PieDataSet dataSet = new PieDataSet(yVals,"资产分布");
        dataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.BLUE});
        PieData pieData = new PieData(xVal,dataSet);
        chart.setDescription("描述图表的");

        chart.setData(pieData);
//        throw new RuntimeException();
    }
}

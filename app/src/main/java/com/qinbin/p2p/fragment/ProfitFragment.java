package com.qinbin.p2p.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.p2p.R;
import com.qinbin.p2p.bean.MyMoney;
import com.qinbin.p2p.bean.Profit;
import com.qinbin.p2p.bean.request.MyAccountRequest;
import com.qinbin.p2p.bean.response.MyAccountResponse;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.StateLayout;

/**
 * Created by teacher on 2016/5/7.
 */
public class ProfitFragment extends BaseFragment {

    private BarChart chart;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_profit;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        titleTv.setText("我的收益");
        chart = (BarChart) childView.findViewById(R.id.chart);
    }

    @Override
    protected void initData() {
        sendRequest(new MyAccountRequest(MyAccountRequest.TYPE_PROFIT), MyAccountResponse.class, successCallback);


    }

    SuccessCallback<MyAccountResponse> successCallback = new SuccessCallback<MyAccountResponse>() {
        @Override
        public void onSuccess(BaseRequest request, MyAccountResponse resp) {
            bindData(resp.getData());
            getStateLayout().setState(StateLayout.STATE_SUCCESS);
        }
    };

    List<String> xVal = new ArrayList<>();

    {
        xVal.add("理财");
        xVal.add("投资");
        xVal.add("基金");
    }

    private void bindData(MyMoney myMoney) {
        List<Profit> profit = myMoney.profit;


        List<IBarDataSet> dataSet = new ArrayList<>();

        List<BarEntry> yVals1 = new ArrayList<>();
        List<BarEntry> yVals2 = new ArrayList<>();
        BarDataSet barDataSet1 = new BarDataSet(yVals1,"每日收益");
        BarDataSet barDataSet2 = new BarDataSet(yVals2,"每日亏损");

        barDataSet1.setColor(Color.RED);
        barDataSet2.setColor(Color.GREEN);
        dataSet.add(barDataSet1);
        dataSet.add(barDataSet2);
        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < profit.size(); i++) {
            xVals.add(profit.get(i).date);
            yVals1.add(new BarEntry(profit.get(i).profit,i));
            yVals2.add(new BarEntry(profit.get(i).profit + 5,i));
        }

        BarData barData = new BarData(xVals,dataSet);
        chart.setData(barData);
        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        legend.setEnabled(true);
        chart.getAxisRight().setEnabled(false);
    }
}

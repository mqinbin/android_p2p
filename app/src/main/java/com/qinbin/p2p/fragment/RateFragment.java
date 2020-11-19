package com.qinbin.p2p.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import com.qinbin.p2p.R;
import com.qinbin.p2p.bean.MyMoney;
import com.qinbin.p2p.bean.Rate;
import com.qinbin.p2p.bean.request.MyAccountRequest;
import com.qinbin.p2p.bean.response.MyAccountResponse;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.StateLayout;

/**
 * Created by teacher on 2016/5/7.
 */
public class RateFragment extends BaseFragment {

    private LineChart chart;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_rate;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        titleTv.setText("利率变化");
        chart = (LineChart) childView.findViewById(R.id.chart);
    }

    @Override
    protected void initData() {
        sendRequest(new MyAccountRequest(MyAccountRequest.TYPE_RATE), MyAccountResponse.class, successCallback);


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
        List<Rate> rate = myMoney.rate;


        List<ILineDataSet> dataSet = new ArrayList<>();

        List<Entry> yVals1 = new ArrayList<>();
        List<Entry> yVals2 = new ArrayList<>();
        LineDataSet barDataSet1 = new LineDataSet(yVals1,"利率");
        LineDataSet barDataSet2 = new LineDataSet(yVals2,"股市");

        barDataSet1.setColor(Color.RED);
        barDataSet1.setCircleRadius(0);
        barDataSet1.setFillColor(Color.argb(0xFF, 0xFF, 0x00, 0x00));
        barDataSet1.setDrawFilled(true);

        barDataSet2.setColor(Color.GREEN);
        barDataSet2.setCircleRadius(0);
        barDataSet2.setDrawFilled(true);
        barDataSet2.setFillColor(Color.argb(0xFF, 0x00, 0xFF, 0x00));




        dataSet.add(barDataSet2);
        dataSet.add(barDataSet1);
        List<String> xVals = new ArrayList<>();
        for (int i = 0; i < rate.size(); i++) {
            xVals.add(rate.get(i).date);
            yVals1.add(new Entry(rate.get(i).rate,i));
            yVals2.add(new Entry(rate.get(i).rate + 5,i));
        }

        LineData lineData = new LineData(xVals,dataSet);
        chart.setData(lineData);

    }
}

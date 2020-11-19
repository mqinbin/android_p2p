package com.qinbin.p2p.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import com.qinbin.arl.AutoRollLayout;
import com.qinbin.p2p.R;
import com.qinbin.p2p.bean.FinancialProduct;
import com.qinbin.p2p.bean.Fund;
import com.qinbin.p2p.bean.HomeData;
import com.qinbin.p2p.bean.request.HomeRequest;
import com.qinbin.p2p.bean.response.HomeResponse;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.RingProgress;
import com.qinbin.p2p.widget.StateLayout;
import com.squareup.picasso.Picasso;

/**
 *
 */
public class HomeFragment extends BaseFragment {


    private AutoRollLayout arl;
    private ImageView adIv;
    private ViewGroup financeTitle;
    private ViewGroup financeRecommend1;
    private ViewGroup financeRecommend2;
    private ViewGroup investTitle;
    private ViewGroup investItem1;
    private ViewGroup investItem2;
    private ViewGroup fundTitle;
    private ViewGroup fundImagesContainer;
    private ViewGroup fundItem1;
    private ViewGroup fundItem2;
    private ViewGroup consumeTitle;
    private ViewGroup insuranceTitle;
    private ViewGroup otherTitle;
    private ViewGroup consumeImagesContainer;
    private ViewGroup insuranceImagesContainer;
    private ViewGroup otherImagesContainer;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {

        titleTv.setText("首页");

        arl = (AutoRollLayout) childView.findViewById(R.id.home_arl);
        adIv = (ImageView) childView.findViewById(R.id.home_ad);
        financeTitle = (ViewGroup) childView.findViewById(R.id.home_finance);
        financeRecommend1 = (ViewGroup) childView.findViewById(R.id.home_finance_recommend_one);
        financeRecommend2 = (ViewGroup) childView.findViewById(R.id.home_finance_recommend_two);

        investTitle = (ViewGroup) childView.findViewById(R.id.home_invest);
        investItem1 = (ViewGroup) childView.findViewById(R.id.home_invest_one);
        investItem2 = (ViewGroup) childView.findViewById(R.id.home_invest_two);

        fundTitle = (ViewGroup) childView.findViewById(R.id.home_fund);
        fundImagesContainer = (ViewGroup) childView.findViewById(R.id.home_fund_images);
        fundItem1 = (ViewGroup) childView.findViewById(R.id.home_fund_one);
        fundItem2 = (ViewGroup) childView.findViewById(R.id.home_fund_two);

        consumeTitle = (ViewGroup) childView.findViewById(R.id.home_consume);
        insuranceTitle = (ViewGroup) childView.findViewById(R.id.home_insurance);
        otherTitle = (ViewGroup) childView.findViewById(R.id.home_other);

        consumeImagesContainer = (ViewGroup) childView.findViewById(R.id.home_consume_images);
        insuranceImagesContainer = (ViewGroup) childView.findViewById(R.id.home_insurance_images);
        otherImagesContainer = (ViewGroup) childView.findViewById(R.id.home_other_images);


    }

    @Override
    protected void initData() {
//     不直接用   NetUtil，因为用了就要实现Callbacks，就要实现3个方法
//      我们只想实现1个成功的方法，其他的让父类去实现
        sendRequest(new HomeRequest(), HomeResponse.class, successCallback);
    }

    private SuccessCallback<HomeResponse> successCallback = new SuccessCallback<HomeResponse>() {
        @Override
        public void onSuccess(BaseRequest request, HomeResponse resp) {
            bindData(resp.getData());

            // 把statelayout的状态切换为成功
            getStateLayout().setState(StateLayout.STATE_SUCCESS);
        }
    };

    private void bindData(HomeData data) {
        arl.setItems(data.topRollImages);
        Picasso.with(adIv.getContext()).load(data.advertisement).into(adIv);

        ((TextView) financeTitle.findViewById(R.id.part_title)).setText(data.financialRecommend.title);
        updateFinanceRecommend(financeRecommend1, data.financialRecommend.financialProducts.get(0));
        updateFinanceRecommend(financeRecommend2, data.financialRecommend.financialProducts.get(1));

        ((TextView) investTitle.findViewById(R.id.part_title)).setText(data.investmentCash.title);
        updateInvestItem(investItem1, data.investmentCash.financialProducts.get(0));
        updateInvestItem(investItem2, data.investmentCash.financialProducts.get(1));

        ((TextView) fundTitle.findViewById(R.id.part_title)).setText(data.fundSupermarket.title);
        updateImages(fundImagesContainer, data.fundSupermarket.images);
        updateFundItem(fundItem1, data.fundSupermarket.funds.get(0));
        updateFundItem(fundItem2, data.fundSupermarket.funds.get(1));

        ((TextView) consumeTitle.findViewById(R.id.part_title)).setText(data.consumptionCredit.title);
        updateImages(consumeImagesContainer, data.consumptionCredit.images);
        ((TextView) insuranceTitle.findViewById(R.id.part_title)).setText(data.insurance.title);
        updateImages(insuranceImagesContainer, data.insurance.images);
        ((TextView) otherTitle.findViewById(R.id.part_title)).setText(data.otherService.title);
        updateImages(otherImagesContainer, data.otherService.images);
    }


    private void updateInvestItem(ViewGroup investItem, FinancialProduct financialProduct) {
        ((TextView) investItem.findViewById(R.id.part_invest_name)).setText(financialProduct.name);
        ((TextView) investItem.findViewById(R.id.part_invest_rate)).setText(String.format("%.2f", financialProduct.annualYield));
        ((TextView) investItem.findViewById(R.id.part_invest_days)).setText(financialProduct.days + "");
        RingProgress ringProgress = (RingProgress) investItem.findViewById(R.id.part_invest_progress);
        ringProgress.setStrokeWidth(15);
        ringProgress.setProgress(financialProduct.raiseProgress / 100);

    }

    private void updateFinanceRecommend(ViewGroup financeRecommend, FinancialProduct financialProduct) {
        ((TextView) financeRecommend.findViewById(R.id.part_finance_name)).setText(financialProduct.name);
        ((TextView) financeRecommend.findViewById(R.id.part_finance_rate)).setText(String.format("%.2f", financialProduct.annualYield));
        ((TextView) financeRecommend.findViewById(R.id.part_finance_days)).setText(financialProduct.days + "");
        ((TextView) financeRecommend.findViewById(R.id.part_finance_start_money)).setText(financialProduct.price + "");

    }

    private void updateImages(ViewGroup imagesContainer, List<String> images) {
        LinearLayout ivContainer = (LinearLayout) imagesContainer.findViewById(R.id.part_images_container);
        int childCount = ivContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView child = (ImageView) ivContainer.getChildAt(i);
            if (i < images.size()) {
                child.setVisibility(View.VISIBLE);
                Picasso.with(child.getContext()).load(images.get(i)).into(child);
            } else {
                child.setVisibility(View.GONE);
                child.setImageBitmap(null);
            }
        }
    }

    private void updateFundItem(ViewGroup fundItem, Fund fund) {
        ((TextView) fundItem.findViewById(R.id.part_fund_name)).setText(fund.name);
        ((TextView) fundItem.findViewById(R.id.part_fund_type)).setText(fund.isBond ? "债券型" : "混合型");
        ((TextView) fundItem.findViewById(R.id.part_fund_net_worth)).setText(String.format("%.2f", fund.netWorth));
        TextView raiseTv = (TextView) fundItem.findViewById(R.id.part_fund_raise);
        raiseTv.setText(String.format("%.2f", fund.rise));
        raiseTv.setTextColor(fund.rise > 0 ? Color.RED : Color.GRAY);
    }


}

package com.qinbin.p2p.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qinbin.p2p.R;
import com.qinbin.p2p.adapter.ProductsAdapter;
import com.qinbin.p2p.bean.AllProduct;
import com.qinbin.p2p.bean.FinanceInfo;
import com.qinbin.p2p.bean.FundInfo;
import com.qinbin.p2p.bean.InvestInfo;
import com.qinbin.p2p.bean.ProductInfo;
import com.qinbin.p2p.bean.request.AllProductRequest;
import com.qinbin.p2p.bean.response.AllProductResponse;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.StateLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by teacher on 2016/4/28.
 */
public class AllProductFragment extends BaseFragment {


    private RecyclerView rv;
    private ProductsAdapter adapter;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_all_products;
    }

    @Override
    protected void initView(View childView, TextView titleTv) {
        rv = (RecyclerView) childView.findViewById(R.id.fragment_all_product_rv);
        rv.setLayoutManager(new LinearLayoutManager(childView.getContext()));
        adapter = new ProductsAdapter();
        rv.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        sendRequest(new AllProductRequest(), AllProductResponse.class,successCallback);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getStateLayout().setState(StateLayout.STATE_SUCCESS);
//            }
//        }, 2000);
    }

    private SuccessCallback<AllProductResponse> successCallback = new SuccessCallback<AllProductResponse>() {
        @Override
        public void onSuccess(BaseRequest request, AllProductResponse resp) {
            AllProduct allProduct = resp.getData();
            // 先把不同类型的数据转化为同一种数据类型（包含3种数据的所有字段）
            List<ProductInfo> productInfos = new ArrayList<>();
            for (FinanceInfo financeInfo:allProduct.finance){
                productInfos.add(new ProductInfo(financeInfo));
            }
            for (FundInfo fundInfo : allProduct.fund) {
                productInfos.add(new ProductInfo(fundInfo));
            }
            for (InvestInfo investInfo : allProduct.invest) {
                productInfos.add(new ProductInfo(investInfo));
            }
            // 随机打乱顺序
            Collections.shuffle(productInfos);
            // 显示到RecyclerView中
            adapter.setProductInfos(productInfos);
            getStateLayout().setState(StateLayout.STATE_SUCCESS);
        }
    };

    @Override
    protected boolean needTitle() {
        return false;
    }
}

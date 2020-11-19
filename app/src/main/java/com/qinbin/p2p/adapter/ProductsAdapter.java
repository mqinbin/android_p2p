package com.qinbin.p2p.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.qinbin.p2p.R;
import com.qinbin.p2p.bean.ProductInfo;
import com.qinbin.p2p.widget.RingProgress;

/**
 如何显示不同外观的条目
 一 在ListView中显示不同的item
 额外实现adapter的  getViewTypeCount getItemViewType
 注意，getViewTypeCount相当于返回数组的长度，  getItemViewType 相对于返回数组元素的角标
 getItemViewType 方法会提供position，可以通过position获得数据的类型
 在getView方法中，如果发现这种类型的View还没创建过，就会让你去创建，如果移除屏幕了，就再次提供复用
 <p/>
 二 在RecyclerView中如何显示不同的item
 额外实现adapter的 getItemViewType
 getItemViewType 方法会提供position，可以通过position获得数据的类型
 在onCreateViewHolder中，就提供了类型，可以去加载不同的item布局了，我们可以根据情况返回出不同的ViewHolder
 在onBindViewHolder中，我们就可以得到对应类型的ViewHolder


 从xml加载布局为View的三种方式
 1 View.inflate(Context context, @LayoutRes int resource, ViewGroup root)
 2  layoutInflater.inflate(@LayoutRes int resource, @Nullable ViewGroup root)
 1和2 其实是一样的
 如果root 是null ，则填充出来的布局没有parent，需要手动添加到parent中
 如果root不是null ，则填充出来的布局有parent，如果再次add到其他的ViewGroup中会出错

 3  layoutInflater.inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)
 attachToRoot 表示是否添加到root中
 如果attachToRoot = true ，效果等同于2
 如果attachToRoot =false ，则现在还没有parent，但是View上会有LayoutParams属性

 */
public class ProductsAdapter extends RecyclerView.Adapter {


    List<ProductInfo> productInfos;

    @Override
    public int getItemCount() {

        return productInfos == null ? 0 : productInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return productInfos.get(position).viewType;
    }

    int[] textColors = new int[]{Color.RED, Color.GREEN, Color.BLUE};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
//            View itemView = View.inflate(parent.getContext(), R.layout.item_finance, null);
            View itemView = layoutInflater.inflate(R.layout.item_finance,parent,false);
            return new FinanceViewHolder(itemView);
        } else if (viewType == 1) {
//            View itemView = View.inflate(parent.getContext(), R.layout.item_fund, null);
            View itemView = layoutInflater.inflate(R.layout.item_fund,parent,false);
            return new FundViewHolder(itemView);
        } else if(viewType == 2){
//            View itemView = View.inflate(parent.getContext(), R.layout.item_invest, null);
            View itemView = layoutInflater.inflate(R.layout.item_invest,parent,false);
            return new InvestViewHolder(itemView);

        }else {
            TextView tv = (TextView) View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null);
            tv.setTextColor(textColors[viewType]);
            return new NormalViewHolder(tv);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductInfo productInfo = productInfos.get(position);
//        if (holder instanceof FinanceViewHolder) {
//            ((FinanceViewHolder) holder).bind(productInfo);
//        } else if (holder instanceof  FundViewHolder) {
//            ((FundViewHolder) holder).bind(productInfo);
//        }else if (holder instanceof  InvestViewHolder) {
//            ((InvestViewHolder) holder).bind(productInfo);
//        }
        if(holder instanceof ProductViewHolder){
            ((ProductViewHolder) holder).bind(productInfo);
        }
        else {
            ((TextView) holder.itemView).setText(productInfo.productName);
        }
    }


    public void setProductInfos(List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
        notifyDataSetChanged();
    }


    private static class NormalViewHolder extends RecyclerView.ViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static abstract class ProductViewHolder extends RecyclerView.ViewHolder{

        public ProductViewHolder(View itemView) {
            super(itemView);
        }
        public abstract void bind(ProductInfo productInfo);
    }

    private static class FinanceViewHolder extends ProductViewHolder {
        TextView nameTv;
        TextView tagTv;
        RingProgress progressRp;
        TextView rateTv;
        TextView dayTv;
        Button actionBtn;

        public FinanceViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_finance_name);
            tagTv = (TextView) itemView.findViewById(R.id.item_finance_tag);
            progressRp = (RingProgress) itemView.findViewById(R.id.item_finance_progress);
            rateTv = (TextView) itemView.findViewById(R.id.item_finance_rate);
            dayTv = (TextView) itemView.findViewById(R.id.item_finance_days);
            actionBtn = (Button) itemView.findViewById(R.id.item_finance_action);
        }

        public void bind(ProductInfo productInfo) {
            nameTv.setText(productInfo.productName);
            tagTv.setText(productInfo.prdMark);
            float percent = Float.parseFloat(productInfo.sellPer.substring(0, productInfo.sellPer.length() - 1)) / 100f;
            progressRp.setProgress(percent);
            rateTv.setText(productInfo.predictYearRate);
            dayTv.setText(productInfo.timeLimit + "");
            actionBtn.setText(productInfo.statusDesc);
            actionBtn.setEnabled(productInfo.purchaseFlag);
        }
    }

    private static class FundViewHolder extends ProductViewHolder {
        TextView nameTv;
        LinearLayout tagContainer;
        TextView netWorthTv;
        TextView raiseTv;

        public FundViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_fund_name);
            tagContainer = (LinearLayout) itemView.findViewById(R.id.item_fund_tag_container);
            netWorthTv = (TextView) itemView.findViewById(R.id.item_fund_net_worth);
            raiseTv = (TextView) itemView.findViewById(R.id.item_fund_raise);
        }

        static String[] riskDesc = new String[]{"较低风险", "中等风险", "较高风险"};

        public void bind(ProductInfo productInfo) {
            nameTv.setText(productInfo.productName);
            ((TextView) tagContainer.getChildAt(0)).setText(productInfo.fundDesc);
            ((TextView) tagContainer.getChildAt(1)).setText(riskDesc[productInfo.prdRiskLevel - 2]);
            ((TextView) tagContainer.getChildAt(1)).setText(productInfo.lowerApplyAmount + "元起投");
            netWorthTv.setText(productInfo.newUnit + "");
            raiseTv.setText(productInfo.lastYearIncreaseStr);
            raiseTv.setTextColor(productInfo.lastYearIncreaseStr.charAt(0) == '-' ? Color.GREEN : Color.RED);
        }
    }

    private static class InvestViewHolder extends ProductViewHolder {
        TextView nameTv;
        RingProgress progressRp;
        TextView rateTv;
        TextView dayTv;
        Button actionBtn;

        public InvestViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.item_invest_name);
            progressRp = (RingProgress) itemView.findViewById(R.id.item_invest_progress);
            rateTv = (TextView) itemView.findViewById(R.id.item_invest_rate);
            dayTv = (TextView) itemView.findViewById(R.id.item_invest_days);
            actionBtn = (Button) itemView.findViewById(R.id.item_invest_action);
        }

        public void bind(ProductInfo productInfo) {
            nameTv.setText(productInfo.productName);
            float percent = Float.parseFloat(productInfo.sellPer) / 100f;
            progressRp.setProgress(percent );
            rateTv.setText(productInfo.predictYearRate + "%");
            dayTv.setText(productInfo.timeLimit + "天");
            actionBtn.setText(productInfo.statusDesc);
            actionBtn.setEnabled(productInfo.purchaseFlag);
        }
    }
}

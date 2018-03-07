package com.wuxiao.yourday.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wuxiao.yourday.R;
import com.wuxiao.yourday.bean.Product;
import com.wuxiao.yourday.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/4/11.
 */

public class SearchProductAdapter extends BaseQuickAdapter<Product.PrdListBean, BaseViewHolder> {
    private ArrayList<String> list;

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }


    public SearchProductAdapter(List<Product.PrdListBean> data, ArrayList<String>list) {
        super(R.layout.search_product_item, data);
        this.list=list;

    }


    @Override
    protected void convert(BaseViewHolder helper, Product.PrdListBean item) {

        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tv_ProductName, item.getName())
                .setText(R.id.tv_Summry, "-" + item.getSummary());

        if (item.getRate().length() > 6) {
            helper.setText(R.id.rate, "0.09%");
        } else {
            helper.setText(R.id.rate, item.getRate());
        }
            helper.setText(R.id.tv_people, list.get(layoutPosition+1) + "人");
        Glide.with(mContext).load(Constants.Times.piURL + item.getLogo()).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into((ImageView) helper.getView(R.id.head));
    }
}

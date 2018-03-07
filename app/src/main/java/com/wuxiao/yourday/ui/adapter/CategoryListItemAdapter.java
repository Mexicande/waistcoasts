package com.wuxiao.yourday.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseRecyclerViewAdapter;
import com.wuxiao.yourday.model.Category;

import java.util.List;

/**
 * 分类列表item Adapter
 * Created by lihuabin on 2016/11/2.
 */
public class CategoryListItemAdapter extends BaseRecyclerViewAdapter<Category> {

    public CategoryListItemAdapter(Context mContext) {
        super(mContext, R.layout.category_list_item);
    }

    public CategoryListItemAdapter(Context mContext, List<Category> data) {
        super(mContext, R.layout.category_list_item, data);
    }

    @Override
    protected void fillData(View itemView, Category model) {
        ImageView categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
        TextView categoryName = (TextView) itemView.findViewById(R.id.category_title);
        categoryIcon.setImageResource(model.getCategoryIconRId());
        categoryName.setText(model.getCategoryName());
    }

}





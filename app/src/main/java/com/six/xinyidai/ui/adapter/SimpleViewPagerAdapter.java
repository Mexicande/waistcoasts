package com.six.xinyidai.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihuabin on 2016/11/7.
 */
public class SimpleViewPagerAdapter extends PagerAdapter{
    Context mContext;
    List<View> itemViewList;

    public SimpleViewPagerAdapter(Context mContext) {
        this.mContext = mContext;
        itemViewList = new ArrayList<View>();
    }

    public SimpleViewPagerAdapter( Context mContext,List<View> itemViewList) {
        this.itemViewList = itemViewList;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return itemViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(itemViewList.get(position));
        return itemViewList.get(position);
    }
}

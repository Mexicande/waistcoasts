package com.wuxiao.yourday.ui.activity;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lihuabin on 2016/12/12.
 */
public class AddCategoryActivity  extends BaseActivity{
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_category;
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}


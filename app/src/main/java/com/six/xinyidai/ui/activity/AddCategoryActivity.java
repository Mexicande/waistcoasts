package com.six.xinyidai.ui.activity;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseActivity;
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


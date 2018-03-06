package com.six.xinyidai.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lihuabin on 2016/12/14.
 */
public class AboutActivity extends BaseActivity{
    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.about_us);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            startActivity(new Intent(mContext,SurprisedActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

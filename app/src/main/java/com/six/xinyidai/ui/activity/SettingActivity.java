package com.six.xinyidai.ui.activity;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lihuabin on 2016/12/14.
 */
public class SettingActivity extends BaseActivity {
    TextView colorSubTitle;
    Switch colorfulSwitch;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        colorSubTitle = (TextView) findViewById(R.id.colorful_subtitle);
        colorfulSwitch = (Switch) findViewById(R.id.colorful_switch);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        setToolbar(R.string.setting_activity_title);
        config();
        colorfulSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                app.setColorful(b);
                if (b) {
                    colorSubTitle.setText(getString(R.string.colorful_ui_description, "已"));
                } else {
                    colorSubTitle.setText(getString(R.string.colorful_ui_description, "未"));
                }

            }
        });
    }


    private void config() {
        colorfulSwitch.setChecked(app.isColorful());
        if (app.isColorful()) {
            colorSubTitle.setText(getString(R.string.colorful_ui_description, "已"));
        } else {
            colorSubTitle.setText(getString(R.string.colorful_ui_description, "未"));
        }
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


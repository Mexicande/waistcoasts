package com.six.xinyidai.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.six.xinyidai.App;
import com.six.xinyidai.R;
import com.six.xinyidai.util.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected EventBus eventBus;
    protected Context mContext;
    protected Intent mIntent;
    protected Bundle mBundle;
    protected App app;
    protected Toolbar toolbar;
    protected FirebaseAnalytics mFirebaseAnalytics;//FirebaseAnalytics

    protected static String NEXT_JUMP = "NEXT_JUMP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mContext = this;
        app = App.context();
        eventBus = EventBus.getDefault();
        mIntent = getIntent();
        if (mIntent != null) {
            mBundle = mIntent.getExtras();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow(); // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        initTranslucent(true);
        assignViews();
        onViewReady();
    }

    protected void assignViews() {
    }

    protected void onViewReady() {
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 设置布局文件资源
     * 重写 return 对应资源id
     */
    public abstract int getLayoutId();


    protected void onDestroy() {
        if (eventBus != null && eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        super.onDestroy();

    }

    private void initState() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //这个属性4.4算是全透明（有的机子是过渡形式的透明），5.0就是半透明了 我的模拟器、真机都是半透明，
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

    public SystemBarTintManager tintManager;

    /**
     * flag=true表示状态栏颜色为  tintManager.setStatusBarTintResource(R.color.white);
     * flag=false表示状态栏颜色与actionbar位置颜色一致
     *
     * @param flag
     */
    protected void initTranslucent(boolean flag) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTranslucentStatus(true);
        if (null == tintManager)
            tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(flag);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setToolbar(int titleRId) {
        if (toolbar != null) {
            toolbar.setTitle(getString(titleRId));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
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

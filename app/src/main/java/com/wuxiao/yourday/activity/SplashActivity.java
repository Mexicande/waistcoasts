package com.wuxiao.yourday.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.wuxiao.yourday.App;
import com.wuxiao.yourday.MainActivity;
import com.wuxiao.yourday.task.GetImageBean;
import com.wuxiao.yourday.task.GetProduct;
import com.wuxiao.yourday.task.GetProduct1;
import com.wuxiao.yourday.task.GetProduct2;
import com.wuxiao.yourday.task.GetProduct3;
import com.wuxiao.yourday.ui.activity.HomeActivity;
import com.wuxiao.yourday.ui.activity.XianJinchaoren;
import com.wuxiao.yourday.util.SPUtils;
import com.wuxiao.yourday.util.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    private SwitchHandler mHandler = new SwitchHandler(this);
    private static  final String URL="http://test.api.anwenqianbao.com/v0/majiahao/getStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        //诸葛初始化分析跟踪

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean url1 = SPUtils.contains(this, "url");
        if(!url1){
            setUrl();
        }else {
            TextNet();
            boolean flag = SPUtils.contains(this, "userId");
            if(flag){
                App.userId= (String) SPUtils.get(this,"userId","");
                mHandler.sendEmptyMessageDelayed(3, 1000);
            }else {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    }
    private void setUrl() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "打个白条");
        JSONObject jsonObject = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("error_code");
                            if ("0".equals(result)) {
                                SPUtils.put(SplashActivity.this, "url", URL);
                                //执行相关操作
                                getData() ;
                                setWelcome();
                            } else {
                                mHandler.sendEmptyMessageDelayed(2, 1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.sendEmptyMessageDelayed(2, 1000);
            }
        });
        App.getVolleyRequestQueue().add(jsonObjectRequest);
    }

    private void TextNet() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {
            //执行相关操作
            getData();
        } else {
            Toast.makeText(this,
                    "亲，网络连接失败咯！", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void getData() {
        //执行相关操作
        new GetImageBean(SplashActivity.this).execute();
        new GetProduct(SplashActivity.this).execute();
        new GetProduct3(SplashActivity.this).execute();
        new GetProduct1(SplashActivity.this).execute();
        new GetProduct2(SplashActivity.this).execute();
    }
    private void setWelcome( ){
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            mHandler.sendEmptyMessageDelayed(4, 1000);
            return;
        }else {
            mHandler.sendEmptyMessageDelayed(2, 1000);
        }
    }

    private static class SwitchHandler extends Handler {
        private WeakReference<SplashActivity> mWeakReference;

        SwitchHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<SplashActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mWeakReference.get();
                if (activity != null) {
                    switch (msg.what){
                        case 1:
                            XianJinchaoren.launch(activity);
                            activity.finish();
                            break;
                        case 2:
                            MainActivity.launch(activity);
                            activity.finish();
                            break;
                        case 3:
                            HomeActivity.launch(activity);
                            activity.finish();
                            break;
                        case 4:
                            Intent intent = new Intent(activity, GuideActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                    }
                }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return false;
        }
            return super.onKeyDown(keyCode, event);
    }
}

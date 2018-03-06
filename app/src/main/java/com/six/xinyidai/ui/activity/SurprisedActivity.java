package com.six.xinyidai.ui.activity;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.six.xinyidai.R;
import com.six.xinyidai.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 彩蛋
 * Created by lihuabin on 2016/12/14.
 */
public class SurprisedActivity extends BaseActivity {
    WebView gifView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_surprised;
    }

    @Override
    protected void assignViews() {
        super.assignViews();
        gifView = (WebView) findViewById(R.id.gif_view);
    }

    @Override
    protected void onViewReady() {
        super.onViewReady();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initTranslucent(false);
        AlertDialog dialog= new AlertDialog.Builder(mContext)
                .setTitle("恭喜")
                .setMessage("全世界只有1%的人能发现这里!\n本页面优先使用本地缓存，仅首次加载需要极少流量，非首次进入和玩耍过程基本不消耗流量。")
                .setPositiveButton(android.R.string.ok,null)
                .show();
        gifView.getSettings().setAllowFileAccess(true);
        gifView.getSettings().setDatabaseEnabled(true);
        gifView.getSettings().setDisplayZoomControls(false);
        gifView.getSettings().setUseWideViewPort(true);
        gifView.getSettings().setLoadWithOverviewMode(true);
        gifView.getSettings().setJavaScriptEnabled(true);
        gifView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        gifView.getSettings().setDomStorageEnabled(true);
        gifView.getSettings().setAppCacheEnabled(true);
        gifView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//本地有缓存就使用（不管缓存是否过期），没有才请求网络
        gifView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            public void onProgressChanged(WebView view, int progress) {

            }

            /***************** android中使用WebView来打开本机的文件选择器 *************************/
            // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
            // For Android  4.1.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                openFileChooser(uploadMsg);

            }

            // For 3.0 +
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType) {
                openFileChooser(uploadMsg);
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {


            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                return true;
            }
            /************** end ***************/
        });
        //gifView.setWebChromeClient(new WebChromeClient());
        gifView.setWebViewClient(new WebViewClient());
        //gifView.loadDataWithBaseURL("file:///android_asset/surprise.gif", "<center><img src='file:///android_asset/surprise.gif'></center>", "text/html", "utf-8", null);
        gifView.loadUrl("http://h5.u9u9.com/games/Colorvalley/index.html");
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

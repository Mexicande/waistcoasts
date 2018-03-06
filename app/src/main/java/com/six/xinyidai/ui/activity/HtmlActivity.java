package com.six.xinyidai.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.six.xinyidai.R;
import com.six.xinyidai.util.DeviceUtil;
import com.six.xinyidai.util.GetPathFromUri4kitkat;
import com.six.xinyidai.util.PopupWindowUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.shenfan.updateapp.UpdateService;

public class HtmlActivity extends AppCompatActivity {

    @Bind(R.id.bar)
    ProgressBar bar;
    @Bind(R.id.webView)
    WebView webView;

    private ValueCallback mFilePathCallback;
    private final int REQUEST_CODE_TAKE_PICETURE = 11;
    private final int REQUEST_CODE_PICK_PHOTO = 12;
    private final int REQUEST_CODE_CAMERA_PERMISSION = 13;
    private final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 14;
    private File vFile;
    private Uri origUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        StatusBarUtil.setColor(this,0,0);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.Blue));
        ButterKnife.bind(this);
        CheckInternet();
    }
    private void CheckInternet() {
        ConnectivityManager con= (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            //执行相关操作
            getDate();

        }else{
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("网络异常，请检查网络")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .show();


        }
    }

    private void getDate() {
        String html = getIntent().getStringExtra("url");
        if(html!=null){
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAppCachePath(getCacheDir().getPath());
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setSupportZoom(false);

            if (Build.VERSION.SDK_INT >= 21) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            webView.loadUrl(html);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    WebView.HitTestResult hitTestResult = view.getHitTestResult();
                    if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                        view.loadUrl(url);
                        return true;
                    }else if (url.contains("platformapi/startapp")) {
                        try {
                            Intent intent;
                            intent = Intent.parseUri(url,
                                    Intent.URI_INTENT_SCHEME);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setComponent(null);
                            // intent.setSelector(null);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(HtmlActivity.this, "请安装最新版支付宝", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    } else if (url.contains("weixin://wap/pay?")) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(HtmlActivity.this, "请安装最新版微信", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    } else if (url.contains("mqqapi://forward/url?")) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(HtmlActivity.this, "请安装最新版QQ", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    } else if (!(url.contains("http://") || url.contains("https://"))) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                    return false;
                }
            });
            webView.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype,
                                            long contentLength) {
                    if (url.endsWith(".apk")) {//判断是否是.apk结尾的文件路径
                        if (DeviceUtil.isWifiAvailable(HtmlActivity.this)) {
                            UpdateService.Builder.create(url).build(HtmlActivity.this);
                        } else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HtmlActivity.this).create();
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                            Window window = alertDialog.getWindow();
                            window.setContentView(R.layout.integral_exchange_tips1);
                            TextView tv1 = (TextView) window.findViewById(R.id.integral_exchange_tips1_tv);
                            tv1.setText("亲，您现在是非wifi状态下，确定要下载吗？");
                            RelativeLayout rl2 = (RelativeLayout) window.findViewById(R.id.integral_exchange_tips1_rl1);
                            RelativeLayout rl3 = (RelativeLayout) window.findViewById(R.id.integral_exchange_tips1_rl2);
                            rl2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            rl3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    UpdateService.Builder.create(url).build(HtmlActivity.this);
                                    alertDialog.dismiss();
                                }
                            });
                        }

                    }
                }
            });

            webView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == keyEvent.ACTION_DOWN) {

                        if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                            webView.goBack();
                            return true;
                        }
                    }
                    return false;
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    // TODO 自动生成的方法存根

                    if (newProgress == 100) {
                        bar.setVisibility(View.GONE);//加载完网页进度条消失
                    } else {
                        bar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        bar.setProgress(newProgress);//设置进度值
                    }
                }


                /**
                 * h5打开相机或相册
                 */
                // Andorid 4.1+
                public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                    try {
                        showMyDialog();
                        mFilePathCallback = uploadFile;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Andorid 3.0 +
                public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
                    try {
                        showMyDialog();
                        mFilePathCallback = uploadFile;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Android 3.0
                public void openFileChooser(ValueCallback<Uri> uploadFile) {
                    try {
                        showMyDialog();
                        mFilePathCallback = uploadFile;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showMyDialog() {
        View rootView=getLayoutInflater().inflate(R.layout.activity_main,null);
        PopupWindowUtil.showPopupWindow(this,rootView,"相机","文件","取消",
                new PopupWindowUtil.onPupupWindowOnClickListener() {
                    @Override
                    public void onFirstButtonClick() {
                        int flag1 = ActivityCompat.checkSelfPermission(HtmlActivity.this, Manifest.permission.CAMERA);
                        int flag2 = ActivityCompat.checkSelfPermission(HtmlActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (PackageManager.PERMISSION_GRANTED != flag1||PackageManager.PERMISSION_GRANTED != flag2) {
                            ActivityCompat.requestPermissions(HtmlActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA_PERMISSION);
                            cancelFilePathCallback();
                        }else {
                            takeForPicture();
                        }
                    }

                    @Override
                    public void onSecondButtonClick() {
                        int flag2 = ActivityCompat.checkSelfPermission(HtmlActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (PackageManager.PERMISSION_GRANTED != flag2) {
                            ActivityCompat.requestPermissions(HtmlActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_PERMISSION);
                            cancelFilePathCallback();
                        }else {
                            takeForPhoto();
                        }
                    }

                    @Override
                    public void onCancleButtonClick() {
                        cancelFilePathCallback();
                    }
                });
    }

    /**
     * 调用相册
     */
    private void takeForPhoto() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),REQUEST_CODE_PICK_PHOTO);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),REQUEST_CODE_PICK_PHOTO);
        }
    }


    /**
     * 调用相机
     */
    private void takeForPicture() {

        try {
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                vFile = new File(Environment.getExternalStorageDirectory().getPath()
                        + "/xianjindai/");//图片位置
                if (!vFile.exists()) {
                    vFile.mkdirs();
                }
            } else {
                Toast.makeText(HtmlActivity.this, "未挂载sdcard", Toast.LENGTH_LONG).show();
                return;
            }

            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new
                    Date()) + ".jpg";

            Uri uri = Uri.fromFile(new File(vFile, fileName));

            //拍照所存路径
            origUri = uri;

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (Build.VERSION.SDK_INT > 23) {//7.0及以上
//            Uri contentUri = getUriForFile(MainActivity.this, "com.xinhe.crame", picturefile);
//            grantUriPermission(getPackageName(),contentUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
//        } else {//7.0以下
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picturefile));
//        }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, origUri);
            startActivityForResult(intent,REQUEST_CODE_TAKE_PICETURE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cancelFilePathCallback() {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    private void takePhotoResult(int resultCode, Intent data) {
        if (mFilePathCallback != null){
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                String path = GetPathFromUri4kitkat.getPath(this, result);
                Uri uri = Uri.fromFile(new File(path));
                if (Build.VERSION.SDK_INT > 18) {
                    mFilePathCallback.onReceiveValue(new Uri[]{uri});
                } else {
                    mFilePathCallback.onReceiveValue(uri);
                }

            }else {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
        }
    }

    private void takePictureResult(int resultCode) {
        if (mFilePathCallback != null) {
            if (resultCode == RESULT_OK) {

                if (Build.VERSION.SDK_INT > 18) {
                    mFilePathCallback.onReceiveValue(new Uri[]{origUri});
                } else {
                    mFilePathCallback.onReceiveValue(origUri);
                }
            } else {
                //点击了file按钮，必须有一个返回值，否则会卡死
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 105:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 105:
                takePictureResult(resultCode);
                break;

            case 106:
                takePhotoResult(resultCode, data);

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}

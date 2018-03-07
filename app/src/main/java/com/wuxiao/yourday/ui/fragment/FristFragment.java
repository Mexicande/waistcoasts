package com.wuxiao.yourday.ui.fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.util.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FristFragment extends Fragment {


    @Bind(R.id.webView)
    WebView webView;

    private SharedPreferences sp;

    public FristFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frist, container, false);
        ButterKnife.bind(this, view);
        String url = (String) SPUtils.get(getActivity(), "url", "");
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCachePath(getActivity().getCacheDir().getPath());
        TextNet();
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction()==keyEvent.ACTION_DOWN){

                    if (i == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {

                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }
    private void TextNet() {
        ConnectivityManager con=(ConnectivityManager)getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            //执行相关操作
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        }else{
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Toast.makeText(getActivity(),
                    "亲，网络连接失败咯！", Toast.LENGTH_LONG)
                    .show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.removeAllViews();
        webView.destroy();
        ButterKnife.unbind(this);
    }
}


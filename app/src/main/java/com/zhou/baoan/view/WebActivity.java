package com.zhou.baoan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhou.baoan.R;
import com.zhou.baoan.base.BaseActivity;
import com.zhou.baoan.base.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends BaseActivity {

    private static final String TAG = "WebActivity";
    @BindView(R.id.webView) WebView webView;

    @Override
    public int getLayout() {
        return R.layout.activity_web;
    }

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(Constant.NEW_URL, url);
        return intent;
    }

    @OnClick(R.id.iv_back) void back(){
        finish();
    }
    @Override
    public void init() {
        String toUrl = getIntent().getStringExtra(Constant.NEW_URL);
        Log.d(TAG, "------------------------------init: "+toUrl);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);//加载JavaScript
        webView.setWebViewClient(mWebViewClient);//这个一定要设置，要不然不会再本应用中加载
        webView.setWebChromeClient(mWebChromeClient);
        webView.loadUrl(toUrl);
    }

    //ChromeClient   监听网页加载
    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            result.confirm();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    //WebViewClient
    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
            // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
            webView.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }
    };
}


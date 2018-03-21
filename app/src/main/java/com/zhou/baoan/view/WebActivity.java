package com.zhou.baoan.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.webkit.DownloadListener;
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
        String toUrl = getIntent().getStringExtra(Constant.NEW_URL) + "&OpenType=capital_index";
        Log.d(TAG, "------------------------------init: "+toUrl);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);//加载JavaScript
        webView.setWebViewClient(mWebViewClient);//这个一定要设置，要不然不会再本应用中加载
        webView.setWebChromeClient(mWebChromeClient);
        webView.loadUrl(toUrl);
        webView.setDownloadListener(new MyWebViewDownLoadListener());
    }

    /**
     * 如果要实现文件下载的功能，需要设置WebView的DownloadListener，通过实现自己的DownloadListener来实现文件的下载
     */
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.i("tag", "url="+url);
            Log.i("tag", "userAgent="+userAgent);
            Log.i("tag", "contentDisposition="+contentDisposition);
            Log.i("tag", "mimetype="+mimetype);
            Log.i("tag", "contentLength="+contentLength);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
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
            Log.e("用户单击超连接", url);
            //判断用户单击的是那个超连接
            String tag = "tel";
            if (url.contains(tag)) {
                String mobile = url.substring(url.lastIndexOf("/") + 1);
                Log.e("mobile----------->",mobile);
                Intent mIntent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse(mobile);
                mIntent.setData(data);
                //Android6.0以后的动态获取打电话权限
                if (ActivityCompat.checkSelfPermission(WebActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(mIntent);
                    //这个超连接,java已经处理了，webview不要处理
                    return true;
                }else{
                    //申请权限
                    ActivityCompat.requestPermissions(WebActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    return true;
                }
            }else {
                webView.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        }
    };
}


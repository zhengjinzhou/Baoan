package com.zhou.baoan.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhou.baoan.R;
import com.zhou.baoan.base.BaseActivity;
import com.zhou.baoan.base.Constant;
import com.zhou.baoan.util.SpUtil;
import com.zhou.baoan.util.ToastUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.tv_index)
    TextView tv_index;
    @BindView(R.id.tv_map) TextView tv_map;
    @BindView(R.id.tv_sum) TextView tv_sum;
    @BindView(R.id.tv_center) TextView tv_center;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.ll) LinearLayout ll;

    private String websession;
    private String[] brief_url;


    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        //判断
        String isok = SpUtil.getString(MainActivity.this, "isok", "");
        if (isok.equals("ok")){
            tv_map.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
        }
        initWeb();
        initBottom();
        brief_url = getResources().getStringArray(R.array.brief_url);
    }

    /**
     * 跳转
     * @param context
     * @param url
     * @return
     */
    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constant.NEW_URL, url);
        return intent;
    }

    /**
     * webView的设置
     */
    private void initWeb() {
        String new_url = getIntent().getStringExtra(Constant.NEW_URL)+"&OpenType=capital_index";
        Log.d(TAG, "initWeb: "+new_url);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);//加载JavaScript
        webView.setWebViewClient(mWebViewClient);//这个一定要设置，要不然不会再本应用中加载
        webView.setWebChromeClient(mWebChromeClient);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl(new_url);

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

    /**
     * 底部栏
     */
    private void initBottom() {
        tv_index.setTextColor(getResources().getColor(R.color.txt_2));
        Drawable img = tv_index.getResources().getDrawable(R.drawable.index_on);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        tv_index.setCompoundDrawables(null, img, null, null); //设置左图标
    }

    @OnClick({R.id.ivHome,R.id.tv_sum, R.id.tv_index, R.id.tv_map, R.id.tv_center,R.id.iv_login})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivHome:
                for(int i=0;i<30;i++){
                    webView.goBack();
                }
                break;
            case R.id.iv_login:
                newDialog();
                break;
            case R.id.tv_index:
                //startActivity(WebActivity.newIntent(this, "http://121.15.203.82:9210/WAN_MPDA_Pic/PageMain/ProjectList.aspx"));
                for (int i=0;i<20;i++){
                    if (webView.canGoBack()){
                        webView.goBack();
                    }
                }
                break;
            case R.id.tv_map:
                showPopupProject();
                break;

            case R.id.tv_sum:
                showPopupSum();
                break;
            case R.id.tv_center:
                showPopupCenter();
                break;

        }
    }

    /**
     * 退出与切换账号
     */
    private void newDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_new, null);
        dialog.setView(inflate,0,0,0,0);
        dialog.show();
        inflate.findViewById(R.id.tv_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inflate.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.clear();
                startToActivity(LoginActivity.class);
                finish();
            }
        });
    }

    /**
     * 弹出框  项目
     */
    private void showPopupProject() {
        TwoLogin(brief_url[0]);
    }

    /**
     * 弹出框 知识中心
     */
    private void showPopupCenter() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_center, null);
        final PopupWindow pop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.setAnimationStyle(R.anim.mypop_anim);
        //pop.showAsDropDown(tv_center, Gravity.TOP, 0);
        pop.showAtLocation(tv_sum,Gravity.BOTTOM,tv_sum.getWidth()*2,tv_center.getWidth());

        //内部资料
        contentView.findViewById(R.id.tv_reference_private).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+brief_url[1]);
                TwoLogin(brief_url[1]);
                pop.dismiss();
            }
        });
        //图书期刊
        contentView.findViewById(R.id.tv_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+brief_url[2]);
                TwoLogin(brief_url[2]);
                pop.dismiss();
            }
        });
        //常用表格
        contentView.findViewById(R.id.tv_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[3]);
                pop.dismiss();
            }
        });
        //单位规章
        contentView.findViewById(R.id.tv_unit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[4]);
                pop.dismiss();
            }
        });
        //标准规范
        contentView.findViewById(R.id.tv_standard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[5]);
                pop.dismiss();
            }
        });
    }

    /**
     * 弹出框 统计报表
     */
    private void showPopupSum() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_sum, null);
        final PopupWindow pop = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setContentView(contentView);
        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.setAnimationStyle(R.anim.mypop_anim);
        pop.showAtLocation(tv_center,Gravity.BOTTOM,tv_center.getHeight(),tv_center.getWidth());
        //按项目状态
        contentView.findViewById(R.id.tv_pro_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[6]);
                pop.dismiss();
            }
        });
        //项目科室
        contentView.findViewById(R.id.tv_department).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[7]);
                pop.dismiss();
            }
        });
        //项目类别
        contentView.findViewById(R.id.tv_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[8]);
                pop.dismiss();
            }
        });
        //项目投资
        contentView.findViewById(R.id.tv_tze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[9]);
                pop.dismiss();
            }
        });
        //汇总统计
        contentView.findViewById(R.id.tv_reference).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[10]);
                pop.dismiss();
            }
        });
        //品牌公开
        contentView.findViewById(R.id.tv_brand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoLogin(brief_url[11]);
                pop.dismiss();
            }
        });

    }


    /**
     * 第二次验证登录
     * <p>
     * 验证服务器是否变更
     * @param s
     */
    private void TwoLogin(final String s) {
        String username = SpUtil.getString(getApplicationContext(), Constant.UserName, "");
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/xml; charset=utf-8");//根据C#大牛那边写的头文件 以及登录验证方式
        OkHttpClient okHttpClient = new OkHttpClient();
        String psw = URLEncoder.encode(Constant.PSW);
        Log.d(TAG, "加密后的密码: " + psw);
        final String strXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
                "<REQUEST>" +
                "<SP_ID>ToEIM_PIC</SP_ID>" +
                "<PASSWORD>" + psw + "</PASSWORD>" +
                "<USER>"+username+"</USER>" +
                "</REQUEST>";
        Request request = new Request.Builder().url(Constant.ssoUrl)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, strXML)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("", "onFailure: 第二次登录失败 " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.d(TAG, "onResponse: 第二次登录成功" + string);
                try {
                    parseXMLWithPull(string,s);//对xml的解析
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * xml的pull解析
     *
     * @param xmlData
     * @throws Exception
     */
    public void parseXMLWithPull(String xmlData,String s) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));
        int eventType = parser.getEventType();
        String resp_code = "";
        String resp_desc = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            //Log.d(TAG, "parseXMLWithPull: "+nodeName);
            switch (eventType) {
                // 开始解析某个结点
                case XmlPullParser.START_TAG: {
                    if ("WEBSESSION".equals(nodeName)) {
                        websession = parser.nextText();
                    } else if ("RESP_CODE".equals(nodeName)) {
                        resp_code = parser.nextText();
                    } else if ("RESP_DESC".equals(nodeName)) {
                        resp_desc = parser.nextText();
                    }
                    break;
                }
                // 完成解析某个结点
                case XmlPullParser.END_TAG: {
                    if ("RESPONSE".equals(nodeName)) {
                        Log.d("MainActivity", "WEBSESSION is " + websession);
                        Log.d("MainActivity", "RESP_CODE is " + resp_code);
                        Log.d("MainActivity", "RESP_DESC is " + resp_desc);
                        if (resp_code.equals("0000")) {
                            //最后一次验证，即为第三次验证
                            startActivity(WebActivity.newIntent(getApplicationContext(), Constant.iniUrl + websession + "&OpenType=" + s));
                            dialog.dismiss();
                        } else {
                            final String des = resp_desc;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(getApplicationContext(), des);
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            eventType = parser.next();
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
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(mIntent);
                    //这个超连接,java已经处理了，webview不要处理
                    return true;
                }else{
                    //申请权限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
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

    private long firstTime = 0;
    private static Boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
        }else if (keyCode == KeyEvent.KEYCODE_BACK){
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000){
                ToastUtil.show(getApplicationContext(),"再按一次退出程序");
                firstTime = secondTime;
            }else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null){
            webView.destroy();
        }
    }
}


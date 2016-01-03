package com.xiaogang.yixiang.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.xiaogang.yixiang.R;
import com.xiaogang.yixiang.base.BaseActivity;

/**
 * Created by Administrator on 2015/12/29.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private String url;
    private WebView detail_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        url = getIntent().getExtras().getString("url");
        detail_webview = (WebView) this.findViewById(R.id.detail_webview);

        detail_webview.getSettings().setJavaScriptEnabled(true);
        detail_webview.loadUrl(url);
        detail_webview.setWebViewClient(new HelloWebViewClient());
    }

    @Override
    public void onClick(View view) {

    }
    public void back(View view){
        finish();
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause ()
    {
        detail_webview.reload ();
        super.onPause ();
    }

}

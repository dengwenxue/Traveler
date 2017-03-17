package com.mark.traveller.ftwy.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.utils.DataConstants;

/**
 * 旅游新闻详情页面
 * Created by Mark on 2016/11/17 0017.
 */

public class TravellingNewsUI extends Activity {
    private ImageButton mBackIMB;
    private WebView mWebView;
    private String mUrl;
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_travell);
        // 获取数据
        mUrl = getIntent().getStringExtra(DataConstants.TRAVL_DATA);

        initView();
        initWebView();

        mBackIMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 新闻页面
     */
    private void initWebView() {
        // 加载数据url
        mWebView.loadUrl(mUrl);
        // 设置控件
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);// 显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true);// 支持js功能

        // 加载网页
        mWebView.setWebViewClient(new WebViewClient() {

            // 所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接:" + url);
                view.loadUrl(url);// 在跳转链接时强制在当前webview中加载
                return true;
            }

            // 开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mPbLoading.setVisibility(View.VISIBLE);
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPbLoading.setVisibility(View.GONE);
            }
        });

        // mWebView.goBack();//跳到上个页面
        // mWebView.goForward();//跳到下个页面
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // 进度发生变化
                System.out.println("进度:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 网页标题
                System.out.println("网页标题:" + title);
            }
        });

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBackIMB = (ImageButton) findViewById(R.id.ui_travel_back);
        mWebView = (WebView) findViewById(R.id.ui_travel_webview);
        mPbLoading = (ProgressBar) findViewById(R.id.ui_travel_progress);
    }

}

package com.example.a201711116.notice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.a201711116.R;
import com.example.a201711116.fragment.NewsActivity;

import org.jsoup.select.Elements;

public class NoticeNewsActivity extends AppCompatActivity {
    TextView newsTitle;
    WebView newsContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_news);
        newsTitle = findViewById(R.id.news_title_my);
        newsContext = findViewById(R.id.news_context_my);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String url = intent.getStringExtra("URL");
        WebSettings webSettings = newsContext.getSettings();

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        newsTitle.setText(title);

        newsContext.getSettings().setJavaScriptEnabled(true);
        newsContext.setWebViewClient(new WebViewClient());
        newsContext.loadUrl(url);

    }
}
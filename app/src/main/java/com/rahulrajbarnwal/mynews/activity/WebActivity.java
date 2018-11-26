package com.rahulrajbarnwal.mynews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.rahulrajbarnwal.mynews.R;

import static com.rahulrajbarnwal.mynews.utils.EndPoint.KW_STATUS;

public class WebActivity extends AppCompatActivity {

    String webUrl = "";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (getIntent() != null){
            webUrl = getIntent().getStringExtra(KW_STATUS);
            Log.e(KW_STATUS, webUrl);
        }
        if (webUrl.contains("http:")){
            StringBuilder sb = new StringBuilder(webUrl);
            sb.setCharAt(4, 's');

            webUrl = sb.toString();
        }
        webView = findViewById(R.id.web_view);
        Log.e(KW_STATUS, webUrl);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(webUrl);
    }
}

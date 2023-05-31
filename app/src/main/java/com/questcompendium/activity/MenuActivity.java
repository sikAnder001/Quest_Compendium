package com.questcompendium.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.questcompendium.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.ivTitleImage)
    ImageView ivTitleImage;
    @BindView(R.id.ivMenu)
    ImageView moIvMenu;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_about_us);
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }
        moIvMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
        Glide.with(this).
                load(getTitleImg()).
                into(ivTitleImage);

        if (!TextUtils.isEmpty(url))
            showProgress();

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                hideProgress();
                webview.setVisibility(View.VISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        webview.loadUrl(url);
    }

    @OnClick(R.id.ivMenu)
    public void menuActivity() {
        onBackPressed();
    }

}
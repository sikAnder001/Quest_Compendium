package com.questcompendium.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.questcompendium.R;
import com.questcompendium.activity.BaseActivity;
import com.questcompendium.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebviewActivity extends BaseActivity {
    @BindView(R.id.tvBackText)
    TextView moTvBackText;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    private float m_downX;
    String moScreenTitle;
    String moRedirectUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_webview);

        if (getIntent() != null) {
            moRedirectUrl = getIntent().getStringExtra(Constants.WEBVIEW_URL);
            moScreenTitle = getIntent().getStringExtra(Constants.SCREEN_NAME);
        }
        if (TextUtils.isEmpty(moRedirectUrl))
            return;
        // Log.d("TTT", "moRedirectUrl..." + moRedirectUrl);
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);
        //  moTvBackText.setText(getString(R.string.facility));
        initWebView();
    }

    @OnClick(R.id.llBack)
    public void changeActivity() {
        onBackPressed();
    }

    private void initWebView() {
        ButterKnife.bind(this);
        tvToolbarTitle.setText("");
     /*   if (!isEmpty(moScreenTitle))
            tvToolbarTitle.setText(moScreenTitle);*/
        if (!isNetworkConnected())
            return;

        webView.loadUrl(moRedirectUrl);

        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("URL", url);
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgress();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgress();
                //finish();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}


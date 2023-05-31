package com.questcompendium.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.questcompendium.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonMenuFragment extends BaseFragment {
    @BindView(R.id.webview)
    WebView webview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      /*  if (moRoot != null)
            return moRoot;*/

        View moRoot = inflater.inflate(R.layout.activity_about_us, container, false);
        ButterKnife.bind(this, moRoot);

        String url = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            url = bundle.getString("url");
        }

        activity.showProgress();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                activity.hideProgress();
                webview.setVisibility(View.VISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        webview.loadUrl(url);
        return moRoot;
    }

}

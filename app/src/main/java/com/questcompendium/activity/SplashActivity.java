package com.questcompendium.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.questcompendium.R;
import com.questcompendium.utils.CommonUtils;
import com.questcompendium.utils.LogV2;
import com.questcompendium.utils.SharedPreferenceManager;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_TIME = 3 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_splash);
        retriveFirebaseDeeplink();
        initializeContent();
    }

    private void initializeContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity();
            }
        }, SPLASH_TIME);
    }

    private void retriveFirebaseDeeplink() {
        // [START get_deep_link]
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        try {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                Map<String, String> values = null;
                                try {
                                    values = CommonUtils.getUrlValues(deepLink + "");
                                } catch (UnsupportedEncodingException e) {
                                    LogV2.logException(TAG, e);
                                }
                                String token = values.get("token");
                                Log.d("TTT", "token...." + token);
                                new SharedPreferenceManager(SplashActivity.this).setHotelId(token);
                            }
                        } catch (Exception e) {
                            LogV2.logException(TAG, e);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                         LogV2.logException(TAG, e);
                    }
                });
    }

    private void openActivity() {
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        Intent intent;
        /*if (sharedPreferenceManager.isSetTutorial()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, WelcomeActivity.class);
        }*/
        if (!TextUtils.isEmpty(sharedPreferenceManager.getHotelId())) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, QrScannerActivity.class);
        }
        startActivity(intent);
        finish();
    }
}


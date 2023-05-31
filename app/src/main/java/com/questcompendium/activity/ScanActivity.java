package com.questcompendium.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.questcompendium.R;
import com.questcompendium.utils.CommonUtils;
import com.questcompendium.utils.SharedPreferenceManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanActivity extends BaseActivity {

    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);

        ivQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, QrScannerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                try {
                    Map<String, String> values = CommonUtils.getUrlValues(result);
                    String token = values.get("token");
                    Log.d("TTT", "token...." + token);
                    SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
                    sharedPreferenceManager.setHotelId(token);

                    startActivity(new Intent(ScanActivity.this, MainActivity.class));
                    finish();

                } catch (UnsupportedEncodingException e) {
                    Log.e("Error", e.getMessage());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
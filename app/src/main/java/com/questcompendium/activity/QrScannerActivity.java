package com.questcompendium.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.onesignal.OneSignal;
import com.questcompendium.AppGlobal;
import com.questcompendium.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.questcompendium.model.Facility;
import com.questcompendium.utils.CommonUtils;
import com.questcompendium.utils.SharedPreferenceManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QrScannerActivity extends BaseActivity {

    private static final String TAG = "QrScannerActivity";

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Log.d("TTT", "permission grant");
                initializeContent();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                //toastMessage("Give storage permission first");
                Log.v("TTT", "Permission is revoked");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                Log.d("TTT", "permission grant");
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    private void initializeContent() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(QrScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        if (result != null && result.getText() != null) {
                            // resultTextView.setText(scanResult.getContents());
                            Log.d("TTT", "scal result..." + result.getText());

                            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(QrScannerActivity.this);
                            sharedPreferenceManager.setHotelId("");

                            Map<String, String> values = null;
                            try {
                                values = CommonUtils.getUrlValues(result.getText());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String token = values.get("token");
                            Log.d("TTT", "token...." + token);
                            sharedPreferenceManager.setHotelId(token);
                            Log.v("token",token);
                            callCountAPI(token);
                            OneSignal.disablePush(false);
                            Intent intent = new Intent(QrScannerActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            finish();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void callCountAPI(String token) {
        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }

        if (TextUtils.isEmpty(token)) {
            toastMessage("Hotel id is empty");
            return;
        }

        Call<Facility> callCount = AppGlobal.moAppService.postHotelAnalytics(token, "1", "0","0");
        callCount.enqueue(new Callback<Facility>() {
            @Override
            public void onResponse(Call<Facility> call, Response<Facility> response) {

            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {

            }
        });
    }
}


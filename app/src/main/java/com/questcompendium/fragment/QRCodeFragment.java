package com.questcompendium.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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
import com.questcompendium.activity.BaseActivity;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.model.Facility;
import com.questcompendium.sql.DBHelper;
import com.questcompendium.utils.CommonUtils;
import com.questcompendium.utils.SharedPreferenceManager;

import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCodeFragment extends BaseFragment {
    private CodeScanner mCodeScanner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      /*  if (moRoot != null)
            return moRoot;*/

        View moRoot = inflater.inflate(R.layout.fragment_qr_code, container, false);
        ButterKnife.bind(this, moRoot);
        ((MainActivity) getActivity()).setCheckMenu(3);

        Dexter.withContext(activity).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Log.d("TTT", "permission grant");
                initializeContent(moRoot);
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
        return moRoot;
    }

    private void initializeContent(View moRoot) {

        CodeScannerView scannerView = moRoot.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(QrScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        if (result != null && result.getText() != null) {
                            // resultTextView.setText(scanResult.getContents());
                            Log.d("TTT", "scal result..." + result.getText());

                            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity);
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
                            DBHelper loDB = new DBHelper(activity);
                            loDB.deleteAllNotifications();
                            OneSignal.disablePush(false);
                            Log.v("token",token);
                            callCountAPI(token);
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            activity.finish();
                        } else {
                            activity.finish();
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

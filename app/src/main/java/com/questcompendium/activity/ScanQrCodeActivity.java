package com.questcompendium.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScanQrCodeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_scan);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Log.d("TTT", "permission grant");

              /*  new ZxingOrient(ScanQrCodeActivity.this)
                        .setInfo("QR code Scanner with UI customization")
                        .setToolbarColor("#A89970")
                        .setInfoBoxColor("#A89970")
                        .setBeep(false)
                        .initiateScan(Barcode.QR_CODE);*/
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {
            // resultTextView.setText(scanResult.getContents());
            Log.d("TTT", "scal result..." + scanResult.getContents());

            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
            sharedPreferenceManager.setHotelId("");

            Map<String, String> values = null;
            try {
                values = CommonUtils.getUrlValues(scanResult.getContents());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String token = values.get("token");
            Log.d("TTT", "token...." + token);
            sharedPreferenceManager.setHotelId(token);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else {
            finish();
        }*/
    }

    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ZxingOrientResult scanResult = ZxingOrient.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            resultTextView.setText(scanResult.getContents());
        }
    }*/

    private static final int REQUEST_CAMERA = 1000;
    String TAG = "TTT";

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
           /* Snackbar.make(findViewById(R.id.coordinator_layout), R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(ScanQrCodeActivity.this, new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();*/
//            ActivityCompat.requestPermissions(ScanQrCodeActivity.this, new String[]{Manifest.permission.CAMERA},
//                    REQUEST_CAMERA);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }
}

   /* @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.d("TTT", "result..." + rawResult.getText()); // Prints scan results
        Log.d("TTT", "format..." + rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        sharedPreferenceManager.setHotelId("");

        Map<String, String> values = null;
        try {
            values = CommonUtils.getUrlValues(rawResult.getText());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String token = values.get("token");
        Log.d("TTT", "token...." + token);
        sharedPreferenceManager.setHotelId(token);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }*/

   /* @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/

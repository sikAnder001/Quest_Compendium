package com.questcompendium.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.questcompendium.R;
import com.questcompendium.utils.SharedPreferenceManager;

import java.util.Objects;


public class BaseActivity extends AppCompatActivity {

    private Dialog dialog;
    public Location currentLocation;
    // public String fsBackImg, fsTitleImage;
    public String fsHotelId;
    //  public Location currentLocation;
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceManager = new SharedPreferenceManager(this);
        fsHotelId = sharedPreferenceManager.getHotelId();
        Log.d("TTT", "fsHotelId.." + fsHotelId);

    }

    public String getTitleImg() {
        return sharedPreferenceManager.getTitleImg();
    }

    public String getBackImg() {
        return sharedPreferenceManager.getBackImg();
    }

    public void failureError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
        hideProgress();
    }

    public void hideKeyboard() {
        try {
            if (this.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validationError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public void toastMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showDialog(message);
    }

    public void showProgress() {
        if (!isFinishing() && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadAppLanguage();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return true;
        }
        //showDialog(getString(R.string.no_internet));
        //Toast.makeText(this, "connect to internet", Toast.LENGTH_SHORT).show();
        return false;
    }


    public void hideProgress() {
        if (!isFinishing() && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    public void showDialog(String fsMessage) {
        Dialog moDialogBox = new Dialog(this);
        moDialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        moDialogBox.setContentView(R.layout.simple_dialouge);
        moDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        moDialogBox.getWindow().setGravity(Gravity.CENTER);
        moDialogBox.setCancelable(false);

        try {
            if (!isFinishing())
                moDialogBox.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView tvOk = moDialogBox.findViewById(R.id.tvOk);
        TextView tvText = moDialogBox.findViewById(R.id.tvText);
        TextView tvCancel = moDialogBox.findViewById(R.id.tvCancel);
        View view1 = moDialogBox.findViewById(R.id.view1);

        tvOk.setText("Ok");
        tvCancel.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        tvText.setText(fsMessage);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moDialogBox.dismiss();
                finish();
            }
        });
    }
}

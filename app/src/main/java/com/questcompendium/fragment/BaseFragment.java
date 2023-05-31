package com.questcompendium.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.questcompendium.activity.BaseActivity;

public class BaseFragment extends Fragment {

    public BaseActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return true;
        }
        //showDialog(getString(R.string.no_internet));
        //Toast.makeText(this, "connect to internet", Toast.LENGTH_SHORT).show();
        return false;
    }
    public void toastMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        activity.showDialog(message);
    }
}

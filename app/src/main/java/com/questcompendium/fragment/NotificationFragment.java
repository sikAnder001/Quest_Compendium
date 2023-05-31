package com.questcompendium.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.questcompendium.R;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.adapter.NotificationAdapter;
import com.questcompendium.model.Notification;
import com.questcompendium.sql.DBHelper;
import com.questcompendium.utils.SharedPreferenceManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends BaseFragment implements NotificationAdapter.OnViewUpdateListener {

    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;
    @BindView(R.id.llEmpty)
    RelativeLayout llEmpty;

    NotificationAdapter notificationAdapter;
    ArrayList<Notification> moNotificationList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View moRoot = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, moRoot);
        initialize();
        ((MainActivity) getActivity()).setCheckMenu(2);
        return moRoot;
    }

    private void initialize() {
        DBHelper loDB = new DBHelper(activity);
        moNotificationList = loDB.getAllNotificationList();
        notificationAdapter = new NotificationAdapter(activity, moNotificationList);
        rvNotification.setAdapter(notificationAdapter);
        notificationAdapter.setOnViewUpdateListener(this);
        onViewUpdate();
        setHotelLogo();
    }

    private void setHotelLogo() {
        SharedPreferenceManager loSharedPreferenceManager = new SharedPreferenceManager(getActivity());
        String fsUrl = loSharedPreferenceManager.getTitleImg();
        if (fsUrl != null && !fsUrl.isEmpty()) {
            Glide.with(activity).
                    load(fsUrl).
                    into(((MainActivity) getActivity()).getMoIvTitleImage());
        }
    }


    @Override
    public void onViewUpdate() {
        if (moNotificationList != null && moNotificationList.size() > 0) {
            llEmpty.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.VISIBLE);
        }
    }
}

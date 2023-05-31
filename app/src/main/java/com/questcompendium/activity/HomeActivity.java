package com.questcompendium.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.questcompendium.R;
import com.questcompendium.fragment.HomeFragment;
import com.questcompendium.fragment.NotificationFragment;
import com.questcompendium.fragment.QRCodeFragment;
import com.questcompendium.fragment.SearchListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.tvToolbarTitle)
    TextView moTvToolbarTitleN;
    @BindView(R.id.nav_view)
    BottomNavigationView moNavView;
    Fragment moActive = null;
    HomeFragment moHomeFragment;
    NotificationFragment moNotificationFragment;
    SearchListFragment moSearchListFragment;
    QRCodeFragment moQrCodeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponent();
    }

    private void initComponent() {
        ButterKnife.bind(this);
        moNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        moNavView.setSelectedItemId(R.id.navigation_home);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d("TTT", "tread set..");
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_home));
                    if (moHomeFragment == null) {
                        moHomeFragment = new HomeFragment();
                        moActive = moHomeFragment;
                        switchFragment(moHomeFragment, 1, "1");
                    } else {
                        switchFragment(moHomeFragment, 2, "1");
                    }
                    return true;

                case R.id.navigation_search:
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_search));
                    if (moSearchListFragment == null) {
                        moSearchListFragment = new SearchListFragment();
                        moActive = moSearchListFragment;
                        switchFragment(moSearchListFragment, 1, "2");
                    } else {
                        switchFragment(moSearchListFragment, 2, "2");
                    }
                    return true;

                case R.id.navigation_notifiation:
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_notification));
                    if (moNotificationFragment == null) {
                        moNotificationFragment = new NotificationFragment();
                        moActive = moNotificationFragment;
                        switchFragment(moNotificationFragment, 1, "3");
                    } else {
                        switchFragment(moNotificationFragment, 2, "3");
                    }
                    return true;
                case R.id.navigation_qr_code:
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_qr_code));
                    if (moQrCodeFragment == null) {
                        moQrCodeFragment = new QRCodeFragment();
                        moActive = moQrCodeFragment;
                        switchFragment(moQrCodeFragment, 1, "4");
                    } else {
                        switchFragment(moQrCodeFragment, 2, "4");
                    }
                    return true;
            }
            return false;
        }
    };
    FragmentTransaction fragmentTransaction;

    private void switchFragment(Fragment fragment, int type, String tag) {
        // Log.d("TTT", "active..." + moActive.getTag());
       /* if (type == 1) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, fragment, tag);
            fragmentTransaction.commit();
        } else {
            fm.beginTransaction().hide(active).show(fragment).commit();
        }*/
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment, tag);
        fragmentTransaction.commit();
    }
}
package com.questcompendium.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.questcompendium.R;
import com.questcompendium.AppGlobal;
import com.questcompendium.adapter.HotelAdapter;
import com.questcompendium.adapter.HotelSideMenuAdapter;
import com.questcompendium.fragment.CommonMenuFragment;
import com.questcompendium.fragment.HomeFragment;
import com.questcompendium.fragment.NotificationFragment;
import com.questcompendium.fragment.QRCodeFragment;
import com.questcompendium.fragment.SearchListFragment;
import com.questcompendium.model.Hotel;
import com.questcompendium.model.MenuN;
import com.questcompendium.utils.Common;
import com.questcompendium.utils.CommonUtils;
import com.questcompendium.utils.Constants;
import com.questcompendium.utils.SharedPreferenceManager;
import com.rey.material.widget.ImageButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.questcompendium.utils.Constants.IntentKey.NOTIFICATION;

public class MainActivity extends BaseActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener/* implements LocationListener*/ {

            private static final String TAG = "MainActivity";

    @BindView(R.id.rvHotelList)
    RecyclerView moRvHotelList;
    @BindView(R.id.rvMenuList)
    RecyclerView rvMenuList;
    @BindView(R.id.rlMap)
    RelativeLayout rlMap;
    @BindView(R.id.llEmpty)
    LinearLayout moLlEmpty;
    @BindView(R.id.tvMainTitle)
    TextView moTvMainTitle;
    @BindView(R.id.tvEmptyDesc)
    TextView moTvEmptyDesc;
    @BindView(R.id.tvToolbarTitle)
    TextView moTvToolbarTitleN;
    @BindView(R.id.ibNavigation)
    ImageButton ibNavigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvMoreDetail)
    TextView tvMoreDetail;
    @BindView(R.id.tvCancel)
    TextView moTvCancel;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.ivSearchMap)
    ImageView moIvSearchMap;
    @BindView(R.id.ivTitleImage)
    ImageView moIvTitleImage;
    @BindView(R.id.ivHotel)
    ImageView moIvHotel;
    @BindView(R.id.tvHotelName)
    TextView moTvHotelName;
    @BindView(R.id.tvHotelDesc)
    TextView moTvHotelDesc;
    @BindView(R.id.tvDistance)
    TextView moTvDistance;
    @BindView(R.id.rlHotelList)
    RelativeLayout moRlHotelList;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.ivMenu)
    ImageView moIvMenu;
    @BindView(R.id.rlMenu)
    LinearLayout rlMenu;
    @BindView(R.id.rlSearchBar)
    RelativeLayout moRlSearchBar;
    private static final int MY_PERMISSIONS_LOCATION = 103;
    private LocationRequest mLocationRequest = null;
    private Long UPDATE_INTERVAL = Long.valueOf(10 * 1000);  /* 10 secs */
    private Long FASTEST_INTERVAL = Long.valueOf(2000); /* 2 sec */
    public static GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;
    private float moMapZoom = 12;
    private Hotel foHotel;
    private String fsCurrentHotelId = "";
    private String fsCurrentHotelName = "";
    private String fsCurrentHotelImage = "";
    HotelAdapter mHotelAdapter;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    public static List<Marker> markers;
    public static HashMap<String, Integer> markerMap;
    LatLngBounds llb;
    @BindView(R.id.nav_view)
    BottomNavigationView moNavView;
    Fragment moActive = null;
    HomeFragment moHomeFragment;
    NotificationFragment moNotificationFragment;
    SearchListFragment moSearchListFragment;
    QRCodeFragment moQrCodeFragment;
    CommonMenuFragment moAboutUsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);
        initialize();
    }

    public ImageView getMoIvTitleImage() {
        return moIvTitleImage;
    }

    public void setMoIvTitleImage(ImageView moIvTitleImage) {
        this.moIvTitleImage = moIvTitleImage;
    }

    boolean isNotification = false;

    private void initialize() {
        ButterKnife.bind(this);

        TextView searchText = (TextView) searchview.findViewById(R.id.search_src_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/sans_regular.otf");
        searchText.setTypeface(typeface);

        llBack.setVisibility(View.GONE);
        //  moTvToolbarTitle.setText(getString(R.string.app_name));
        tvMoreDetail.setOnClickListener(this);
        moIvSearchMap.setOnClickListener(this);
        moTvCancel.setOnClickListener(this);
        moIvMenu.setOnClickListener(this);
        // moTvAboutUs.setOnClickListener(this);
        ibNavigation.setOnClickListener(this);

        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }

        moNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (getIntent() != null) {
            isNotification = getIntent().getBooleanExtra(NOTIFICATION, false);
        }
        if (isNotification)
            moNavView.setSelectedItemId(R.id.navigation_notifiation);
        else
            moNavView.setSelectedItemId(R.id.navigation_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
        setUpGClient();
        getMenuBarListAPI();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    moIvMenu.setVisibility(View.VISIBLE);
                    Log.d(TAG, "tread set..");
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_home));
                    moHomeFragment = new HomeFragment();
                    moActive = moHomeFragment;
                    switchFragment(moHomeFragment, 1, "1");

                    return true;

                case R.id.navigation_search:
                    moIvMenu.setVisibility(View.INVISIBLE);
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_search));
                    moSearchListFragment = new SearchListFragment();
                    moActive = moSearchListFragment;
                    switchFragment(moSearchListFragment, 1, "2");
                    return true;

                case R.id.navigation_notifiation:
                    moIvMenu.setVisibility(View.INVISIBLE);
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_notification));
                    moNotificationFragment = new NotificationFragment();
                    moActive = moNotificationFragment;
                    switchFragment(moNotificationFragment, 1, "3");
                    return true;
                case R.id.navigation_qr_code:
                    moIvMenu.setVisibility(View.INVISIBLE);
                    moTvToolbarTitleN.setText(getResources().getString(R.string.title_qr_code));
                    moQrCodeFragment = new QRCodeFragment();
                    moActive = moQrCodeFragment;
                    switchFragment(moQrCodeFragment, 1, "4");
                    return true;
            }
            return false;
        }
    };

    FragmentTransaction fragmentTransaction;

    private void switchFragment(Fragment fragment, int type, String tag) {

        //moIvMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
        moIvMenu.setTag("list");
        rlMenu.setVisibility(View.GONE);
        llBack.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE/*BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE*/);
        fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction = getSupportFragmentManager().beginTransaction();
        /*fragmentTransaction.replace(R.id.main_container, fragment, tag);
        fragmentTransaction.commit();*/
        fragmentTransaction//.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(tag)
                .commit();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (!isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
                    builder.setTitle(getResources().getString(R.string.location_permission))
                            .setMessage(getResources().getString(R.string.location_permission_msg))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
                                }
                            }).show();
                }
            } else
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        mMap = googleMap;
        mMap.setOnMarkerClickListener(marker -> {
            try {
                if (markerMap != null &&
                        markerMap.size() > 0 &&
                        marker.getId() != null &&
                        markerMap.containsKey(marker.getId())) {

                    if (marker.getId() != null) {
                        // LogV2.d(TAG, "before ads size..." + marker.getId());
                        //  int id = Integer.parseInt(markerMap.get(marker.getId()).toString().replace("m", ""));
                        //int id = Integer.parseInt(marker.getId().replace("m", ""));
                        //id = id - 1;
                        int id = markerMap.get(marker.getId());
                        Log.d(TAG, "ads size..." + id);
                        if (foHotel != null && foHotel.getFoHotelList() != null && foHotel.getFoHotelList().length > 0) {
                            moRlHotelList.setVisibility(View.VISIBLE);
                            moTvHotelName.setText(foHotel.getFoHotelList()[id].getFsHotelName());
                            moTvHotelDesc.setText(foHotel.getFoHotelList()[id].getFsAddress());
                            moTvDistance.setText(foHotel.getFoHotelList()[id].getFsDistance());

                            Glide.with(MainActivity.this).
                                    load(foHotel.getFoHotelList()[id].getFsImage()).
                                    placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                                    into(moIvHotel);

                            fsCurrentHotelId = foHotel.getFoHotelList()[id].getFiHotelId();
                            fsCurrentHotelName = foHotel.getFoHotelList()[id].getFsHotelName();
                            fsCurrentHotelImage = foHotel.getFoHotelList()[id].getFsImage();
                        } else {
                            toastMessage("Hotel not found");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
        onMapRedyCode();
    }

    private void onMapRedyCode() {
        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the foUserProfile grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                checkLocationPermission();
                return;
            }
            mMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkLocationSettings();
        try {
            if (currentLocation != null)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),
                        currentLocation.getLongitude()), moMapZoom));

        } catch (Exception e) {
            e.printStackTrace();
        }
        //mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "permission was granted");
                        checkLocationSettings();
                    }
                    break;
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_LOCATION);
                    break;
                }
        }
    }

    private void checkLocationSettings() {
        try {
            if (!checkGPSEnabled()) {
                return;
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                getLocation();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                currentLocation = location;
                getHotelList(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), "");
                mFusedLocationClient.removeLocationUpdates(this);
            }
        }
    };

    private void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        mMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        Log.d(TAG, "currentLocation..." + currentLocation);
                        moveToCurrentLocation();
                        getHotelList(String.valueOf(currentLocation.getLatitude()),
                                String.valueOf(currentLocation.getLongitude()), "");

                    } else {
                        startLocationUpdates();
                    }
                });
    }

    private boolean checkGPSEnabled() {
        if (!isLocationEnabled()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                //payal
                //startLocationUpdates();
                mLocationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(UPDATE_INTERVAL)
                        .setFastestInterval(FASTEST_INTERVAL);
                Log.d(TAG, "getMyLocation...");
                getMyLocation();
            }, 400);
        }
        return isLocationEnabled();
    }

    GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 200;

    private synchronized void setUpGClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } else {
            Log.d(TAG, "initialize mGoogleApiClient is not null");
        }
    }

    private void getMyLocation() {
        if (mGoogleApiClient != null) {

            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                    //payal
                    //final Location[] mylocation = {LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)};
                    final Location[] mylocation = new Location[1];
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations, this can be null.
                                    if (location != null) {
                                        mylocation[0] = location;
                                        Log.d(TAG, "mylocation[0]..." + mylocation[0]);
                                        // Logic to handle location object
                                    }
                                }
                            });

                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);


                    //payal
                    /*LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, MapsActivity.this);*/
                    LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    onLocationChanged(locationResult.getLastLocation());
                                }
                            },
                            Looper.myLooper());


                    //payal
                    //PendingResult result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
                    LocationSettingsRequest.Builder builder2 = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                            .checkLocationSettings(builder2.build());
                    //checkLocationSettings(mGoogleApiClient, builder.build());

                    result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                            try {
                                LocationSettingsResponse response = task.getResult(ApiException.class);
                                // All location settings are satisfied. The client can initialize location
                                // requests here.
                                //setupLocationListener();
                                int permissionLocation = ContextCompat
                                        .checkSelfPermission(MainActivity.this,
                                                Manifest.permission.ACCESS_FINE_LOCATION);
                                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
/*                                    mylocation[0] = LocationServices.FusedLocationApi
                                            .getLastLocation(mGoogleApiClient);*/
                                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                                    mFusedLocationClient.getLastLocation()
                                            .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                                                @Override
                                                public void onSuccess(Location location) {
                                                    // Got last known location. In some rare situations, this can be null.
                                                    if (location != null) {
                                                        mylocation[0] = location;
                                                        Log.d(TAG, "mylocation[0]..." + mylocation[0]);
                                                        // Logic to handle location object
                                                    }
                                                }
                                            });
                                }

                            } catch (ApiException exception) {
                                switch (exception.getStatusCode()) {
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                        // Location settings are not satisfied. But could be fixed by showing the
                                        // foUserProfile a dialog.
                                        try {
                                            // Cast to a resolvable exception.
                                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                                            // Show the dialog by calling startResolutionForResult(),
                                            // and check the result in onActivityResult().
                                            /*resolvable.startResolutionForResult(
                                                    MapsActivity.this, IntentConstant.REQ_LOCATION_SETTINGS);*/
                                            resolvable.startResolutionForResult(MainActivity.this,
                                                    REQUEST_CHECK_SETTINGS_GPS);
                                        } catch (IntentSender.SendIntentException | ClassCastException e) {
                                            // Ignore the error.
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        // Location settings are not satisfied. However, we have no way to fix the
                                        // settings so we won't show the dialog.
                                        break;
                                }
                            }
                        }
                    });
                }
            } else {
                // LogV2.d(TAG, "map is not connected...");
            }
        } else {
            // LogV2.d(TAG, "mGoogleApiClient is null..");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivity result..." + requestCode);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(TAG, "ok gps is required....");
                        checkLocationSettings();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d(TAG, "cancel...");
                        checkLocationSettings();
                        break;
                }
                break;

            case 1000:
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra("result");
                    try {
                        Map<String, String> values = CommonUtils.getUrlValues(result);
                        String token = values.get("token");
                        Log.d(TAG, "token...." + token);
                        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
                        sharedPreferenceManager.setHotelId(token);

                        moIvMenu.setVisibility(View.VISIBLE);
                        moTvToolbarTitleN.setText(getResources().getString(R.string.title_home));
                        moHomeFragment = new HomeFragment();
                        moActive = moHomeFragment;
                        switchFragment(moHomeFragment, 1, "1");
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error", e.getMessage());
                    }
                }

                break;
        }
    }

    private void moveToCurrentLocation() {
        if (currentLocation == null) {
            return;
        }
        Log.d("lalo", currentLocation.getLatitude() + " " + currentLocation.getLongitude());
        if (mMap != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude()), moMapZoom));
    }

    private boolean isLocationEnabled() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private void getHotelList(String latitute, String longitute, String search) {
        if (moHomeFragment != null) {
            moHomeFragment.getCurrentTemp(latitute, longitute);
        }
    }

    public void setEmptyScreen() {
        moLlEmpty.setVisibility(View.VISIBLE);
        moRvHotelList.setVisibility(View.GONE);
        moTvMainTitle.setText(getString(R.string.no_hotel));
        moTvEmptyDesc.setText(getString(R.string.no_hotel_desc));
    }

    public void drawMarkers(ArrayList<Hotel.HotelList> foHotelList) {
        Log.d(TAG, "draw marker...");
        markers = new ArrayList<>();
        markerMap = new HashMap<>();
        mMap.clear();
        float color;
        for (int i = 0; i < foHotelList.size(); i++) {
            color = BitmapDescriptorFactory.HUE_RED;
            Marker marker;
            marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(color)).
                    position(new LatLng(Double.parseDouble(foHotelList.get(i).getFdLat()), Double.parseDouble(foHotelList.get(i).getFdLong()))));

            try {
                marker.setTag(foHotelList.get(i).getFiHotelId() + "");
                builder.include(new LatLng(Double.parseDouble(foHotelList.get(i).getFdLat()), Double.parseDouble(foHotelList.get(i).getFdLong())));
                markerMap.put(marker.getId(), i);
                Log.d(TAG, "set marker...");
                markers.add(marker);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (currentLocation != null) {
            builder.include(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            mMap.moveCamera(point);
        }
        try {
            llb = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(llb, 600, 600, 0));
            if (moMapZoom > 0)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(moMapZoom));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvMoreDetail) {
            moRlHotelList.setVisibility(View.GONE);
            if (!fsCurrentHotelId.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, HotelMoreDetail.class);
                intent.putExtra(Constants.HOTEL_ID, fsCurrentHotelId);
                intent.putExtra(Constants.HOTEL_NAME, fsCurrentHotelName);
                intent.putExtra(Constants.HOTEL_IMAGE, fsCurrentHotelImage);
                startActivity(intent);
            } else {
                toastMessage("Hotel id is not found");
            }
        } else if (v.getId() == R.id.tvCancel) {
            moRlHotelList.setVisibility(View.GONE);
        } else if (v.getId() == R.id.ivSearchMap) {
            if (moRvHotelList.getVisibility() == View.VISIBLE) {
                moRvHotelList.setVisibility(View.GONE);
                rlMap.setVisibility(View.VISIBLE);
                moIvSearchMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_white));
                //moIvSearchMap.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                moRvHotelList.setVisibility(View.VISIBLE);
                rlMap.setVisibility(View.GONE);
                moIvSearchMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_map));
                //  moIvSearchMap.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            hideKeyboard();
        } else if (v.getId() == R.id.ivMenu) {
            if (!TextUtils.isEmpty(moIvMenu.getTag().toString().trim())) {
                if (moIvMenu.getTag().toString().equalsIgnoreCase("list")) {
                    moIvMenu.setTag("close");
                    moIvMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
                    rlMenu.setVisibility(View.VISIBLE);
                    //visible
                } else {
                    //moIvMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
                    moIvMenu.setTag("list");
                    rlMenu.setVisibility(View.GONE);
                    //gone
                }
            }
        } else if (v.getId() == R.id.tvAboutUs) {
            moIvMenu.setVisibility(View.INVISIBLE);
            rlMenu.setVisibility(View.GONE);
            llBack.setVisibility(View.VISIBLE);
            moTvToolbarTitleN.setText("About Us");
            if (moAboutUsFragment == null) {
                moAboutUsFragment = new CommonMenuFragment();
                moActive = moAboutUsFragment;
                switchFragment(moAboutUsFragment, 1, "5");
            } else {
                switchFragment(moAboutUsFragment, 2, "5");
            }
            rlMenu.setVisibility(View.GONE);
        } else if (v.getId() == R.id.ibNavigation) {
            moTvToolbarTitleN.setText("Home");
            //moIvMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
            moIvMenu.setTag("list");
            moIvMenu.setVisibility(View.VISIBLE);
            llBack.setVisibility(View.GONE);
            onBackPressed();
        }
    }

    public void aboutUs(String link, String title) {
        Log.d(TAG, "link..." + link);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // moIvMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
                moIvMenu.setTag("list");
                rlMenu.setVisibility(View.GONE);
            }
        }, 700);
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        intent.putExtra("url", link);
        startActivity(intent);

       /* if (moAboutUsFragment == null) {
            moAboutUsFragment = new CommonMenuFragment();
            moActive = moAboutUsFragment;
            switchFragment(moAboutUsFragment, 1, "5");
        } else {
            switchFragment(moAboutUsFragment, 2, "5");
        }*/
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d(TAG, "back click..");
        FragmentManager fragments = getSupportFragmentManager();

        Fragment homeFrag = getSupportFragmentManager().findFragmentById(R.id.main_container);
        //  Fragment homeFrag = fragments.findFragmentByTag(String.valueOf(R.id.main_container));

        if (fragments.getBackStackEntryCount() == 1) {
            Common.backpress("Are you sure you want to exit the app?", this, true);
            //finish();
        } else if (fragments.getBackStackEntryCount() > 1) {
            Log.d(TAG, "popback count...." + fragments.getBackStackEntryCount());
            fragments.popBackStackImmediate();
        } else {
            Log.d(TAG, "back..");
            super.onBackPressed();
        }
    }

    public void setCheckMenu(int position) {

        moNavView.getMenu().getItem(0).setChecked(false);
        moNavView.getMenu().getItem(1).setChecked(false);
        moNavView.getMenu().getItem(2).setChecked(false);
        moNavView.getMenu().getItem(3).setChecked(false);
        moNavView.getMenu().getItem(position).setChecked(true);
    }

    private void getMenuBarListAPI() {


        if (TextUtils.isEmpty(sharedPreferenceManager.getHotelId())) {
            return;
        }
        Call<MenuN> loHotelDetailResponse = AppGlobal.moAppService.getMenuBarList(sharedPreferenceManager.getHotelId());
        loHotelDetailResponse.enqueue(new Callback<MenuN>() {

            @Override
            public void onResponse(Call<MenuN> call, Response<MenuN> foResponse) {
                hideProgress();
                //Common.printReqRes(foResponse.body(), "getMenuBarList", Constants.API_RESPONSE);
                if (foResponse.isSuccessful()) {
                    try {
                        Log.d(TAG, "getMenuBarListAPI response body..." + foResponse.body());
                        MenuN foHotelMenu = foResponse.body();
                        if (foHotelMenu != null && foHotelMenu.getFbIsSuccess().equalsIgnoreCase("true")) {
                            ArrayList<MenuN.FoMenuBarList> menuList = foHotelMenu.getFoMenuBarList();
                            HotelSideMenuAdapter hotelSideMenuAdapter = new HotelSideMenuAdapter(MainActivity.this, menuList);
                            rvMenuList.setAdapter(hotelSideMenuAdapter);
                            //setAdapter(foHotelMenu);
                            //setHotelMenu(foHotelMenu);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // String fsMessage = Common.getErrorMessage(null);
                    Common.showErrorDialog(MainActivity.this, "Something went wrong", false);
                }
            }

            @Override
            public void onFailure(Call<MenuN> call, Throwable t) {
                hideProgress();
                // Common.printReqRes(t, "getMenuBarList", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(MainActivity.this, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(MainActivity.this, t.getMessage(), false);
                }
            }
        });
    }

    public void updateMarker(ArrayList<Hotel.HotelList> filteredList) {
        drawMarkers(filteredList);
    }

    public void changeActivity(int position) {
        Intent intent = new Intent(this, HotelMoreDetail.class);
        intent.putExtra(Constants.HOTEL_ID, foHotel.getFoHotelList()[position].getFiHotelId());
        intent.putExtra(Constants.HOTEL_NAME, foHotel.getFoHotelList()[position].getFsHotelName());
        intent.putExtra(Constants.HOTEL_IMAGE, foHotel.getFoHotelList()[position].getFsImage());
        intent.putExtra(Constants.LAT, String.valueOf(currentLocation.getLatitude()));
        intent.putExtra(Constants.LON, String.valueOf(currentLocation.getLatitude()));
        intent.putExtra(Constants.BACKGROUND_IMAGE, foHotel.getFoHotelList()[position].getFsAppBackgroundImage());
        startActivity(intent);
    }
}

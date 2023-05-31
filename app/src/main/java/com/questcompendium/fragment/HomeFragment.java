package com.questcompendium.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.questcompendium.R;
import com.questcompendium.AppGlobal;
import com.questcompendium.activity.FacilityActivity;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.activity.QrScannerActivity;
import com.questcompendium.adapter.HotelMenuAdapter;
import com.questcompendium.model.Facility;
import com.questcompendium.model.Menu;
import com.questcompendium.model.Temp;
import com.questcompendium.utils.Common;
import com.questcompendium.utils.CommonUtils;
import com.questcompendium.utils.Constants;
import com.questcompendium.utils.SharedPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tvDate)
    TextView moTvDate;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvCurrentTemp)
    TextView moTvCurrentTemp;
    @BindView(R.id.tvDayOfWeek)
    TextView moTvDayOfWeek;
    @BindView(R.id.tvWelcome)
    TextView moTvWelcome;
    @BindView(R.id.tvHotelDesc)
    TextView moTvHotelDesc;
    private String fsHotelName, fsHotelImage;

    @BindView(R.id.rvHotelMenu)
    RecyclerView moRvHotelMenu;
    @BindView(R.id.rlHomeScreen)
    RelativeLayout rlHomeScreen;


    String latitude, longitude;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      /*  if (moRoot != null)
            return moRoot;*/

        View moRoot = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, moRoot);

        /*if (activity.getIntent() != null) {
            fsHotelId = activity.getIntent().getStringExtra(Constants.HOTEL_ID);
            fsHotelName = activity.getIntent().getStringExtra(Constants.HOTEL_NAME);
            fsHotelImage = activity.getIntent().getStringExtra(Constants.HOTEL_IMAGE);
            fsLat = activity.getIntent().getStringExtra(Constants.LAT);
            fsLon = activity.getIntent().getStringExtra(Constants.LON);
            fsBackImg = activity.getIntent().getStringExtra(Constants.BACKGROUND_IMAGE);
        }*/
        /*if (TextUtils.isEmpty(fsHotelId))
            return;*/
        Log.d("TTT", "Home..");
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity);
        activity.fsHotelId = sharedPreferenceManager.getHotelId();

        initialize();
        ((MainActivity) getActivity()).setCheckMenu(0);
        return moRoot;
    }

    private void initialize() {
        // ButterKnife.bind(activity);

      /*  if (!TextUtils.isEmpty(activity.fsBackImg))
            Glide.with(this).
                    load(activity.fsBackImg).
                    *//*   placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).*//*
                            into(ivBack);*/

        GridLayoutManager manager = new GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false);
        moRvHotelMenu.setLayoutManager(manager);

       /* moIvHotel.setOnClickListener(this);
        moIvLocal.setOnClickListener(this);
        moIvEmergency.setOnClickListener(this);
        moIvDining.setOnClickListener(this);*/

        getToken();
        setCurrentDate();

        // moIvMenuHotel.setImageResource(0);
       /* Glide.with(activity).
                load(fsHotelImage).
                placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                into(moIvMenuHotel);*/

        if (activity.currentLocation != null)
            getCurrentTemp(activity.currentLocation.getLatitude() + "", activity.currentLocation.getLongitude() + "");
        // getHotelMoreDetail();
    }

    private void getToken() {

        OSDeviceState device = OneSignal.getDeviceState();
        String pushToken = device.getPushToken();
        String userId = device.getUserId();
        sendRegistrationToServer(userId);
        Log.d("TTT", "token..." + userId);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w("TTT", "getInstanceId failed", task.getException());
//                        return;
//                    }
//                    // Get new Instance ID token
//                    String token = task.getResult().getToken();
//                    Log.d("TTT", "token..." + token);
//                    sendRegistrationToServer(token);
//                });
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        SharedPreferenceManager moSharedPreferenceManager = new SharedPreferenceManager(activity);
        getHotelMoreDetail(token);

        if (moSharedPreferenceManager.getFCMToken().equalsIgnoreCase(token)) {
            // Log.d("TTT", "return...");
            return;
        }
        // Log.d("TTT", "send in progress...");
        moSharedPreferenceManager.setFCMToken(token);

        // server call
    }

    private void setCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        String formattedDate = df.format(c);
        moTvDate.setText(formattedDate);
        String dayLongName = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        moTvDayOfWeek.setText(dayLongName);

        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        //String time = Common.getTimeFromDate(currentDateandTime);
        if (time.contains("am")) {
            time = time.replace("am", "AM");
            //  moTvTime.setText(time);
        } else {
            time = time.replace("pm", "PM");
            //moTvTime.setText(time);
        }
    }

    public void getHotelMoreDetail(String token) {
        rlHomeScreen.setVisibility(View.GONE);

        if (!activity.isNetworkConnected()) {
            activity.toastMessage("No Internet Connection");
            return;
        }
        if (TextUtils.isEmpty(activity.fsHotelId)) {
            activity.toastMessage("Hotel id is empty");
            OneSignal.disablePush(true);
            OneSignal.clearOneSignalNotifications();
            return;
        }
        // SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity);
        activity.showProgress();
        JSONArray jsonArrayFinal = new JSONArray();
        JsonObject gsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject = CommonUtils.getDeviceDetails(activity);
        try {
            jsonObject1.put("fiHotelsId", activity.fsHotelId);
            jsonObject1.put("fsToken", token);
            jsonArrayFinal.put(jsonObject);
            jsonObject1.put("Device-Details", jsonObject);
            gsonObject = (JsonObject) jsonParser.parse(jsonObject1.toString());
            // jsonArrayFinal.put(jsonObject1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        activity.showProgress();

        Call<Menu> loHotelDetailResponse = AppGlobal.moAppService.getHotelMoreDetail(gsonObject);
        loHotelDetailResponse.enqueue(new Callback<Menu>() {

            @Override
            public void onResponse(Call<Menu> call, Response<Menu> foResponse) {
                activity.hideProgress();
                Common.printReqRes(foResponse.body(), "GetHotelMoreList", Constants.API_RESPONSE);
                if (foResponse.isSuccessful()) {
                    try {
                        Log.d("TTT", "response body..." + foResponse.body());
                        Menu foHotelMenu = foResponse.body();
                        if (foHotelMenu != null && foHotelMenu.getFbIsSuccess().equalsIgnoreCase("true")) {

                            if (foHotelMenu.getFoHotelList() != null && foHotelMenu.getFoHotelList().size() > 0) {
                                SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(activity);
                                sharedPreferenceManager.setBackImg(foHotelMenu.getFoHotelList().get(0).getFsAppBackgroundImage());
                                sharedPreferenceManager.setTitleImg(foHotelMenu.getFoHotelList().get(0).getFsImage());
                                setAdapter(foHotelMenu);
                                moTvWelcome.setText(foHotelMenu.getFoHotelList().get(0).getFsHotelName());
                                moTvHotelDesc.setText(foHotelMenu.getFoHotelList().get(0).getFsShortDesc());
                                Glide.with(activity).
                                        load(foHotelMenu.getFoHotelList().get(0).getFsAppBackgroundImage()).
                                        into(ivBack);

                                latitude = foHotelMenu.getFoHotelList().get(0).getFiLatitude();
                                longitude = foHotelMenu.getFoHotelList().get(0).getFiLongitude();

                                Glide.with(activity).
                                        load(foHotelMenu.getFoHotelList().get(0).getFsImage()).
                                        into(((MainActivity) getActivity()).getMoIvTitleImage());
                                rlHomeScreen.setVisibility(View.VISIBLE);

                            } else {
                                SharedPreferenceManager loSharedPreferenceManager = new SharedPreferenceManager(getActivity());
                                loSharedPreferenceManager.setHotelId("");
                                dismissQR("This property is not validate, please contact to support team.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String fsMessage = Common.getErrorMessage(null);
                    Common.showErrorDialog(activity, fsMessage, false);
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                activity.hideProgress();
                Common.printReqRes(t, "GetHotelMoreList", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(activity, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(activity, t.getMessage(), false);
                }
            }

        });
    }

    private void dismissQR(String fsMessage) {
        Dialog moDialogBox = new Dialog(getActivity());
        moDialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        moDialogBox.setContentView(R.layout.simple_dialouge);
        moDialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        moDialogBox.getWindow().setGravity(Gravity.CENTER);
        moDialogBox.setCancelable(false);

        try {
            if (!getActivity().isFinishing())
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
                moDialogBox.hide();
                Intent intent = new Intent(getActivity(), QrScannerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    public void getCurrentTemp(String latitute1, String longitute1) {
        String url = "weather?lat=" + latitude + "&lon=" + longitude + "&appid=bdadd086fe6101fbf6105f399715edf2&units=Metric";
        Call<Temp> loHotelDetailResponse = AppGlobal.moAppServiceDirect.getCurrTemp(url);
        loHotelDetailResponse.enqueue(new Callback<Temp>() {

            @Override
            public void onResponse(Call<Temp> call, Response<Temp> foResponse) {
                activity.hideProgress();
                Common.printReqRes(foResponse.body(), "getCurrentTemp", Constants.API_RESPONSE);


                if (foResponse.code() == 429) {
                    // : {"cod":429, "message": "Your account is temporary blocked due to exceeding of requests limitation of your subscription type. Please choose the proper subscription http://openweathermap.org/price"}
                    return;
                }
                if (foResponse.isSuccessful()) {
                    try {
                        Temp temp = foResponse.body();
                        if (temp != null)
                            moTvCurrentTemp.setText(temp.getMain().getTemp() + "\u2103");
                        Log.d("TTT", "response body..." + foResponse.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Temp> call, Throwable t) {
                activity.hideProgress();
                Common.printReqRes(t, "getCurrentTemp", Constants.API_ERROR);
            }
        });
    }

    private void setAdapter(Menu foHotelMenu) {
        //Menu.FoMenuList[]
        HotelMenuAdapter adapter = new HotelMenuAdapter(activity, foHotelMenu.getFoMenuList(), activity.fsHotelId, new HotelMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String fiMenuId, String fsMenuName) {
                callCountAPI(fiMenuId,fsMenuName);
            }
        });
        moRvHotelMenu.setAdapter(adapter);
        moRvHotelMenu.setFocusable(false);
    }

    private void callCountAPI(String fiMenuId, String fsMenuName) {
        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }

        SharedPreferenceManager sP=new SharedPreferenceManager(activity);

        Call<Facility> callCount = AppGlobal.moAppService.postHotelAnalytics(sP.getHotelId(), "0", fsMenuName,fiMenuId);
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

package com.questcompendium.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.questcompendium.R;
import com.questcompendium.AppGlobal;
import com.questcompendium.adapter.HotelMenuAdapter;
import com.questcompendium.model.Menu;
import com.questcompendium.model.Search;
import com.questcompendium.model.SearchList;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelMoreDetail extends BaseActivity /*implements View.OnClickListener*/ {
    @BindView(R.id.tvToolbarTitle)
    TextView moTvToolbarTitle;
    @BindView(R.id.tvBackText)
    TextView moTvBackText;
    @BindView(R.id.tvDate)
    TextView moTvDate;
    @BindView(R.id.tvCurrentTemp)
    TextView moTvCurrentTemp;
    @BindView(R.id.tvDayOfWeek)
    TextView moTvDayOfWeek;
    @BindView(R.id.tvTime)
    TextView moTvTime;
    @BindView(R.id.tvWelcome)
    TextView moTvWelcome;
    @BindView(R.id.ibNavigation)
    ImageView moIbNavigation;
    @BindView(R.id.ivMenuHotel)
    ImageView moIvMenuHotel;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    /*    @BindView(R.id.ivHotel)
      ImageView moIvHotel;
       @BindView(R.id.ivLocal)
       ImageView moIvLocal;
       @BindView(R.id.ivEmergency)
       ImageView moIvEmergency;
       @BindView(R.id.ivDining)
       ImageView moIvDining;*/
    private String fsHotelId, fsHotelName, fsHotelImage, fsLat, fsLon, fsBackImg;

    @BindView(R.id.rvHotelMenu)
    RecyclerView moRvHotelMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_more_detail);

        if (getIntent() != null) {
            fsHotelId = getIntent().getStringExtra(Constants.HOTEL_ID);
            fsHotelName = getIntent().getStringExtra(Constants.HOTEL_NAME);
            fsHotelImage = getIntent().getStringExtra(Constants.HOTEL_IMAGE);
            fsLat = getIntent().getStringExtra(Constants.LAT);
            fsLon = getIntent().getStringExtra(Constants.LON);
            fsBackImg = getIntent().getStringExtra(Constants.BACKGROUND_IMAGE);
        }
        if (TextUtils.isEmpty(fsHotelId)) return;

        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);

        Glide.with(this).
                load(fsBackImg).
                /*   placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).*/
                        into(ivBack);

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        moRvHotelMenu.setLayoutManager(manager);

       /* moIvHotel.setOnClickListener(this);
        moIvLocal.setOnClickListener(this);
        moIvEmergency.setOnClickListener(this);
        moIvDining.setOnClickListener(this);*/

        moTvBackText.setText("Hotel");
        moTvToolbarTitle.setText(fsHotelName);
        moTvWelcome.setText("Welcome to " + fsHotelName);
        setCurrentDate();
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                   /* Intent intent = new Intent(HotelMoreDetail.this, ListScreenActivity.class);
                    intent.putExtra("search", etSearch.getText().toString().trim());
                    startActivity(intent);*/
                    searchListAPI();
                }
            }
        });
        // moIvMenuHotel.setImageResource(0);
        Glide.with(HotelMoreDetail.this).
                load(fsHotelImage).
                placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                into(moIvMenuHotel);

        try {
            Log.d("TTT", "fsHotelImage..." + fsLat + " " + fsLon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCurrentTemp();
        getHotelMoreDetail();
    }

    private void searchListAPI() {
        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }
        showProgress();

        Call<SearchList> loFacilityResponse = AppGlobal.moAppService.getSearchValue(fsHotelId, etSearch.getText().toString().trim());
        loFacilityResponse.enqueue(new Callback<SearchList>() {

            @Override
            public void onResponse(Call<SearchList> call, Response<SearchList> foResponse) {
                hideProgress();
                Common.printReqRes(foResponse.body(), "searchListAPI", Constants.API_RESPONSE);
                if (foResponse.isSuccessful()) {
                    try {
                        Log.d("TTT", "search response body..." + foResponse.body());
                        SearchList foCategory = foResponse.body();
                        if (foCategory != null && foCategory.getFbIsSuccess().equalsIgnoreCase("true")) {

                            ArrayList<Search> searchList = new ArrayList<>();
                            for (int i = 0; i < foCategory.getFoCatList().size(); i++) {
                                Search search = new Search(foCategory.getFoCatList().get(i).getFiCatId(),
                                        foCategory.getFoCatList().get(i).getFsTitle(), "fiCatId",
                                        foCategory.getFoCatList().get(i).getFiMenuId());
                                searchList.add(search);
                            }
                            for (int i = 0; i < foCategory.getFoFacilityList().size(); i++) {
                                Search search = new Search(foCategory.getFoFacilityList().get(i).getFiFacilityId(),
                                        foCategory.getFoFacilityList().get(i).getFsFacilityName(), "fiFacilityId",
                                        foCategory.getFoFacilityList().get(i).getFsFacilityMenuId());
                                searchList.add(search);
                            }

                            if (searchList != null && searchList.size() > 0) {
                                Intent intent = new Intent(HotelMoreDetail.this, ListScreenActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("search", searchList);
                                intent.putExtras(bundle);
                                intent.putExtra("backgimg", fsBackImg);
                                intent.putExtra("fsHotelId", fsHotelId);
                                startActivity(intent);
                            } else {
                                toastMessage("No Result Found.");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String fsMessage = Common.getErrorMessage(null);
                    Common.showErrorDialog(HotelMoreDetail.this, fsMessage, false);
                }
            }

            @Override
            public void onFailure(Call<SearchList> call, Throwable t) {
                hideProgress();
                Common.printReqRes(t, "searchListAPI", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(HotelMoreDetail.this, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(HotelMoreDetail.this, t.getMessage(), false);
                }
            }
        });
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
            moTvTime.setText(time);
        } else {
            time = time.replace("pm", "PM");
            moTvTime.setText(time);
        }
    }

    @OnClick(R.id.llBack)
    public void changeActivity() {
        onBackPressed();
    }

    private void getHotelMoreDetail() {
        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }
        if (TextUtils.isEmpty(fsHotelId)) {
            toastMessage("Hotel id is empty");
            return;
        }
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
        showProgress();
        JSONArray jsonArrayFinal = new JSONArray();
        JsonObject gsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject = CommonUtils.getDeviceDetails(this);
        try {
            jsonObject1.put("fiHotelsId", fsHotelId);
            jsonObject1.put("fsToken", sharedPreferenceManager.getFCMToken());
            jsonArrayFinal.put(jsonObject);
            jsonObject1.put("Device-Details", jsonObject);
            gsonObject = (JsonObject) jsonParser.parse(jsonObject1.toString());
           // jsonArrayFinal.put(jsonObject1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showProgress();

        Call<Menu> loHotelDetailResponse = AppGlobal.moAppService.getHotelMoreDetail(gsonObject);
        loHotelDetailResponse.enqueue(new Callback<Menu>() {

            @Override
            public void onResponse(Call<Menu> call, Response<Menu> foResponse) {
                hideProgress();
                Common.printReqRes(foResponse.body(), "GetHotelMoreList", Constants.API_RESPONSE);
                if (foResponse.isSuccessful()) {
                    try {
                        Log.d("TTT", "response body..." + foResponse.body());
                        Menu foHotelMenu = foResponse.body();
                        if (foHotelMenu != null && foHotelMenu.getFbIsSuccess().equalsIgnoreCase("true")) {
                            setAdapter(foHotelMenu);
                            //setHotelMenu(foHotelMenu);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String fsMessage = Common.getErrorMessage(null);
                    Common.showErrorDialog(HotelMoreDetail.this, fsMessage, false);
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                hideProgress();
                Common.printReqRes(t, "GetHotelMoreList", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(HotelMoreDetail.this, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(HotelMoreDetail.this, t.getMessage(), false);
                }
            }
        });
    }

    private void getCurrentTemp() {
        String url = "weather?lat=" + fsLat + "&lon=" + fsLon + "&appid=e7b2054dc37b1f464d912c00dd309595&units=Metric";
        Call<Temp> loHotelDetailResponse = AppGlobal.moAppServiceDirect.getCurrTemp(url);
        loHotelDetailResponse.enqueue(new Callback<Temp>() {

            @Override
            public void onResponse(Call<Temp> call, Response<Temp> foResponse) {
                hideProgress();
                Common.printReqRes(foResponse.body(), "getCurrentTemp", Constants.API_RESPONSE);
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
                hideProgress();
                Common.printReqRes(t, "getCurrentTemp", Constants.API_ERROR);
            }
        });
    }

    private void setAdapter(Menu foHotelMenu) {
        //Menu.FoMenuList[]
        HotelMenuAdapter adapter = new HotelMenuAdapter(HotelMoreDetail.this, foHotelMenu.getFoMenuList(), fsHotelId, new HotelMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String fiMenuId, String fsMenuName) {

            }
        });
        moRvHotelMenu.setAdapter(adapter);
    }

  /*  private void setHotelMenu(Menu menu) {
        Glide.with(HotelMoreDetail.this).
                load(menu.getFoMenuList()[0].getFsImage()).
                placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                into(moIvHotel);
        moIvHotel.setTag(String.valueOf(menu.getFoMenuList()[0].getFiMenuId()));

        Glide.with(HotelMoreDetail.this).
                load(menu.getFoMenuList()[1].getFsImage()).
                placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                into(moIvLocal);
        moIvLocal.setTag(String.valueOf(menu.getFoMenuList()[1].getFiMenuId()));

        Glide.with(HotelMoreDetail.this).
                load(menu.getFoMenuList()[2].getFsImage()).
                placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                into(moIvEmergency);
        moIvEmergency.setTag(String.valueOf(menu.getFoMenuList()[2].getFiMenuId()));

        Glide.with(HotelMoreDetail.this).
                load(menu.getFoMenuList()[3].getFsImage()).
                placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                into(moIvDining);
        moIvDining.setTag(String.valueOf(menu.getFoMenuList()[3].getFiMenuId()));

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HotelMoreDetail.this, FacilityActivity.class);
        intent.putExtra(Constants.HOTEL_ID, fsHotelId);
        if (v.getId() == R.id.ivHotel) {
            intent.putExtra(Constants.HOTEL_MENU_ID, moIvHotel.getTag().toString());
        } else if (v.getId() == R.id.ivLocal) {
            intent.putExtra(Constants.HOTEL_MENU_ID, moIvLocal.getTag().toString());
        } else if (v.getId() == R.id.ivEmergency) {
            intent.putExtra(Constants.HOTEL_MENU_ID, moIvEmergency.getTag().toString());
        } else if (v.getId() == R.id.ivDining) {
            intent.putExtra(Constants.HOTEL_MENU_ID, moIvDining.getTag().toString());
        }
        startActivity(intent);
    }*/
}

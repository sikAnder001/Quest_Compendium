package com.questcompendium.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.questcompendium.AppGlobal;
import com.questcompendium.R;
import com.questcompendium.adapter.CategoryAdapter;
import com.questcompendium.model.Category;
import com.questcompendium.utils.Common;
import com.questcompendium.utils.Constants;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.tvBackText)
    TextView moTvBackText;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.rvFacility)
    RecyclerView moRvFacility;
    @BindView(R.id.llEmpty)
    LinearLayout moLlEmpty;
    @BindView(R.id.tvMainTitle)
    TextView moTvMainTitle;
    @BindView(R.id.tvEmptyDesc)
    TextView moTvEmptyDesc;
    @BindView(R.id.tvToolbarTitle)
    TextView moTvToolbarTitle;
    @BindView(R.id.ibNavigation)
    ImageView moIbNavigation;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivTitleImage)
    ImageView ivTitleImage;
    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    String fsHotelId, fsHotelMenuId, fsHotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_category_screen);

        if (getIntent() != null) {
            fsHotelMenuId = getIntent().getStringExtra(Constants.HOTEL_MENU_ID);
            fsHotelId = getIntent().getStringExtra(Constants.HOTEL_ID);
            fsHotelName = getIntent().getStringExtra(Constants.HOTEL_NAME);
            //  fsBackImg = getIntent().getStringExtra(Constants.BACKGROUND_IMAGE);
            // fsTitleImg = getIntent().getStringExtra(Constants.BACKGROUND_TITLE_IMAGE);
        }
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);
        ivTitleImage.setVisibility(View.GONE);
        ivMenu.setVisibility(View.VISIBLE);
        moTvToolbarTitle.setVisibility(View.VISIBLE);
        ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        ivMenu.setPadding(10,10,10,10);
        moTvToolbarTitle.setText(fsHotelName);
        llBack.setVisibility(View.VISIBLE);

        Glide.with(this).
                load(getBackImg()).
                into(ivBack);

        Glide.with(this).
                load(getTitleImg()).
                into(ivTitleImage);

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        moRvFacility.setLayoutManager(manager);
        getHotelFacility();
    }

    private void getHotelFacility() {
        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }

        if (TextUtils.isEmpty(fsHotelId) || TextUtils.isEmpty(fsHotelMenuId)) {
            toastMessage("Hotel id is empty");
            return;
        }

        showProgress();

        Call<Category> loFacilityResponse = AppGlobal.moAppService.getHotelCategory(fsHotelId, fsHotelMenuId);
        loFacilityResponse.enqueue(new Callback<Category>() {

            @Override
            public void onResponse(Call<Category> call, Response<Category> foResponse) {
                hideProgress();
                Common.printReqRes(foResponse.body(), "GetFacility", Constants.API_RESPONSE);
                if (foResponse.isSuccessful()) {
                    try {
                        //  Log.d("TTT", "response body..." + foResponse.body());
                        Category foCategory = foResponse.body();
                        if (foCategory != null && foCategory.getFbIsSuccess().equalsIgnoreCase("true")) {
                            if (foCategory.getFoCatList() != null && foCategory.getFoCatList().length > 0) {
                                CategoryAdapter mHotelAdapter = new CategoryAdapter(CategoryActivity.this,
                                        foCategory.getFoCatList(), fsHotelId);
                                moRvFacility.setAdapter(mHotelAdapter);
                            } else {
                                //setEmptyScreen();
                                toastMessage("No Result Found");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String fsMessage = Common.getErrorMessage(null);
                    Common.showErrorDialog(CategoryActivity.this, fsMessage, false);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                hideProgress();
                Common.printReqRes(t, "GetFacility", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(CategoryActivity.this, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(CategoryActivity.this, t.getMessage(), false);
                }
            }
        });
    }

    public void setEmptyScreen() {
        moLlEmpty.setVisibility(View.VISIBLE);
        moRvFacility.setVisibility(View.GONE);
        moTvMainTitle.setText(getString(R.string.no_facility));
        moTvEmptyDesc.setText(getString(R.string.no_facility_desc));
    }


   /* private Facility.FoFacilityList[] initializeItem() {
        Facility.FoFacilityList fontList = new Facility.FoFacilityList("Spa", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList1 = new Facility.FoFacilityList("Colling", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList2 = new Facility.FoFacilityList("Outdoor restaurant", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList3 = new Facility.FoFacilityList("Poolside bar", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList4 = new Facility.FoFacilityList("Swimming pool/ Jacuzzi", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList5 = new Facility.FoFacilityList("Disable rooms & Interconnecting rooms", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList6 = new Facility.FoFacilityList("24 Hour security", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList7 = new Facility.FoFacilityList("Outside catering service", "1", "www.wrpsolution.com");
        Facility.FoFacilityList[] foHotelList = new Facility.FoFacilityList[]{fontList, fontList1, fontList2, fontList3, fontList4, fontList5};
        return foHotelList;
    }*/

    @OnClick(R.id.ibNavigation)
    public void changeActivity() {
        onBackPressed();
    }

    @OnClick(R.id.ivMenu)
    public void menuActivity() {
        //onBackPressed();
        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
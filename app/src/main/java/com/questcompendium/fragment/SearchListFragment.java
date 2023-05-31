package com.questcompendium.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.questcompendium.R;
import com.questcompendium.AppGlobal;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.adapter.SearchListAdapter;
import com.questcompendium.model.Search;
import com.questcompendium.model.SearchList;
import com.questcompendium.utils.Common;
import com.questcompendium.utils.Constants;
import com.rey.material.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchListFragment extends BaseFragment {

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.ivSearch)
    ImageView ivSearch;
    @BindView(R.id.rlSearchList)
    RecyclerView rlSearchList;
    String backgroundImg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View moRoot = inflater.inflate(R.layout.activity_list_screen, container, false);
        ButterKnife.bind(this, moRoot);
        initialize();
        Log.d("TTT", "search..");

        ((MainActivity) getActivity()).setCheckMenu(1);
        return moRoot;
    }

    private void initialize() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                   /* Intent intent = new Intent(HotelMoreDetail.this, ListScreenActivity.class);
                    intent.putExtra("search", etSearch.getText().toString().trim());
                    startActivity(intent);*/
                    searchListAPI();
                } else {
                    Common.clickSingoutDialog("Search box should be not empty", activity, false);
                }
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etSearch.getText().toString()))
                        searchListAPI();
                    else
                        Common.clickSingoutDialog("Search box should be not empty", activity, false);
                    return true;
                }
                return false;
            }
        });
    }

    private void searchListAPI() {
        if (!activity.isNetworkConnected()) {
            activity.toastMessage("No Internet Connection");
            return;
        }
        activity.showProgress();

        Call<SearchList> loFacilityResponse = AppGlobal.moAppService.getSearchValue(activity.fsHotelId, etSearch.getText().toString().trim());
        loFacilityResponse.enqueue(new Callback<SearchList>() {

            @Override
            public void onResponse(Call<SearchList> call, Response<SearchList> foResponse) {
                activity.hideProgress();
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
                                Search search = new Search(foCategory.getFoFacilityList().get(i).getFsDescriptionLink(),
                                        foCategory.getFoFacilityList().get(i).getFsFacilityName(), "fiFacilityId",
                                        foCategory.getFoFacilityList().get(i).getFsFacilityBanner());
                                searchList.add(search);
                            }

                            for (int i = 0; i < foCategory.getFoMenuList().size(); i++) {
                                Search search = new Search(foCategory.getFoMenuList().get(i).getFiMenuId(),
                                        foCategory.getFoMenuList().get(i).getFsMenuName(), "fiMenuId",
                                        foCategory.getFoMenuList().get(i).getFiMenuId());
                                Log.d("TTT", "menu list..." + foCategory.getFoMenuList().get(i).getFiMenuId());
                                searchList.add(search);
                            }

                            if (searchList != null && searchList.size() > 0) {
                                rlSearchList.setVisibility(View.VISIBLE);
                                SearchListAdapter searchListAdapter = new SearchListAdapter(activity, searchList, backgroundImg, activity.fsHotelId);
                                rlSearchList.setAdapter(searchListAdapter);
                            } else {
                                rlSearchList.setVisibility(View.GONE);
                                Common.clickSingoutDialog("No result found", activity, false);
                            }
                        } else {
                            rlSearchList.setVisibility(View.GONE);
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
            public void onFailure(Call<SearchList> call, Throwable t) {
                activity.hideProgress();
                Common.printReqRes(t, "searchListAPI", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(activity, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(activity, t.getMessage(), false);
                }
            }
        });
    }


}

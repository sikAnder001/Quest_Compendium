package com.questcompendium.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.questcompendium.R;
import com.questcompendium.activity.CategoryActivity;
import com.questcompendium.activity.FacilityActivity;
import com.questcompendium.model.Search;
import com.questcompendium.utils.Constants;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SimpleViewHolder> {
    private Activity moActivity;
    private ArrayList<Search> hotelListFiltered;
    String backImage, fsHotelId;

    public SearchListAdapter(Activity context, ArrayList<Search> moHotelList, String backImage, String fsHotelId) {
        this.moActivity = context;
        this.backImage = backImage;
        this.hotelListFiltered = moHotelList;
        this.fsHotelId = fsHotelId;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Search hotelItem = hotelListFiltered.get(position);

        if (hotelItem.getType().equals("fiCatId")) {
            holder.tvMenu.setText("Facility");
        } else if (hotelItem.getType().equals("fiFacilityId")) {
            holder.tvMenu.setText("Information");
        } else {
            holder.tvMenu.setText("Menu");
        }
        holder.tvTitle.setText(hotelItem.getTitle());

        holder.rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) moActivity).changeActivity(position);
                if (hotelItem.getType().equals("fiCatId")) {
                    Intent intent = new Intent(moActivity, CategoryActivity.class);
                    intent.putExtra(Constants.HOTEL_MENU_ID, hotelItem.getFiMenuId());
                    intent.putExtra(Constants.HOTEL_ID, hotelItem.getId());
                    intent.putExtra(Constants.HOTEL_NAME, hotelItem.getTitle());
                    //intent.putExtra(Constants.BACKGROUND_IMAGE, backImage);
                    moActivity.startActivity(intent);
                } else if (hotelItem.getType().equals("fiFacilityId")) {
                    Intent intent = new Intent(moActivity, FacilityActivity.class);
                    //   intent.putExtra(Constants.IMAGE_URL, catListItem.getFsImage());
                    //intent.putExtra(Constants.HOTEL_MENU_ID, catListItem.getFiMenuId());
                    // Log.d("TTT", "fsHotelId..." + fsHotelId);
                    // L/og.d("TTT", "cat id..." + hotelItem.getId());
                    intent.putExtra(Constants.BANNER, hotelItem.getFiMenuId());
                    intent.putExtra(Constants.LINK, hotelItem.getId());
                    intent.putExtra(Constants.TITLE, hotelItem.getTitle());
                    moActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(moActivity, CategoryActivity.class);
                    intent.putExtra(Constants.HOTEL_MENU_ID, hotelItem.getFiMenuId());
                    intent.putExtra(Constants.HOTEL_ID, fsHotelId);
                    intent.putExtra(Constants.HOTEL_NAME, hotelItem.getTitle());
                    //intent.putExtra(Constants.BACKGROUND_IMAGE, backImage);
                    moActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelListFiltered != null ? hotelListFiltered.size() : 0;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvMenu;
        public RelativeLayout rlSearch;

        public SimpleViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            rlSearch = view.findViewById(R.id.rlSearch);
            tvMenu = view.findViewById(R.id.tvMenu);
        }
    }
}
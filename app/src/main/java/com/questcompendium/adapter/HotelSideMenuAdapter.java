package com.questcompendium.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.questcompendium.R;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.model.MenuN;

import java.util.ArrayList;

public class HotelSideMenuAdapter extends RecyclerView.Adapter<HotelSideMenuAdapter.SimpleViewHolder> {
    private MainActivity moActivity;
    private ArrayList<MenuN.FoMenuBarList> moHotelList;
    private String fsHotelId, fsBackImg;

    public HotelSideMenuAdapter(MainActivity context, ArrayList<MenuN.FoMenuBarList> moHotelList) {
        this.moActivity = context;
        this.moHotelList = moHotelList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_side_menu, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        MenuN.FoMenuBarList menuItem = moHotelList.get(position);

        holder.tvAboutUs.setText(menuItem.getFiMenuTitle());
        holder.rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moActivity.aboutUs(menuItem.getFiDecLink(), menuItem.getFiMenuTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return moHotelList != null ? moHotelList.size() : 0;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAboutUs;
        public RelativeLayout rlMenu;

        public SimpleViewHolder(View view) {
            super(view);
            tvAboutUs = view.findViewById(R.id.tvAboutUs);
            rlMenu = view.findViewById(R.id.rlMenu);

        }
    }
}
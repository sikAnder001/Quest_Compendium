package com.questcompendium.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.questcompendium.R;
import com.questcompendium.activity.CategoryActivity;
import com.questcompendium.model.Menu;
import com.questcompendium.utils.Constants;
import com.rey.material.widget.TextView;

public class HotelMenuAdapter extends RecyclerView.Adapter<HotelMenuAdapter.SimpleViewHolder> {
    private Activity moActivity;
    private Menu.FoMenuList[] moHotelList;
    private String fsHotelId;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String fiMenuId, String fsMenuName);
    }

    public HotelMenuAdapter(Activity context, Menu.FoMenuList[] moHotelList, String fsHotelId,OnItemClickListener mListener) {
        this.moActivity = context;
        moActivity = context;
        this.moHotelList = moHotelList;
        this.fsHotelId = fsHotelId;
        this.mListener = mListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_menu, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Menu.FoMenuList facilityItem = moHotelList[position];

        Glide.with(moActivity).
                load(facilityItem.getFsImage()).
                placeholder(moActivity.getResources().getDrawable(R.drawable.iv_placeholder)).
                into(holder.moIvHotelMenu);

        holder.moTvMenuTitle.setText(facilityItem.getFsMenuName().trim());
        holder.rlItemFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(facilityItem.getFiMenuId(),facilityItem.getFsMenuName());

                Intent intent = new Intent(moActivity, CategoryActivity.class);
                intent.putExtra(Constants.HOTEL_MENU_ID, facilityItem.getFiMenuId());
                intent.putExtra(Constants.HOTEL_ID, fsHotelId);
                intent.putExtra(Constants.HOTEL_NAME, facilityItem.getFsMenuName());
               /* intent.putExtra(Constants.BACKGROUND_IMAGE, fsBackImg);
                intent.putExtra(Constants.BACKGROUND_TITLE_IMAGE, fsTitleImage);*/
                moActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moHotelList != null ? moHotelList.length : 0;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView moIvHotelMenu;
        public TextView moTvMenuTitle;
        public RelativeLayout rlItemFacility;

        public SimpleViewHolder(View view) {
            super(view);
            moIvHotelMenu = view.findViewById(R.id.ivHotelMenu);
            moTvMenuTitle = view.findViewById(R.id.tvMenuTitle);
            rlItemFacility = view.findViewById(R.id.rlItemFacility);

        }
    }
}
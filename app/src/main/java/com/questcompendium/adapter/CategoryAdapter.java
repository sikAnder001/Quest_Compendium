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
import com.questcompendium.activity.FacilityActivity;
import com.questcompendium.model.Category;
import com.questcompendium.utils.Common;
import com.questcompendium.utils.Constants;
import com.rey.material.widget.TextView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.SimpleViewHolder> {
    private Activity moActivity;
    private Category.FoCatList[] moFacilityList;
    private String fsHotelId;

    public CategoryAdapter(Activity context, Category.FoCatList[] moHotelList, String fsHotelId) {
        this.moActivity = context;
        moActivity = context;
        this.moFacilityList = moHotelList;
        this.fsHotelId = fsHotelId;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Category.FoCatList catListItem = moFacilityList[position];
        Glide.with(moActivity).
                load(catListItem.getFsImage()).
                placeholder(moActivity.getResources().getDrawable(R.drawable.iv_placeholder)).
                into(holder.moIvHotelMenu);

        holder.moTvMenuTitle.setText(catListItem.getFsTitle().trim());

        holder.rlItemFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (catListItem.getFiCount().equalsIgnoreCase("0")) {
                    //show dialog
                    Common.clickSingoutDialog("No details available for the facility " + catListItem.getFsTitle(), moActivity, false);
                } else {
                    Intent intent = new Intent(moActivity, FacilityActivity.class);
                    //   intent.putExtra(Constants.IMAGE_URL, catListItem.getFsImage());
                    intent.putExtra(Constants.HOTEL_MENU_ID, catListItem.getFiMenuId());
                    intent.putExtra(Constants.HOTEL_ID, fsHotelId);
                    intent.putExtra(Constants.CAT_ID, catListItem.getFiCatId());
                    intent.putExtra(Constants.COUNT, catListItem.getFiCount());
                    intent.putExtra(Constants.TITLE, catListItem.getFsTitle());
                    /* intent.putExtra(Constants.BACKGROUND_TITLE_IMAGE, fsTitleImg);*/
                    moActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moFacilityList != null ? moFacilityList.length : 0;
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
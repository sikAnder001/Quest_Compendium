package com.questcompendium.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.questcompendium.R;
import com.questcompendium.model.Facility;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.TextView;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.SimpleViewHolder> {
    private Activity moActivity;
    private Facility.FoFacilityList[] moFacilityList;
    private RecyclerView parentRecycler;

    public FacilityAdapter(Activity context, Facility.FoFacilityList[] moHotelList) {
        this.moActivity = context;
        this.moFacilityList = moHotelList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facilty, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Facility.FoFacilityList facilityItem = moFacilityList[position];
        Glide.with(moActivity).
                load(facilityItem.getFacilityicon()).
                placeholder(moActivity.getResources().getDrawable(R.drawable.iv_placeholder)).
                into(holder.moIvHotelMenu);

        holder.moTvHotelTitle.setText(facilityItem.getFsFacilityName().trim());

        holder.moRlItemFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return moFacilityList != null ? moFacilityList.length : 0;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircularImageView moIvHotelMenu;
        ImageView moIvCircle;
        public TextView moTvHotelTitle;
        public RelativeLayout moRlItemFacility;

        public SimpleViewHolder(View view) {
            super(view);
            moIvHotelMenu = view.findViewById(R.id.ivHotelMenu);
            moTvHotelTitle = view.findViewById(R.id.tvMenuTitle);
            moRlItemFacility = view.findViewById(R.id.rlItemFacility);
            moIvCircle = view.findViewById(R.id.ivCircle);
        }

        public void showText() {
            int parentHeight = ((View) moIvHotelMenu.getParent()).getHeight();
            float scale = (parentHeight - moTvHotelTitle.getHeight()) / (float) moIvHotelMenu.getHeight();
            // int height = (int) pxFromDp(70, context);
            //  Log.d("TTT", "height..." + height);
            // Log.d("TTT", "before scale..." + scale);
            scale = (float) (scale / 1.3);
            // Log.d("TTT", "after scale..." + scale);
            moIvHotelMenu.setPivotX(moIvHotelMenu.getWidth() * 0.5f);
            moIvHotelMenu.setPivotY(0);
            moIvHotelMenu.animate().scaleX(scale)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            //  moTvHotelTitle.setVisibility(View.VISIBLE);
                            moIvHotelMenu.setAlpha(0.9f);
                            moTvHotelTitle.setTextColor(ContextCompat.getColor(moIvHotelMenu.getContext(), R.color.white));
                            //  moIvHotelMenu.setColorFilter(ContextCompat.getColor(moIvHotelMenu.getContext(), R.color.white));
                            //moIvCircle.setImageDrawable(ContextCompat.getDrawable(moActivity, R.drawable.fill_circle));
                            moIvCircle.setAlpha(0.5f);
                        }
                    })
                    .scaleY(scale).setDuration(200)
                    .start();
        }

        public void hideText() {
            moIvHotelMenu.setAlpha(0.4f);
            // moIvHotelMenu.setColorFilter(ContextCompat.getColor(moIvHotelMenu.getContext(), R.color.colorDivider));
            moTvHotelTitle.setTextColor(ContextCompat.getColor(moIvHotelMenu.getContext(), R.color.colorDivider));
            moIvCircle.setAlpha(0.4f);
            // moIvCircle.setImageDrawable(ContextCompat.getDrawable(moActivity, R.drawable.grey_circle));
            //  moTvHotelTitle.setVisibility(View.VISIBLE);
            moIvHotelMenu.animate().scaleX(1f).scaleY(1f)
                    .setDuration(200)
                    .start();
        }

        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }
    }
}
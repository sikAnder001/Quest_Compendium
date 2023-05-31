package com.questcompendium.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.questcompendium.R;
import com.questcompendium.activity.MainActivity;
import com.questcompendium.model.Hotel;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.SimpleViewHolder> implements Filterable {
    private Activity moActivity;
    private ArrayList<Hotel.HotelList> moHotelList;
    private ArrayList<Hotel.HotelList> hotelListFiltered;

    public HotelAdapter(Activity context, ArrayList<Hotel.HotelList> moHotelList) {
        this.moActivity = context;
        moActivity = context;
        this.hotelListFiltered = moHotelList;
        this.moHotelList = moHotelList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel_list, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Hotel.HotelList hotelItem = hotelListFiltered.get(position);
        holder.moTvHotelName.setText(hotelItem.getFsHotelName());
        holder.moTvHotelDesc.setText(hotelItem.getFsAddress());
        holder.moTvDistance.setText(hotelItem.getFsDistance() + "KM");

        Glide.with(moActivity).
                load(hotelItem.getFsImage()).
                placeholder(moActivity.getResources().getDrawable(R.drawable.iv_placeholder)).
                into(holder.moIvHotel);

        holder.moCvHotelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) moActivity).changeActivity(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelListFiltered != null ? hotelListFiltered.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    hotelListFiltered = moHotelList;
                } else {
                    ArrayList<Hotel.HotelList> filteredList = new ArrayList<>();
                    for (int i = 0; i < moHotelList.size(); i++) {
                        if (moHotelList.get(i).getFsHotelName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(moHotelList.get(i));
                        }
                    }
                    hotelListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = hotelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hotelListFiltered = (ArrayList<Hotel.HotelList>) filterResults.values;
                notifyDataSetChanged();
                ((MainActivity) moActivity).updateMarker(hotelListFiltered);
            }
        };
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView moTvHotelName, moTvHotelDesc, moTvDistance;
        public ImageView moIvHotel;
        public CardView moCvHotelItem;

        public SimpleViewHolder(View view) {
            super(view);
            moTvHotelName = view.findViewById(R.id.tvHotelName);
            moTvHotelDesc = view.findViewById(R.id.tvHotelDesc);
            moIvHotel = view.findViewById(R.id.ivHotel);
            moTvDistance = view.findViewById(R.id.tvDistance);
            moCvHotelItem = view.findViewById(R.id.cvHotelItem);
        }
    }
}
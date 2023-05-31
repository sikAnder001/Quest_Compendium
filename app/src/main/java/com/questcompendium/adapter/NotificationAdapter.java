package com.questcompendium.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.questcompendium.R;
import com.questcompendium.model.Notification;
import com.questcompendium.sql.DBHelper;
import com.questcompendium.utils.Common;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.SimpleViewHolder> {

    private Context moActivity;
    private ArrayList<Notification> notificationList;

    public NotificationAdapter(Activity context, ArrayList<Notification> moHotelList) {
        this.moActivity = context;
        this.notificationList = moHotelList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        Notification notification = notificationList.get(position);
        holder.tvTitle.setText(notification.getTitle());
        //holder.tvDesc.setText(notification.getDesc());
        holder.tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvDesc.setText(Html.fromHtml(getLinkyfiedText(notification.getDesc())));
        holder.tvDateTime.setText(notification.getDateTime());
        String lsImageUrl = notification.getImageUrl();
        String lsBtnLnk = notification.getBtnLink();
        String lsBtnTxt = notification.getBtnTxt();

        if (lsImageUrl != null && !lsImageUrl.isEmpty()) {
            holder.ivImage.setVisibility(View.VISIBLE);
            Glide.with(moActivity).
                    load(lsImageUrl).
                    placeholder(moActivity.getResources().getDrawable(R.drawable.iv_placeholder)).
                    into(holder.ivImage);
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }

        if (lsBtnLnk != null && !lsBtnLnk.isEmpty()) {
            holder.btnLink.setVisibility(View.VISIBLE);
            holder.btnLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.openBrowser(moActivity, lsBtnLnk);
                }
            });

            if (lsBtnTxt != null && !lsBtnTxt.isEmpty())
                holder.btnLink.setText(lsBtnTxt);
        } else {
            holder.btnLink.setVisibility(View.GONE);
        }

        holder.ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper loDB = new DBHelper(moActivity);
                loDB.deleteNotification(notification.getDateTime(), notification.getTitle());
                notificationList.remove(notification);
                notifyDataSetChanged();
                mListener.onViewUpdate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }


    class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDesc, tvTitle, tvDateTime, btnLink;
        public CardView cvNotification;
        public ImageButton ibClose;
        public ImageView ivImage;

        public SimpleViewHolder(View view) {
            super(view);
            tvDesc = view.findViewById(R.id.tvDesc);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            cvNotification = view.findViewById(R.id.cvNotification);
            ibClose = view.findViewById(R.id.ibClose);
            ivImage = view.findViewById(R.id.ivImage);
            btnLink = view.findViewById(R.id.btnLink);
        }
    }

    public String getLinkyfiedText(String fsText) {
        StringBuilder loStringBuilder = new StringBuilder();
        fsText = fsText.replace(" ", "\n").replace(",", "\n");
        String[] laSplitted = fsText.split("\n");

        for (int liCount = 0; liCount < laSplitted.length; liCount++) {
            String link = laSplitted[liCount];
            if ((laSplitted[liCount]).contains("www.") || (laSplitted[liCount]).contains("https") || (laSplitted[liCount]).contains("http")) { // use more statements for
                link = "<a href=\"" + laSplitted[liCount] + "\">" + laSplitted[liCount] + "</a>";
            }
            loStringBuilder.append(link);
            loStringBuilder.append(" ");
        }
        return loStringBuilder.toString();
    }

    public interface OnViewUpdateListener {
        void onViewUpdate();
    }

    public void setOnViewUpdateListener(OnViewUpdateListener foListener) {
        mListener = foListener;
    }

    private OnViewUpdateListener mListener;
}
package com.questcompendium.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Search implements Parcelable {
    String id;
    String title;
    String type;
    String fiMenuId;

    public Search(String id, String title, String type, String fiMenuId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.fiMenuId = fiMenuId;
    }

    protected Search(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
        fiMenuId = in.readString();
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getFiMenuId() {
        return fiMenuId;
    }

    public void setFiMenuId(String fiMenuId) {
        this.fiMenuId = fiMenuId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(fiMenuId);
    }
}

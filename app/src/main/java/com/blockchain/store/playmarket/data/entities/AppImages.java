package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

class AppImages implements Parcelable {
    public String banner;
    public ArrayList<String> gallery = new ArrayList<>();
    public String logo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.banner);
        try {
            dest.writeStringList(this.gallery);
        } catch (Exception e) {

        }

        dest.writeString(this.logo);
    }

    public AppImages() {
    }

    protected AppImages(Parcel in) {
        this.banner = in.readString();
        this.gallery = in.createStringArrayList();
        this.logo = in.readString();
    }

    public static final Parcelable.Creator<AppImages> CREATOR = new Parcelable.Creator<AppImages>() {
        @Override
        public AppImages createFromParcel(Parcel source) {
            return new AppImages(source);
        }

        @Override
        public AppImages[] newArray(int size) {
            return new AppImages[size];
        }
    };
}

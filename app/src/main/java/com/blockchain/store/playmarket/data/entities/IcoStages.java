package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

class IcoStages implements Parcelable {
    public String startDate;
    @SerializedName("endDate")
    public String time;
    public String price;

    public IcoStages() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.time);
        dest.writeString(this.price);
    }

    protected IcoStages(Parcel in) {
        this.startDate = in.readString();
        this.time = in.readString();
        this.price = in.readString();
    }

    public static final Creator<IcoStages> CREATOR = new Creator<IcoStages>() {
        @Override
        public IcoStages createFromParcel(Parcel source) {
            return new IcoStages(source);
        }

        @Override
        public IcoStages[] newArray(int size) {
            return new IcoStages[size];
        }
    };
}

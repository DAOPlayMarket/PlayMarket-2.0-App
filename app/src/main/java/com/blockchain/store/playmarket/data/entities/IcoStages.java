package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IcoStages implements Parcelable {
    public String startsAt;
    @SerializedName("endDate")
    public String endsAt;
    public String price;

    public IcoStages() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startsAt);
        dest.writeString(this.endsAt);
        dest.writeString(this.price);
    }

    protected IcoStages(Parcel in) {
        this.startsAt = in.readString();
        this.endsAt = in.readString();
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

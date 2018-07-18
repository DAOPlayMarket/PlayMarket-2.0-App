package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Rating implements Parcelable {
    @SerializedName("sum")
    public int ratingSum;
    @SerializedName("count")
    public int ratingCount;

    public Rating(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ratingSum);
        dest.writeInt(this.ratingCount);
    }

    private Rating(Parcel in) {
        this.ratingSum = in.readInt();
        this.ratingCount = in.readInt();
    }


    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel source) {
            return new Rating(source);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };
}

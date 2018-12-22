package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class IcoLocalData implements Parcelable {
    public ArrayList<String> price = new ArrayList<>();
    public String numberOfPeriods;
    public String durationOfPeriod;
    public String tokensInPeriod;
    public String startsAt;

    public int getCurrentPeriod() {
        return (int) Math.abs((System.currentTimeMillis() - Long.valueOf(startsAt) * 1000) / Integer.valueOf(durationOfPeriod));
    }

    public String getCompanyValue() {
        return String.valueOf(Double.valueOf(price.get(0)) * 100000);
    }






    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.price);
        dest.writeString(this.numberOfPeriods);
        dest.writeString(this.durationOfPeriod);
        dest.writeString(this.tokensInPeriod);
        dest.writeString(this.startsAt);
    }

    public IcoLocalData() {
    }

    protected IcoLocalData(Parcel in) {
        this.price = in.createStringArrayList();
        this.numberOfPeriods = in.readString();
        this.durationOfPeriod = in.readString();
        this.tokensInPeriod = in.readString();
        this.startsAt = in.readString();
    }

    public static final Parcelable.Creator<IcoLocalData> CREATOR = new Parcelable.Creator<IcoLocalData>() {
        @Override
        public IcoLocalData createFromParcel(Parcel source) {
            return new IcoLocalData(source);
        }

        @Override
        public IcoLocalData[] newArray(int size) {
            return new IcoLocalData[size];
        }
    };
}


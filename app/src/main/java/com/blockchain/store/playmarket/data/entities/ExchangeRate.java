package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ExchangeRate implements Parcelable {
    public String currency;
    public String rate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.currency);
        dest.writeString(this.rate);
    }

    public ExchangeRate() {
    }

    protected ExchangeRate(Parcel in) {
        this.currency = in.readString();
        this.rate = in.readString();
    }

    public static final Parcelable.Creator<ExchangeRate> CREATOR = new Parcelable.Creator<ExchangeRate>() {
        @Override
        public ExchangeRate createFromParcel(Parcel source) {
            return new ExchangeRate(source);
        }

        @Override
        public ExchangeRate[] newArray(int size) {
            return new ExchangeRate[size];
        }
    };
}

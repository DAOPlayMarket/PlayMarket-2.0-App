package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrencyRate implements Parcelable {
    public String name = "PMC";
    public String number3;
    public String decimals= "2";


    public int getDecimals(){
        return Integer.parseInt(decimals);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.number3);
        dest.writeString(this.decimals);
    }

    public CurrencyRate() {
    }

    protected CurrencyRate(Parcel in) {
        this.name = in.readString();
        this.number3 = in.readString();
        this.decimals = in.readString();
    }

    public static final Parcelable.Creator<CurrencyRate> CREATOR = new Parcelable.Creator<CurrencyRate>() {
        @Override
        public CurrencyRate createFromParcel(Parcel source) {
            return new CurrencyRate(source);
        }

        @Override
        public CurrencyRate[] newArray(int size) {
            return new CurrencyRate[size];
        }
    };
}

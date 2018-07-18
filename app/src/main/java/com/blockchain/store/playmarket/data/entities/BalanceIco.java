package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;


public class BalanceIco implements Parcelable {

    public String address;
    public String balanceOf;
    public String decimals;

    protected BalanceIco(Parcel in) {
        this.address = in.readString();
        this.balanceOf = in.readString();
        this.decimals = in.readString();
    }

    public BalanceIco() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.balanceOf);
        dest.writeString(this.decimals);
    }

    public static final Creator<BalanceIco> CREATOR = new Creator<BalanceIco>() {
        @Override
        public BalanceIco createFromParcel(Parcel in) {
            return new BalanceIco(in);
        }

        @Override
        public BalanceIco[] newArray(int size) {
            return new BalanceIco[size];
        }
    };
}

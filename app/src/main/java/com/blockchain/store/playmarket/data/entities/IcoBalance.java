package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;


public class IcoBalance implements Parcelable {

    public String address;
    public String balanceOf;
    public String decimals;

    protected IcoBalance(Parcel in) {
        this.address = in.readString();
        this.balanceOf = in.readString();
        this.decimals = in.readString();
    }

    public String getTokenCount() {
        long tokensNum = Long.valueOf(balanceOf);
        short decimalsNum = Short.valueOf(decimals);
        double transformedTokensNum = tokensNum * Math.pow(10, -decimalsNum);
        transformedTokensNum = Math.round(transformedTokensNum * 10000.0) / 10000.0;
        return String.valueOf(transformedTokensNum);
    }

    public IcoBalance() {
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

    public static final Creator<IcoBalance> CREATOR = new Creator<IcoBalance>() {
        @Override
        public IcoBalance createFromParcel(Parcel in) {
            return new IcoBalance(in);
        }

        @Override
        public IcoBalance[] newArray(int size) {
            return new IcoBalance[size];
        }
    };
}

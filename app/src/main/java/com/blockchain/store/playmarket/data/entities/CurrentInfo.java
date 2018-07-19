package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentInfo implements Parcelable {

    public String weiRaised;
    public String tokensSold;
    public String usdRaised;
    public String currentPeriod;
    public String decimals;

    protected CurrentInfo(Parcel in) {
        this.weiRaised = in.readString();
        this.tokensSold = in.readString();
        this.usdRaised = in.readString();
        this.currentPeriod = in.readString();
        this.decimals = in.readString();
    }

    public static final Creator<CurrentInfo> CREATOR = new Creator<CurrentInfo>() {
        @Override
        public CurrentInfo createFromParcel(Parcel in) {
            return new CurrentInfo(in);
        }

        @Override
        public CurrentInfo[] newArray(int size) {
            return new CurrentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weiRaised);
        dest.writeString(this.tokensSold);
        dest.writeString(this.usdRaised);
        dest.writeString(this.currentPeriod);
        dest.writeString(this.decimals);
    }
}

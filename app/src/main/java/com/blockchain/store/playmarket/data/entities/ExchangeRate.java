package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class ExchangeRate implements Parcelable {
    public CurrencyRate currency = new CurrencyRate();
    public String rate = "100";

    public double getRate(){
        try{
            return Double.parseDouble(rate);
        }catch (Exception e){
            return 1d;
        }
    }

    public ExchangeRate() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.currency, flags);
        dest.writeString(this.rate);
    }

    protected ExchangeRate(Parcel in) {
        this.currency = in.readParcelable(CurrencyRate.class.getClassLoader());
        this.rate = in.readString();
    }

    public static final Creator<ExchangeRate> CREATOR = new Creator<ExchangeRate>() {
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

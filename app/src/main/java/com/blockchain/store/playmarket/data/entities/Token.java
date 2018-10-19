package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Token implements Parcelable {
    public String symbol;
    public String name;
    public String decimals;
    public String contract;
    public String site;
    public String price;
    public String balanceOf = "0";
    public String address;

    public String getTokenCount() {
        long tokensNum = Long.valueOf(balanceOf);
        short decimalsNum = Short.valueOf(decimals);
        double transformedTokensNum = tokensNum * Math.pow(10, -decimalsNum);
        transformedTokensNum = Math.round(transformedTokensNum * 10000.0) / 10000.0;
        return String.valueOf(transformedTokensNum);
    }

    public Token() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.symbol);
        dest.writeString(this.name);
        dest.writeString(this.decimals);
        dest.writeString(this.contract);
        dest.writeString(this.site);
        dest.writeString(this.price);
        dest.writeString(this.balanceOf);
        dest.writeString(this.address);
    }

    protected Token(Parcel in) {
        this.symbol = in.readString();
        this.name = in.readString();
        this.decimals = in.readString();
        this.contract = in.readString();
        this.site = in.readString();
        this.price = in.readString();
        this.balanceOf = in.readString();
        this.address = in.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}

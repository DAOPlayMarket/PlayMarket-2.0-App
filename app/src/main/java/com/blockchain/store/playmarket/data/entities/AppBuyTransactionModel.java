package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class AppBuyTransactionModel extends TransactionModel implements Parcelable {
    public int TransactionType = Constants.TransactionTypes.BUY_APP.ordinal();
    public String priceInWei;
    public App boughtApp;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.BUY_APP;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public String getTransactionFormattedResult() {
        return "- " + new EthereumPrice(priceInWei).getDisplayPrice(false);
    }

    @Override
    public String getFormattedTitle() {
        return "'" + boughtApp.getTitleName() + "' Buy";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.TransactionType);
        dest.writeString(this.priceInWei);
        dest.writeParcelable(this.boughtApp, flags);
    }

    public AppBuyTransactionModel() {
    }

    protected AppBuyTransactionModel(Parcel in) {
        this.TransactionType = in.readInt();
        this.priceInWei = in.readString();
        this.boughtApp = in.readParcelable(App.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppBuyTransactionModel> CREATOR = new Parcelable.Creator<AppBuyTransactionModel>() {
        @Override
        public AppBuyTransactionModel createFromParcel(Parcel source) {
            return new AppBuyTransactionModel(source);
        }

        @Override
        public AppBuyTransactionModel[] newArray(int size) {
            return new AppBuyTransactionModel[size];
        }
    };
}

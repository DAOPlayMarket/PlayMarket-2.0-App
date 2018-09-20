package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class InvestTransactionModel extends TransactionModel implements Parcelable {
    public int TransactionType = Constants.TransactionTypes.INVEST.ordinal();
    public AppInfo appInfo;
    public String price;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.INVEST;
    }


    @Override
    public String getTransactionFormattedResult() {
        return "- " + new EthereumPrice(price).getDisplayPrice(false);
    }

    @Override
    public String getFormattedTitle() {
        return "'" + appInfo.getTitleName() + "' Buy";
    }

    @Override
    public boolean isPositive() {
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.TransactionType);
        dest.writeParcelable(this.appInfo, flags);
        dest.writeString(this.price);
    }

    public InvestTransactionModel() {
    }

    protected InvestTransactionModel(Parcel in) {
        this.TransactionType = in.readInt();
        this.appInfo = in.readParcelable(AppInfo.class.getClassLoader());
        this.price = in.readString();
    }

    public static final Parcelable.Creator<InvestTransactionModel> CREATOR = new Parcelable.Creator<InvestTransactionModel>() {
        @Override
        public InvestTransactionModel createFromParcel(Parcel source) {
            return new InvestTransactionModel(source);
        }

        @Override
        public InvestTransactionModel[] newArray(int size) {
            return new InvestTransactionModel[size];
        }
    };
}

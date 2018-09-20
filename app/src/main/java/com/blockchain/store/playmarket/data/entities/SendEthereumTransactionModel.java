package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class SendEthereumTransactionModel extends TransactionModel implements Parcelable {
    public int TransactionType = Constants.TransactionTypes.TRANSFER.ordinal();
    public String priceInWei;
    public String addressFrom;
    public String addressTo;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.TRANSFER;
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
        return "ETH transfer";
    }

    @Override
    public String getDetailedInfo() {
        return "Recipient address " + addressTo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.TransactionType);
        dest.writeString(this.priceInWei);
        dest.writeString(this.addressFrom);
        dest.writeString(this.addressTo);
    }

    public SendEthereumTransactionModel() {
    }

    protected SendEthereumTransactionModel(Parcel in) {
        this.TransactionType = in.readInt();
        this.priceInWei = in.readString();
        this.addressFrom = in.readString();
        this.addressTo = in.readString();
    }

    public static final Parcelable.Creator<SendEthereumTransactionModel> CREATOR = new Parcelable.Creator<SendEthereumTransactionModel>() {
        @Override
        public SendEthereumTransactionModel createFromParcel(Parcel source) {
            return new SendEthereumTransactionModel(source);
        }

        @Override
        public SendEthereumTransactionModel[] newArray(int size) {
            return new SendEthereumTransactionModel[size];
        }
    };
}

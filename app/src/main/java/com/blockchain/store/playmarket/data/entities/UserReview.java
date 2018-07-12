package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class UserReview implements Parcelable {
    private static final String TAG = "UserReview";
    public String txIndexOrigin;
    public int blockNumber;
    public String txIndex;
    public String description;
    public String vote;
    public String idApp;
    public String voter;

    public boolean isTxIndexIsEmpty() {
        txIndex = txIndex.replaceFirst("0x", "").replaceAll("0", "");
        Log.d(TAG, "isTxIndexIsEmpty: " + txIndex.isEmpty());
        return txIndex.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.txIndexOrigin);
        dest.writeInt(this.blockNumber);
        dest.writeString(this.txIndex);
        dest.writeString(this.description);
        dest.writeString(this.vote);
        dest.writeString(this.idApp);
        dest.writeString(this.voter);
    }

    public UserReview() {
    }

    protected UserReview(Parcel in) {
        this.txIndexOrigin = in.readString();
        this.blockNumber = in.readInt();
        this.txIndex = in.readString();
        this.description = in.readString();
        this.vote = in.readString();
        this.idApp = in.readString();
        this.voter = in.readString();
    }

    public static final Parcelable.Creator<UserReview> CREATOR = new Parcelable.Creator<UserReview>() {
        @Override
        public UserReview createFromParcel(Parcel source) {
            return new UserReview(source);
        }

        @Override
        public UserReview[] newArray(int size) {
            return new UserReview[size];
        }
    };
}

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
    public boolean isReviewOnReview = false;


    public boolean isTxIndexIsEmpty() {
        String tempString = txIndex.replaceFirst("0x", "").replaceAll("0", "");
        Log.d(TAG, "isTxIndexIsEmpty: " + tempString);
        return tempString.isEmpty();
    }

    public UserReview() {
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
        dest.writeByte(this.isReviewOnReview ? (byte) 1 : (byte) 0);
    }

    protected UserReview(Parcel in) {
        this.txIndexOrigin = in.readString();
        this.blockNumber = in.readInt();
        this.txIndex = in.readString();
        this.description = in.readString();
        this.vote = in.readString();
        this.idApp = in.readString();
        this.voter = in.readString();
        this.isReviewOnReview = in.readByte() != 0;
    }

    public static final Creator<UserReview> CREATOR = new Creator<UserReview>() {
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

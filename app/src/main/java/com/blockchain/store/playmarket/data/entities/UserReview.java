package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserReview implements Parcelable {
    private static final String TAG = "UserReview";
    public String hashTx;
    public String author;
    public String idApp;
    public String rating;
    public String text;
    @SerializedName("timestamp")
    public String timeStamp;
    public int blockNumber;
    public ArrayList<UserReview> responses = new ArrayList<>();
    public boolean isReviewOnReview = false;

    public UserReview() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hashTx);
        dest.writeString(this.author);
        dest.writeString(this.idApp);
        dest.writeString(this.rating);
        dest.writeString(this.text);
        dest.writeString(this.timeStamp);
        dest.writeInt(this.blockNumber);
        dest.writeTypedList(this.responses);
        dest.writeByte(this.isReviewOnReview ? (byte) 1 : (byte) 0);
    }

    protected UserReview(Parcel in) {
        this.hashTx = in.readString();
        this.author = in.readString();
        this.idApp = in.readString();
        this.rating = in.readString();
        this.text = in.readString();
        this.timeStamp = in.readString();
        this.blockNumber = in.readInt();
        this.responses = in.createTypedArrayList(UserReview.CREATOR);
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

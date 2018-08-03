package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PlaymarketFeed implements Parcelable {
    public String status;
    public ArrayList<PlaymarketFeedItem> items;
    public PlaymarketFeedField feed;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeTypedList(this.items);
    }

    public PlaymarketFeed() {
    }

    protected PlaymarketFeed(Parcel in) {
        this.status = in.readString();
        this.items = in.createTypedArrayList(PlaymarketFeedItem.CREATOR);
    }

    public static final Parcelable.Creator<PlaymarketFeed> CREATOR = new Parcelable.Creator<PlaymarketFeed>() {
        @Override
        public PlaymarketFeed createFromParcel(Parcel source) {
            return new PlaymarketFeed(source);
        }

        @Override
        public PlaymarketFeed[] newArray(int size) {
            return new PlaymarketFeed[size];
        }
    };
}

package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

class PlaymarketFeedField implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public PlaymarketFeedField() {
    }

    protected PlaymarketFeedField(Parcel in) {
    }

    public static final Parcelable.Creator<PlaymarketFeedField> CREATOR = new Parcelable.Creator<PlaymarketFeedField>() {
        @Override
        public PlaymarketFeedField createFromParcel(Parcel source) {
            return new PlaymarketFeedField(source);
        }

        @Override
        public PlaymarketFeedField[] newArray(int size) {
            return new PlaymarketFeedField[size];
        }
    };
}

package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

class IcoDocs implements Parcelable {
    public String whitepaper;
    public String onepage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.whitepaper);
        dest.writeString(this.onepage);
    }

    public IcoDocs() {
    }

    protected IcoDocs(Parcel in) {
        this.whitepaper = in.readString();
        this.onepage = in.readString();
    }

    public static final Parcelable.Creator<IcoDocs> CREATOR = new Parcelable.Creator<IcoDocs>() {
        @Override
        public IcoDocs createFromParcel(Parcel source) {
            return new IcoDocs(source);
        }

        @Override
        public IcoDocs[] newArray(int size) {
            return new IcoDocs[size];
        }
    };
}

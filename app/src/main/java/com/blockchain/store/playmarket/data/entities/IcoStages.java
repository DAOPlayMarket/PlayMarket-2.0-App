package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

class IcoStages implements Parcelable {
    public String time;
    public String price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeString(this.price);
    }

    public IcoStages() {
    }

    protected IcoStages(Parcel in) {
        this.time = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<IcoStages> CREATOR = new Parcelable.Creator<IcoStages>() {
        @Override
        public IcoStages createFromParcel(Parcel source) {
            return new IcoStages(source);
        }

        @Override
        public IcoStages[] newArray(int size) {
            return new IcoStages[size];
        }
    };
}

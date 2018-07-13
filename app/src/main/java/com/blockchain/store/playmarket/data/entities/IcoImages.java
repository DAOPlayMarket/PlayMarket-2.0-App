package com.blockchain.store.playmarket.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

class IcoImages implements Parcelable {
    public ArrayList<String> gallery;
    public String logo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.gallery);
        dest.writeString(this.logo);
    }

    public IcoImages() {
    }

    protected IcoImages(Parcel in) {
        this.gallery = in.createStringArrayList();
        this.logo = in.readString();
    }

    public static final Parcelable.Creator<IcoImages> CREATOR = new Parcelable.Creator<IcoImages>() {
        @Override
        public IcoImages createFromParcel(Parcel source) {
            return new IcoImages(source);
        }

        @Override
        public IcoImages[] newArray(int size) {
            return new IcoImages[size];
        }
    };
}
